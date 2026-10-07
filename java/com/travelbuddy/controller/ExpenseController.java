package com.travelbuddy.controller;

import com.travelbuddy.entity.Expense;
import com.travelbuddy.service.ExpenseService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/trips/{tripId}/expenses")
    public ResponseEntity<List<Expense>> getExpenses(@PathVariable Long tripId, Authentication authentication) {
        return ResponseEntity.ok(expenseService.getExpenses(tripId, authentication.getName()));
    }

    @PostMapping("/trips/{tripId}/expenses")
    public ResponseEntity<Expense> createExpense(@PathVariable Long tripId, @Valid @RequestBody Expense expense, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(tripId, expense, authentication.getName()));
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense, Authentication authentication) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expense, authentication.getName()));
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication authentication) {
        expenseService.deleteExpense(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}