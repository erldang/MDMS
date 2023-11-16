package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.name = ?1")
    List<User> findByNameAndPhone(String name , String phone);

    @Query("select u from User u where u.email = ?1 and u.name = ?2 and u.phone = ?3")
    User findPassword(String email , String name , String phone);

}
