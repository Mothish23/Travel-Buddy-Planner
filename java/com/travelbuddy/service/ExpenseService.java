package com.travelbuddy.service;

import com.travelbuddy.entity.Expense;
import com.travelbuddy.entity.Trip;
import com.travelbuddy.exception.ResourceNotFoundException;
import com.travelbuddy.repository.ExpenseRepository;
import com.travelbuddy.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;

    public ExpenseService(ExpenseRepository expenseRepository, TripRepository tripRepository) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
    }

    public List<Expense> getExpenses(Long tripId, String email) {
        getTrip(tripId, email);
        return expenseRepository.findByTripIdOrderByExpenseDateDesc(tripId);
    }

    public Expense createExpense(Long tripId, Expense expense, String email) {
        Trip trip = getTrip(tripId, email);
        expense.setTrip(trip);
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updated, String email) {
        Expense existing = expenseRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        existing.setTitle(updated.getTitle());
        existing.setCategory(updated.getCategory());
        existing.setAmount(updated.getAmount());
        existing.setExpenseDate(updated.getExpenseDate());
        return expenseRepository.save(existing);
    }

    public void deleteExpense(Long id, String email) {
        Expense expense = expenseRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expenseRepository.delete(expense);
    }

    private Trip getTrip(Long tripId, String email) {
        return tripRepository.findByIdAndUserEmail(tripId, email).orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }
}