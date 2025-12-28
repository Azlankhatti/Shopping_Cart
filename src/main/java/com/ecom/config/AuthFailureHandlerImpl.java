package com.ecom.config;

import com.ecom.model.UserDetails;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");

        UserDetails userDetails = userRepository.findByEmail(email);

        if (userDetails.isEnable()){
            if (userDetails.isAccountNonLocked()){

                if (userDetails.getFailAttempt()< AppConstant.ATTEMPT_TIME){

                }

            }else {
                exception = new LockedException("your account is inactive");
            }

        }else {
            exception = new LockedException("your account is inactive");
        }


        super.onAuthenticationFailure(request, response, exception);
    }
}
