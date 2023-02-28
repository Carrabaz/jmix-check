package com.company.training.app;

import io.jmix.bpm.engine.events.UserTaskAssignedEvent;
import io.jmix.core.DataManager;
import io.jmix.email.Emailer;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("smpl_TaskAssigned")
public class TaskAssignSender implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(TaskAssignSender.class);

    private Emailer emailer;

    private DataManager dataManager;

    private RuntimeService runtimeService;

    TaskAssignSender(Emailer emailer, DataManager dataManager, RuntimeService runtimeService) {
        this.emailer = emailer;
        this.dataManager = dataManager;
        this.runtimeService = runtimeService;
    }

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
