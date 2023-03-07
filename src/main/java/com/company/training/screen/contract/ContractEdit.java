package com.company.training.screen.contract;

import com.company.training.entity.Contract;
import com.company.training.entity.CustomSettings;
import com.company.training.entity.ServiceCompletionCertificate;
import com.company.training.entity.Stage;
import io.jmix.appsettings.AppSettings;
import io.jmix.bpmui.processform.ProcessFormContext;
import io.jmix.bpmui.processform.annotation.Outcome;
import io.jmix.bpmui.processform.annotation.Param;
import io.jmix.bpmui.processform.annotation.ProcessForm;
import io.jmix.bpmui.processform.annotation.ProcessFormParam;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionPropertyContainer;
import io.jmix.ui.screen.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@UiController("t_Contract.edit")
@UiDescriptor("contract-edit.xml")
@EditedEntityContainer("contractDc")
@ProcessForm(
        params = {
                @Param(name = "contract")
        },
        outcomes = {
                @Outcome(id = "execute_yes"),
                @Outcome(id = "execute_no"),
                @Outcome(id = "execute_archive_yes"),
                @Outcome(id = "execute_archive_no")
        }
)
public class ContractEdit extends StandardEditor<Contract> {

    private static final Logger log = LoggerFactory.getLogger(ContractEdit.class);

    @Autowired
    private Notifications notifications;

    @Autowired
    private AppSettings appSettings;

    @Autowired
    private ScreenBuilders screenBuilders;

    @Autowired
    private CollectionPropertyContainer<Stage> stagesDc;

    @Autowired
    private TextField<BigDecimal> vatField;

    @Autowired
    private TextField<Integer> amountField;

    @Autowired
    private Table<Stage> stagesTable;

    @Autowired
    private ProcessFormContext processFormContext;

    @ProcessFormParam(name = "contract")
    private Contract contractVariable;

    @Autowired
    private ButtonsPanel stageButtonPanel;

    @Autowired
    private Button executedBtn;

    @Autowired
    private Button archivedBtn;

    @Autowired
    private HBoxLayout editActions;

    /**
     * Добавление сертификата к этапу контракта
     *
     * @param event
     */
    @Subscribe("addCertificate")
    public void onAddCertificateClick(Button.ClickEvent event) {
        log.info("<< Add Certificate Click event >>");
        BigDecimal certificateVat = appSettings.load(CustomSettings.class).getCertificateVat();
        if (stagesTable.getSingleSelected() == null) {
            notifications.create().withDescription("Select stage").show();
        }
        Stage selectedStage = stagesTable.getSingleSelected();
        screenBuilders.editor(ServiceCompletionCertificate.class, this)
                .newEntity()
                .withInitializer(entity -> {
                    entity.setDate(LocalDate.now());
                    entity.setAmount(selectedStage.getAmount());
                    entity.setVat(BigDecimal.valueOf(entity.getAmount())
                            .multiply(certificateVat)
                            .multiply(computeCustomerVat(getEditedEntity())));
                    entity.setTotalAmount(entity.getAmount() + entity.getVat().intValue());
                })
                .build().show();
    }

    @Install(to = "stagesTable.create", subject = "screenOptionsSupplier")
    private ScreenOptions stagesTableCreateScreenOptionsSupplier() {
        return new MapScreenOptions(Map.of("Name", "Named_1"));
    }

    /**
     * Изменение численных значений у контракта после модификаций в этапах
     *
     * @param afterShowEvent
     */
    @Subscribe
    public void onAfterShow(AfterShowEvent afterShowEvent) {
        log.info("<< Set values for contract on his stages >>");
        Contract c = getEditedEntity();
        amountField.setEditable(Objects.isNull(getEditedEntity().getStages()) || getEditedEntity().getStages().size() == 0);
        BigDecimal contractVat = appSettings.load(CustomSettings.class).getContractVat();
        stagesDc.addCollectionChangeListener(event -> {
            log.info("Be change in collection listener");
            boolean editable = event.getSource().getItems().size() == 0;
            amountField.setEditable(editable);
            amountField.setValue(calculateTotalAmount(event));
            vatField.setValue(BigDecimal.valueOf(amountField.getValue())
                    .multiply(contractVat)
                    .multiply(computeCustomerVat(getEditedEntity())));
            getEditedEntity().setTotalAmount(amountField.getValue() + vatField.getValue().intValue());
        });
    }

    @Subscribe
    public void onInit(InitEvent event) {
        log.info("InitEvent");
        if (!Objects.isNull(contractVariable)) {
            setEntityToEdit(contractVariable);
            stageButtonPanel.setVisible(false);
            editActions.setVisible(false);
            if (StringUtils.equals(contractVariable.getStatus().getName(), "Действует")) {
                executedBtn.setVisible(true);
            }
            if (StringUtils.equals(contractVariable.getStatus().getName(), "Исполнен")) {
                archivedBtn.setVisible(true);
            }
        }
    }

    @Subscribe("executedBtn")
    public void onExecutedBtnClick(Button.ClickEvent event) {
        processFormContext.taskCompletion().withOutcome("execute_yes").complete();
        closeWithDefaultAction();
    }

    @Subscribe("archivedBtn")
    public void onArchivedBtnClick(Button.ClickEvent event) {
        processFormContext.taskCompletion().withOutcome("execute_archive_yes").complete();
        closeWithDefaultAction();
    }

    private int calculateTotalAmount(CollectionContainer.CollectionChangeEvent<Stage> event) {
        return event.getSource().getItems().stream().mapToInt(Stage::getAmount).sum();
    }

    private BigDecimal computeCustomerVat(Contract customer) {
        boolean escapeVat = (customer.getCustomer().getEscapeVat() == null) ? true : false;
        return escapeVat ? BigDecimal.valueOf(0) : BigDecimal.valueOf(1);
    }
}