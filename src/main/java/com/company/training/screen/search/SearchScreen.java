package com.company.training.screen.search;

import io.jmix.searchui.component.SearchField;
import io.jmix.ui.component.HasContextHelp;
import io.jmix.ui.screen.Install;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UiController("t_SearchScreen")
@UiDescriptor("Search-screen.xml")

public class SearchScreen extends Screen {

    private static final Logger log = LoggerFactory.getLogger(SearchScreen.class);

    @Install(to = "searchId", subject = "searchCompletedHandler")
    private void searchIdSearchCompletedHandler(SearchField.SearchCompletedEvent searchCompletedEvent) {
        log.info("Search method");
    }
}