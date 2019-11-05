package com.quemepongo.persistence;

import com.quemepongo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByEmail(String email);

}
