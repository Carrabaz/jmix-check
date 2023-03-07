package com.company.training.screen.bpmjmix;

import io.jmix.bpmui.processform.annotation.Outcome;
import io.jmix.bpmui.processform.annotation.ProcessForm;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UiController("t_BpmJmixScreen")
@UiDescriptor("Bpm-jmix-screen.xml")
@ProcessForm(
        outcomes = {
                @Outcome(id = "approve"),
                @Outcome(id = "reject")
        }
)
public class BpmJmixScreen extends Screen {

    private static final Logger log = LoggerFactory.getLogger(BpmJmixScreen.class);

    @Subscribe
    public void onInit(InitEvent event) {
        log.info("Initialization custom screen");
    }
}