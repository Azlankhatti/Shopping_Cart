package com.ecom.serviceimpl;

import com.ecom.model.UserDetails;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails saveUser(UserDetails user) {
        user.setRole("USER_ROLE");
        user.setEnable(true);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserDetails saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDetails> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public boolean updateAccountStatus(Integer id, Boolean status) {

        Optional<UserDetails> findByuser = userRepository.findById(id);

        if(findByuser.isPresent()){
            UserDetails userDetails = findByuser.get();
            userDetails.setEnable(status);
            userRepository.save(userDetails);
            return true;


        }

        return false;
    }


}
