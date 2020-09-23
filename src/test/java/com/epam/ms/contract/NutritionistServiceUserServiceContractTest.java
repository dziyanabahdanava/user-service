package com.epam.ms.contract;

import au.com.dius.pact.provider.junit.Consumer;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.StateChangeAction;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.MalformedURLException;

@Provider("user_service")
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Consumer("nutritionist_service")
@Slf4j
public class NutritionistServiceUserServiceContractTest {
    @LocalServerPort
    private int port;

    @State(value="GET request for user profile is processed successfully", action= StateChangeAction.SETUP)
    public void setupGetState() {
        System.out.println("1");
    }

    @State(value="GET request for user profile is processed successfully", action= StateChangeAction.TEARDOWN)
    public void postGetState() {
        System.out.println("2");
    }

    @BeforeEach
    void before(PactVerificationContext context) throws MalformedURLException {
         context.setTarget(new HttpTestTarget("localhost", port, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
