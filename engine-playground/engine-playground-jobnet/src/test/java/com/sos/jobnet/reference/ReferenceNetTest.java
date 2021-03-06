package com.sos.jobnet.reference;

import com.sos.scheduler.engine.data.order.OrderFinishedEvent;
import com.sos.scheduler.engine.eventbus.EventHandler;
import com.sos.scheduler.engine.kernel.util.Time;
import com.sos.scheduler.engine.test.SchedulerTest;
import com.sos.scheduler.engine.test.util.CommandBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 * test for the reference jobnet
 */
@SuppressWarnings("deprecation")
public class ReferenceNetTest extends SchedulerTest {

    //TODO mit geeigneten Assertion die Korrektheit der Ausführung prüfen.

    private static final Logger logger = Logger.getLogger(ReferenceNetTest.class);

    private static final int totalNumberOfOrders = 8;    // 1 for JobNetPlanExecutor, 7 for Jobnet
    private int numberOfFinishedOrders = 0;
    private static final Time timeout = Time.of(300);

    @Test
    public void test() throws Exception {
//        controller().activateScheduler("-e","-log-level=debug");
        controller().activateScheduler();

        // This file connects with Oracle DB at 8of9
        String hibernateConfig = controller().scheduler().getConfiguration().localConfigurationDirectory() + "/hibernate.cfg.xml";
        scheduler().getVariables().put("hibernate_connection_config_file",hibernateConfig);

        CommandBuilder cmd = new CommandBuilder();
        String command = cmd.addOrder("jobnet_plan_creator", "reference").
                addParam("JobChainName", "reference").
                addParam("OrderId", "A").
                getCommand();
        scheduler().executeXml(command);
        controller().waitForTermination(timeout);
    }

    @EventHandler
    public void handleOrderEnd(OrderFinishedEvent e) throws IOException {
        logger.info("order " + e.getKey().toString() + " ended.");
        numberOfFinishedOrders++;
        if (numberOfFinishedOrders == totalNumberOfOrders)
            controller().terminateScheduler();
    }


}
