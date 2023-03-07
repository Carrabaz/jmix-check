package com.company.training.screen.stage;

import com.company.training.entity.Contract;
import com.company.training.entity.CustomSettings;
import com.company.training.entity.Stage;
import io.jmix.appsettings.AppSettings;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Objects;

@UiController("t_Stage.edit")
@UiDescriptor("stage-edit.xml")
@EditedEntityContainer("stageDc")
public class StageEdit extends StandardEditor<Stage> {

    private static final Logger log = LoggerFactory.getLogger(StageEdit.class);

    @Autowired
    private AppSettings appSettings;

    /**
     * Добавление default значений
     *
     * @param event
     */
    @Subscribe
    public void onInitEntity(InitEntityEvent<Stage> event) {
        log.info("<< Set default stage values >>");
        Contract contract = event.getEntity().getContract();
        BigDecimal stageVat = appSettings.load(CustomSettings.class).getStageVat();
        Stage stage = event.getEntity();
        stage.setAmount(Objects.isNull(contract.getAmount()) ? 0 : contract.getAmount());
        stage.setVat(BigDecimal.valueOf(stage.getAmount())
                .multiply(stageVat)
                .multiply(computeCustomerVat(contract)));
        stage.setTotalAmount(stage.getAmount() + stage.getVat().intValue());
    }

    private BigDecimal computeCustomerVat(Contract customer) {
        boolean escapeVat = (customer.getCustomer().getEscapeVat() == null) ? true : false;
        return escapeVat ? BigDecimal.valueOf(0) : BigDecimal.valueOf(1);
    }
}