package com.thesearch.mylaptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.thesearch.mylaptopshop.model.User;

public interface UserRepository extends  JpaRepository<User, Long>{
    boolean existsByEmail(String email);
    User findByEmail(String email);
    
    @Modifying
    @Transactional
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
}
