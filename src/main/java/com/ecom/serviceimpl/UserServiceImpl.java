package com.ecom.serviceimpl;

import com.ecom.model.UserDetails;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        user.setAccountNonLocked(true);
        user.setFailAttempt(0);
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

    @Override
    public void increaseFailedAttempt(UserDetails user) {
        int attempt =  user.getFailAttempt() + 1;
        user.setFailAttempt(attempt);
        userRepository.save(user);

    }

    @Override
    public void userAccountLock(UserDetails user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);


    }

    @Override
    public boolean unlockAccountTimeExpired(UserDetails user) {

        long lockTime =user.getLockTime().getTime();
        long unLockTime = lockTime+ AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime<currentTime){

            user.setAccountNonLocked(true);
            user.setFailAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }


}
