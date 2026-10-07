package com.travelbuddy.repository;

import com.travelbuddy.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripIdOrderByExpenseDateDesc(Long tripId);
    Optional<Expense> findByIdAndTripUserEmail(Long id, String email);
}