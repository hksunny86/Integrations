package com.example.dbscheduler.repository;

import com.example.dbscheduler.entity.BlinkCustomerLimit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface BlinkCustomerLimitRepository extends CrudRepository<BlinkCustomerLimit, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE BLINK_CUSTOMER_LIMIT b SET b.maximum = :maximum, b.updated_maximum=null WHERE b.limit_Id = :limitId", nativeQuery = true)
    public void updateMaximum(@Param("limitId") Long limitId, @Param("maximum") Long maximum);

}
