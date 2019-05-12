package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.CottonServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet("/*")
public class DemoServlet extends CottonServlet {

    public DemoServlet() {
        super(new DemoEnvironment());
    }
}
