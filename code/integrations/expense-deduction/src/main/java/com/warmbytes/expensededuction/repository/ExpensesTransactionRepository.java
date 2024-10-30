package com.warmbytes.expensededuction.repository;

import com.warmbytes.expensededuction.model.ExpensesTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ExpensesTransactionRepository extends CrudRepository<ExpensesTransaction, Long> {

    @Query(value = "SELECT * from EXPENSES_TRANSACTION where mobile_no = :mobileNo and is_Process = 0 and TRANSACTION_STATUS IS NULL " +
            " and rownum = 1 order by created_on desc ", nativeQuery = true)
    public ExpensesTransaction findByMobileNo(@Param("mobileNo") String mobileNo);
}
