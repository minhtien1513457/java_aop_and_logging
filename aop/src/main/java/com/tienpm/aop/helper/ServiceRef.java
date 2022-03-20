package com.tienpm.aop.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

public class ServiceRef {

    public static ServiceRef INSTANCE;

    public ServiceRef(){
        INSTANCE=this;
    }

    @Autowired
    public ApplicationContext context;

    @Autowired
    public WebClient webClient;
}