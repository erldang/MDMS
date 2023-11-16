package com.example.backend.repository;

import com.example.backend.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain,Integer> {


    @Query("select d from Domain d where d.name = ?1")
    Domain findDomainByName(String name);

}
