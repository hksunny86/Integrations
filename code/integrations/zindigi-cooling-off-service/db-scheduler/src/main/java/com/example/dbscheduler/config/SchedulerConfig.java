package com.example.dbscheduler.config;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;

@Configuration
public class SchedulerConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private List<Task<?>> tasks;  // Inject all tasks

    @Bean
    public Scheduler scheduler() {
        return Scheduler
                .create(dataSource, tasks)
                .pollingInterval(Duration.ofSeconds(30))  // Adjust polling interval as needed
                .threads(5)                              // Configure thread pool size as appropriat
                .registerShutdownHook()    // Wait for task completion on shutdown
                .build();
    }
}
