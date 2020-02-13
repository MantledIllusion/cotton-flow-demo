package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DemoUser implements User {

    static final String RIGHT_EDITOR = "_editor";

    static final User USER_EDITOR = new DemoUser(RIGHT_EDITOR);
    static final User USER_NONE = new DemoUser();

    private final Set<String> rightIds;

    private DemoUser(String... rightIds) {
        this.rightIds = new HashSet<>(Arrays.asList(rightIds));
    }

    @Override
    public boolean hasRights(Set<String> rightIds) {
        return this.rightIds.containsAll(rightIds);
    }
}
