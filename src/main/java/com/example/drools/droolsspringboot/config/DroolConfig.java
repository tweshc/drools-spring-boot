package com.example.drools.droolsspringboot.config;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.io.impl.ReaderResource;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.StringReader;

@Configuration
@Slf4j
public class DroolConfig {

    private KieServices kieServices = KieServices.Factory.get();
    public static final String RULE_FILE = "offer.drl";

    private KieFileSystem getKieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULE_FILE));
        return kieFileSystem;
    }

    @Bean
    public KieContainer getKieContainer() throws IOException {
        log.info("BEAN created - 'getKieContainer' created...");
        getKieRepository();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer;
    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }

    @Bean
    public KieSession getKieSession() throws IOException{
        log.info("BEAN created - Kie session created...");
        return getKieContainer().newKieSession();
    }

    private static final String MOCK_DB_RESULT = "package KieRule;\n" +
            "import com.example.drools.droolsspringboot.model.Order;\n" +
            "\n" +
            "rule \"HDFC\"\n" +
            "\n" +
            "when\n" +
            "orderObject : Order(cardType==\"HDFC\" && price>10000);\n" +
            "then\n" +
            "orderObject.setDiscount(10);\n" +
            "end;\n" +
            "\n" +
            "rule \"ICICI\"\n" +
            "\n" +
            "when\n" +
            "orderObject : Order(cardType==\"ICICI\" && price>15000);\n" +
            "then\n" +
            "orderObject.setDiscount(8);\n" +
            "end;\n" +
            "\n" +
            "rule \"DBS\"\n" +
            "\n" +
            "when\n" +
            "orderObject : Order(cardType==\"DBS\" && price>15000);\n" +
            "then\n" +
            "orderObject.setDiscount(15);\n" +
            "end;";
}
