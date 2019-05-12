package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.Blueprint;
import com.mantledillusion.vaadin.cotton.CottonServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;

@WebServlet("/*")
public class DemoServlet extends CottonServlet {

    @Autowired
    public DemoServlet(Blueprint cottonEnvironment) {
        super(cottonEnvironment);
    }
}
