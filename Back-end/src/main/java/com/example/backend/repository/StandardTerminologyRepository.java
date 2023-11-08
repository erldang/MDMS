package com.example.backend.repository;

import com.example.backend.entity.StandardTerminology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardTerminologyRepository extends JpaRepository<StandardTerminology , Integer> {
}
