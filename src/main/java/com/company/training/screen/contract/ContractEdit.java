package com.company.training.screen.contract;

import com.company.training.entity.Contract;
import com.company.training.entity.CustomSettings;
import com.company.training.entity.ServiceCompletionCertificate;
import com.company.training.entity.Stage;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Table;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionPropertyContainer;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@UiController("t_Contract.edit")
@UiDescriptor("contract-edit.xml")
@EditedEntityContainer("contractDc")
public class ContractEdit extends StandardEditor<Contract> {
    private static final Logger log = LoggerFactory.getLogger(ContractEdit.class);
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CollectionPropertyContainer<Stage> stagesDc;
    @Autowired
    private Table<Stage> stagesTable;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private TextField<Integer> amountField;
    @Autowired
    private TextField<BigDecimal> vatField;
    @Autowired
    private Notifications notifications;
    @Autowired
    private AppSettings appSettings;

    @Subscribe("stagesTable.createCertificate")
    public void onStagesTableCreateCertificate(Action.ActionPerformedEvent event) {

    }

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

    /**
     * Создание этапа по умолчанию, если пользователь не стал создавать этапов
     *
     * @param event
     */
    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPostCommit(DataContext.PostCommitEvent event) {
        log.info("<< Post Commit event >>");
        BigDecimal stageVat = appSettings.load(CustomSettings.class).getStageVat();
        Contract editedContract = event.getCommittedInstances().getAll(Contract.class).stream().findFirst().orElseThrow();
        if (editedContract.getStages().size() == 0) {
            Stage initialStage = dataManager.create(Stage.class);
            initialStage.setName("Start and End");
            initialStage.setDateFrom(editedContract.getDateFrom());
            initialStage.setDateTo(editedContract.getDateTo());
            initialStage.setAmount(editedContract.getAmount());
            initialStage.setVat(BigDecimal.valueOf(initialStage.getAmount())
                    .multiply(stageVat)
                    .multiply(computeCustomerVat(getEditedEntity())));
            initialStage.setTotalAmount(initialStage.getAmount() + initialStage.getVat().intValue());
            initialStage.setDescription("Start till End");
            initialStage.setContract(editedContract);
            dataManager.save(initialStage);
        }
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
        amountField.setEditable(getEditedEntity().getStages() == null);
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

    private int calculateTotalAmount(CollectionContainer.CollectionChangeEvent<Stage> event) {
        return event.getSource().getItems().stream().mapToInt(Stage::getAmount).sum();
    }

    private BigDecimal computeCustomerVat(Contract customer) {
        boolean escapeVat = (customer.getCustomer().getEscapeVat() == null) ? true : false;
        return escapeVat ? BigDecimal.valueOf(0) : BigDecimal.valueOf(1);
    }
}