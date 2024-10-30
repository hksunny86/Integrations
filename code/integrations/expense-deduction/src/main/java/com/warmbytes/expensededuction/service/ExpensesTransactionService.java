package com.warmbytes.expensededuction.service;

import com.warmbytes.expensededuction.model.ExpensesTransaction;
import com.warmbytes.expensededuction.repository.ExpensesTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpensesTransactionService {

    @Autowired
    private ExpensesTransactionRepository repository;

    public ExpensesTransaction findByMobileNo(String mobileNo) {
        return repository.findByMobileNo(mobileNo);
    }

    public ExpensesTransaction save(ExpensesTransaction expensesTransaction) {
        return repository.save(expensesTransaction);
    }
}
