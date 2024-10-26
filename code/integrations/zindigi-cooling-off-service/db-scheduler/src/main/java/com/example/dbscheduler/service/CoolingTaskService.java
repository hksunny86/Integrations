package com.example.dbscheduler.service;

import com.example.dbscheduler.entity.CoolingTask;
import com.example.dbscheduler.repository.CoolingTaskRepo;
import com.example.dbscheduler.utils.ExampleContext;
import com.github.kagkarlsson.scheduler.task.TaskWithDataDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class CoolingTaskService {

    Logger logger = LoggerFactory.getLogger(CoolingTaskService.class);

    @Autowired
    CoolingTaskRepo coolingTaskRepo;
    public TaskWithDataDescriptor BASIC_ONE_TIME_TASK;

    private static final Map<String, Consumer<ExampleContext>> TASK_STARTERS = new HashMap<>();

    public CoolingTask save(CoolingTask coolingTask) {
        return coolingTaskRepo.save(coolingTask);
    }
}
