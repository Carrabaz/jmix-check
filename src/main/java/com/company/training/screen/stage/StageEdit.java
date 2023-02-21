package com.company.training.screen.stage;

import com.company.training.app.Vat;
import com.company.training.entity.Contract;
import com.company.training.entity.Stage;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@UiController("t_Stage.edit")
@UiDescriptor("stage-edit.xml")
@EditedEntityContainer("stageDc")
public class StageEdit extends StandardEditor<Stage> {

    private static final Logger log = LoggerFactory.getLogger(StageEdit.class);

    @Autowired
    private TextField<String> nameField;
    private String name;
    @Autowired
    private Vat vat;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        log.info("before show method");
        /*if (name != null) {
            nameField.setValue(name);
        }*/
        //getScreenData().getDataContext().getParent()
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onChange(DataContext.ChangeEvent event) {
        log.info("After added Stage entity");
        //getScreenData().getDataContext().getParent().getModified()

    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Stage> event) {
        log.info("init entity method");
        //DataContext dc = getScreenData().getDataContext().getParent().;
        //Set<Object> set = getScreenData().getDataContext().getParent().getModified();
        //long count = getScreenData().getDataContext().getParent().getModified().stream().count();
        Contract contract = event.getEntity().getContract();
        Stage stage = event.getEntity();
        stage.setAmount(contract.getAmount());
        stage.setVat(BigDecimal.valueOf(stage.getAmount())
                .multiply(BigDecimal.valueOf(vat.getVatForStages()).divide(BigDecimal.valueOf(100)))
                .multiply(BigDecimal.valueOf(1)));
        stage.setTotalAmount(stage.getAmount() + stage.getVat().intValue());
    }


    @Subscribe
    public void onInit(InitEvent event) {
        /*if (event.getOptions() instanceof MapScreenOptions) {
            MapScreenOptions options = (MapScreenOptions) event.getOptions();
            name = (String) options.getParams().get("Name");

        }*/
        log.info("onInit method ");
    }

}