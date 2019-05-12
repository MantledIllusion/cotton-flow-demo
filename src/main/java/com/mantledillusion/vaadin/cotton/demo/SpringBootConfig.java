package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.Blueprint;
import com.mantledillusion.injection.hura.core.annotation.instruction.Define;
import com.mantledillusion.vaadin.cotton.CottonEnvironment;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan
public class SpringBootConfig implements Blueprint {

    @Define
    public PropertyAllocation defineActivateAutomaticRouteDiscovery() {
        return CottonEnvironment.forAutomaticRouteDiscovery(true);
    }
}
