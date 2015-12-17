package org.netcs.service;

import org.netcs.model.sql.User;
import org.netcs.model.sql.form.UserCreateForm;

import java.util.List;
import java.util.Set;

public interface UserService {

    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User create(UserCreateForm form);

}