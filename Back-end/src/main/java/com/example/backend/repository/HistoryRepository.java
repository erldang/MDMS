package com.example.backend.repository;

import com.example.backend.entity.History;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {


    @Query("select h from History h where h.email = ?1")
    List<History> findHistoryByEmail(String history);

    @Modifying
    @Transactional
    @Query("delete History h where h.email = ?1")
    void deleteHistoryByEmail(String email);
}
