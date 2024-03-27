package com.test.component;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class GracefulShutdown implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("Me dead bruh!!");
    }
}
