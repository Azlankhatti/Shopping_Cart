package com.ecom.serviceimpl;

import com.ecom.model.UserDetails;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails saveUser(UserDetails user) {
        UserDetails saveUser = userRepository.save(user);
        return saveUser;
    }
}
