package com.example.drools.droolsspringboot.util;

import com.example.drools.droolsspringboot.model.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    ApplicationContext appContext;

    @GetMapping(value="/destroyBean")
    public Boolean destroyBean(@RequestParam(value="beanName") String beanName){
        getBeanDefinitionRegistry().removeBeanDefinition(beanName);
        return Boolean.TRUE;
    }

    @GetMapping(value="/addBean")
    public void addBean(){

    }

    @GetMapping(value="/getAllBeanNames")
    public List<String> getAllBeanNames(){
        log.info(String.valueOf(appContext.getBeanDefinitionCount()));
        return Arrays.asList(appContext.getBeanDefinitionNames());
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry(){
        AutowireCapableBeanFactory factory = appContext.getAutowireCapableBeanFactory();
        return (BeanDefinitionRegistry) factory;
    }

    @Bean
    public TestBean myBean(){
        return new TestBean("testValue");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
