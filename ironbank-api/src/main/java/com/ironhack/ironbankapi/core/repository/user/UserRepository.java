package com.ironhack.ironbankapi.core.repository.user;

import com.ironhack.ironbankapi.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
