package com.company.training.listener;

import com.company.training.app.StageService;
import com.company.training.entity.Contract;
import com.company.training.entity.Status;
import com.company.training.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.event.EntitySavingEvent;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component("t_ContractEventListener")
public class ContractEventListener {

    @Autowired
    StageService stageService;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private RuntimeService runtimeService;

    @EventListener
    public void onContractSaving(EntitySavingEvent<Contract> event) {
        Contract contract = event.getEntity();
        if (event.isNewEntity()) {
            Status newStatus = dataManager.load(Status.class)
                    .query("select s from t_Status s where s.name = 'Действует'")
                    .one();
            contract.setStatus(newStatus);

            startChangeStatusProcess(contract);
        }
    }

    @EventListener
    public void onContractChangedBeforeCommit(EntityChangedEvent<Contract> event) {
        if (event.getType() != EntityChangedEvent.Type.DELETED) {
            Contract contract = dataManager.load(event.getEntityId()).one();
            if (Objects.isNull(contract.getStages()) || contract.getStages().size() == 0) {
                stageService.createDefaultStage(contract);
            }
        }
    }

    private void startChangeStatusProcess(Contract contract) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("id", LocalDateTime.now());
        processVariables.put("contract", contract);
        processVariables.put("manager", dataManager.load(User.class)
                .query("select u from t_User u where u.username like 'Manager%'")
                .list().stream().findAny().get());
        runtimeService.startProcessInstanceByKey("AutoStartChangeStatus", processVariables);
    }
}