package com.ecom.service;

import com.ecom.model.UserDetails;

import java.util.List;


public interface UserService {

    public UserDetails saveUser(UserDetails user);

    public UserDetails getUserByEmail(String email);

    public List<UserDetails> getUsers(String role);

    boolean updateAccountStatus(Integer id, Boolean status);
}
