package com.example.dbscheduler.repository;

import com.example.dbscheduler.entity.CoolingTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoolingTaskRepo extends CrudRepository<CoolingTask, Long> {
}
