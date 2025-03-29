package com.gabriaum.mission.user.controller;

import com.gabriaum.mission.user.User;

import java.util.HashMap;
import java.util.UUID;

public class UserController extends HashMap<UUID, User> {

    public void register(User user) {
        put(user.getUniqueId(), user);
    }
}
