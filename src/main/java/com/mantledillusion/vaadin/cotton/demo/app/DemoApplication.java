package com.mantledillusion.vaadin.cotton.demo.app;

import com.mantledillusion.injection.hura.weblaunch.HuraWeblaunchApplication;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;

public class DemoApplication {

    private static final ResourceManager VAADIN_RESOURCE_MANAGER =
            new ClassPathResourceManager(DemoApplication.class.getClassLoader(), "META-INF/resources/");

    public static void main(String[] args) {
        HuraWeblaunchApplication
                .build(DemoApplicationInitializer.class)
                .setResourceManager(VAADIN_RESOURCE_MANAGER)
                .startUp(args);
    }
}