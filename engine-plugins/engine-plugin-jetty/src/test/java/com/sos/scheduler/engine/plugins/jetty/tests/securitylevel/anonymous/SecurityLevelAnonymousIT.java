package com.sos.scheduler.engine.plugins.jetty.tests.securitylevel.anonymous;

import com.sos.scheduler.engine.test.SchedulerTest;
import com.sos.scheduler.engine.test.util.CommandBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import org.junit.Test;

import static com.sos.scheduler.engine.plugins.jetty.test.JettyPluginTests.contextUri;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public final class SecurityLevelAnonymousIT extends SchedulerTest {

    private static final String xmlCommand = new CommandBuilder().startJobImmediately("a").getCommand();

    @Test
    public void test() throws Exception {
        controller().activateScheduler();
        testUser(ClientResponse.Status.OK, "task enqueued");
    }

    private void testUser(ClientResponse.Status expectedStatus, String expectedResult) throws Exception {
        Client webClient = Client.create();
        WebResource webResource = webClient.resource(new URI(contextUri(scheduler().injector()) + "/jobscheduler/engine-cpp/command"));
        ClientResponse response = webResource.post(ClientResponse.class, xmlCommand);
        assertThat(response.getStatus(), equalTo(expectedStatus.getStatusCode()));
        assertThat(response.getEntity(String.class), containsString(expectedResult));
        webClient.destroy();
    }
}
