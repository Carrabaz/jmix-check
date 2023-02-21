package com.company.training.app;

import io.jmix.bpm.engine.events.UserTaskAssignedEvent;
import io.jmix.bpmui.screen.modeler.properties.usertask.UserTaskPropertiesFragment;
import io.jmix.core.DataManager;
import io.jmix.email.Emailer;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component("smpl_TaskAssigned")
public class TaskAssignSender implements TaskListener {
    private static final Logger log = LoggerFactory.getLogger(TaskAssignSender.class);

    @Autowired
    private Emailer emailer;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private RuntimeService runtimeService;

    @EventListener
    public void onTaskAssigned(UserTaskAssignedEvent event) {
        log.info("Task Id - " + event.getTask().getId());
        log.info("Attempt to set Task Listener");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("Attempt to set Task Listener");
    }
}
