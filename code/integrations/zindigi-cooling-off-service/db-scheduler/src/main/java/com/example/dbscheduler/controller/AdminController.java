package com.example.dbscheduler.controller;

import com.example.dbscheduler.config.BasicExamplesConfiguration;
import com.example.dbscheduler.utils.ExampleContext;
import com.github.kagkarlsson.scheduler.SchedulerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    // Endpoints
    public static final String LIST_SCHEDULED = "/tasks";
    public static final String START = "/start";
    public static final String STOP = "/stop";

    private static final Map<String, Consumer<ExampleContext>> TASK_STARTERS = new HashMap<>();

    static {
        TASK_STARTERS.put(
                BasicExamplesConfiguration.BASIC_ONE_TIME_TASK.getTaskName(),
                BasicExamplesConfiguration::triggerOneTime);

    }

    private final SchedulerClient schedulerClient;
    private final ExampleContext exampleContext;

    public AdminController(SchedulerClient schedulerClient, TransactionTemplate tx) {
        this.schedulerClient = schedulerClient;
        exampleContext = new ExampleContext(schedulerClient, tx, LOG);
    }

    @GetMapping(path = LIST_SCHEDULED)
    public List<Scheduled> list() {
        LOG.info("Hello word");
        return schedulerClient.getScheduledExecutions().stream()
                .map(
                        e -> {
                            return new Scheduled(
                                    e.getTaskInstance().getTaskName(),
                                    e.getTaskInstance().getId(),
                                    e.getExecutionTime(),
                                    e.getData());
                        })
                .collect(Collectors.toList());
    }

//    @PostMapping(
//            path = START,
//            headers = {"Content-type=application/json"})
//    public void start(@RequestBody StartRequest request) {
//        TASK_STARTERS.get(request.taskName).accept(exampleContext);
//    }
    @PostMapping(
            path = START)
    public void start(@RequestBody StartRequest request) {
        TASK_STARTERS.get(request.taskName).accept(exampleContext);
    }

    @PostMapping(
            path = STOP,
            headers = {"Content-type=application/json"})
    public void stop(@RequestBody StartRequest request) {
        schedulerClient.getScheduledExecutions().stream()
                .filter(s -> s.getTaskInstance().getTaskName().equals(request.taskName))
                .findAny()
                .ifPresent(s -> schedulerClient.cancel(s.getTaskInstance()));
    }

    public static class StartRequest {
        public final String taskName;

        public StartRequest() {
            this("");
        }

        public StartRequest(String taskName) {
            this.taskName = taskName;
        }
    }

    public static class Scheduled {
        public final String taskName;
        public final String id;
        public final Instant executionTime;
        public final Object data;

        public Scheduled() {
            this(null, null, null, null);
        }

        public Scheduled(String taskName, String id, Instant executionTime, Object data) {
            this.taskName = taskName;
            this.id = id;
            this.executionTime = executionTime;
            this.data = data;
        }
    }
}
