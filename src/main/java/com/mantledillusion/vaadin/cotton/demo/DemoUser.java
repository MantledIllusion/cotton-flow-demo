package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DemoUser implements User {

    static final String RIGHT_SUPER_SENSITIVE_DATA = "_admin";
    static final String RIGHT_SENSITIVE_DATA = "_casual";

    static final User USER_POWER = new DemoUser(RIGHT_SUPER_SENSITIVE_DATA);
    static final User USER_CASUAL = new DemoUser(RIGHT_SENSITIVE_DATA);
    static final User USER_GUEST = new DemoUser();

    private final Set<String> rightIds;

    private DemoUser(String... rightIds) {
        this.rightIds = new HashSet<>(Arrays.asList(rightIds));
    }

    @Override
    public boolean hasRights(Set<String> rightIds) {
        return this.rightIds.containsAll(rightIds);
    }
}
