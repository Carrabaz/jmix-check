package com.company.training.app;

import com.company.training.entity.Contract;
import com.company.training.entity.Status;
import io.jmix.core.DataManager;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("changeStatus")
public class ChangeStatus implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(ChangeStatus.class);
    @Autowired
    DataManager dataManager;
    @Autowired
    private Emailer emailer;

    public int setStatus(Contract contract, String status) {
        Status newStatus = dataManager.load(Status.class)
                .query("select s from t_Status s where s.name = :status")
                .parameter("status", status)
                .one();
        contract.setStatus(newStatus);
        dataManager.save(contract);
        log.info("Contract status changed");
        return 1;
    }

    @Override
    public void execute(DelegateExecution execution) {
        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses("mdg@gptrans.gazprom.ru")
                .setSubject("Testing Subject")
                .setFrom(null)
                .setBody("Some sentences.")
                .build();
        emailer.sendEmailAsync(emailInfo);
        log.info("Send email after change status.");
    }
}
