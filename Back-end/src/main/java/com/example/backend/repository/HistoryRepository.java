package com.example.backend.repository;

import com.example.backend.entity.History;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, CriteriaBuilder.In> {
}
