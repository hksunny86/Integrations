package com.example.dbscheduler.repository;

import com.example.dbscheduler.entity.AppUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE APP_USER ap SET ap.COOLING_OFF_APPLICABLE = 0, ap.COOLING_OFF_TIME=null WHERE ap.APP_USER_ID = :appUserId", nativeQuery = true)
    public void updateCoolingOffPeriod(@Param("appUserId") Long appUserId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE APP_USER ap SET ap.email = :email, updated_email=null, ap.email_updated_on=null WHERE ap.APP_USER_ID = :appUserId", nativeQuery = true)
    public void updateEmail(@Param("appUserId") Long appUserId, @Param("email") String email);

}
