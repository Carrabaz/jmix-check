package com.company.training.screen.bpmjmix;

import com.company.training.entity.Contract;
import io.jmix.bpm.data.form.FormData;
import io.jmix.bpm.service.BpmModelService;
import io.jmix.bpmui.processform.*;
import io.jmix.bpmui.processform.annotation.Outcome;
import io.jmix.bpmui.processform.annotation.ProcessForm;
import io.jmix.bpmui.processform.annotation.ProcessVariable;
import io.jmix.core.DataManager;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("t_BpmJmixScreen")
@UiDescriptor("Bpm-jmix-screen.xml")
@EditedEntityContainer("contractDc")
@ProcessForm(
        outcomes = {
                @Outcome(id = "approve"),
                @Outcome(id = "reject")
        }
)
public class BpmJmixScreen extends StandardEditor<Contract> {

    private static final Logger log = LoggerFactory.getLogger(BpmJmixScreen.class);

    @Autowired
    @ProcessVariable
    private TextField<String> field1;

    @Autowired
    private ProcessFormContext processFormContext;

    @Autowired
    private BpmModelService bpmModelService;

    @Autowired
    protected ProcessFormScreens processFormScreens;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(InitEvent event) {

        log.info("Initialization custom screen");

        ProcessDefinition def = processFormContext.getProcessDefinition();
        Task t = processFormContext.getTask();
        FormData f = processFormContext.getFormData();

        ProcessStarting p = processFormContext.processStarting();
        TaskClaiming tt = processFormContext.taskClaiming();
        TaskCompletion tc = processFormContext.taskCompletion();

        log.info("Ended ");
    }

    @Subscribe
    public void onInit1(InitEvent event) {
        setEntityToEdit(dataManager.load(Contract.class).query("select u from t_Contract u where u.number = 1254").one());
    }

    /*@Subscribe("clo")
    public void onCloClick(Button.ClickEvent event) {
        processFormContext.taskCompletion()
                .withOutcome("approve")
                .saveInjectedProcessVariables()
                .complete();
        closeWithDefaultAction();
    }*/

/*    @Subscribe("clo")
    public void onChangeState(Action.ActionPerformedEvent event) {
        log.info("Action clo");
        processFormContext.taskCompletion()
                .withOutcome("approve")
                .saveInjectedProcessVariables()
                .complete();
        closeWithDefaultAction();
    }*/



    @Subscribe("cluc")
    public void onCluc(Action.ActionPerformedEvent event) {
        log.info("Action clo");
        processFormContext.taskCompletion()
                .withOutcome("approve")
                .saveInjectedProcessVariables()
                .complete();
        closeWithDefaultAction();
    }

    @Autowired
    private TaskService taskService;

    @Subscribe("openTaskBtn")
    public void onOpenTaskBtnClick(Button.ClickEvent event) {

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("approve-order-process")
                .taskAssignee("admin")
                .active()
                .orderByTaskCreateTime()
                .list()
                .get(0);

        Screen taskProcessForm = processFormScreens.createTaskProcessForm(task, this);
        taskProcessForm.show();
    }
}