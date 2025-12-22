package com.ecom.repository;

import com.ecom.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

    public UserDetails findByEmail(String email);

    List<UserDetails> findByRole(String role);
}
