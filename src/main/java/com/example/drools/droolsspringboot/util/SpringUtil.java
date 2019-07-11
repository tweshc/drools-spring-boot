package com.example.drools.droolsspringboot.util;

import com.example.drools.droolsspringboot.model.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@RestController
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    @GetMapping(value="/getAllBeanNames")
    public List<String> getAllBeanNames(){
        log.info("total Bean Count: {}", String.valueOf(appContext.getBeanDefinitionCount()));
        return Arrays.asList(appContext.getBeanDefinitionNames());
    }

    @GetMapping(value="/destroyBean")
    public Boolean destroyBean(@RequestParam(value="beanName") String beanName){
        log.info("Attempting to destroy bean with beanId: {}", beanName);
        log.info("registry contains this bean? {}",
                getBeanDefinitionRegistry().containsBeanDefinition(beanName));
        getBeanDefinitionRegistry().removeBeanDefinition(beanName);
        return Boolean.TRUE;
    }

    @GetMapping(value="/addBean/{beanName}")
    public String addBean(@PathVariable String beanName){
        GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
        myBeanDefinition.setBeanClass(TestBean.class);
        myBeanDefinition.setScope(SCOPE_SINGLETON);
        myBeanDefinition.setPropertyValues(getMutableProperties());
        getBeanDefinitionRegistry().registerBeanDefinition(beanName, myBeanDefinition);

        //return value of field of new TestBean instance
        return getBean(beanName, TestBean.class).getS();
    }

    private MutablePropertyValues getMutableProperties(){
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("s", "I am a new bean now.");
        return mpv;
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry(){
        AutowireCapableBeanFactory factory = appContext.getAutowireCapableBeanFactory();
        return (BeanDefinitionRegistry) factory;
    }

    public <T> T getBean(String beanId, Class<T> beanClass) {
        return appContext.getBean(beanId, beanClass);
    }

    @Bean
    public TestBean myBean222(){
        return new TestBean("testValue");
    }

}
