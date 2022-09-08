package com.ironhack.ironbankapi.auth.repository;

import com.ironhack.ironbankapi.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryKeycloak {
}
