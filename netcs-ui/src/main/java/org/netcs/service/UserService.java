package org.netcs.service;

import org.netcs.model.sql.User;

import java.util.List;

public interface UserService {

    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();
}