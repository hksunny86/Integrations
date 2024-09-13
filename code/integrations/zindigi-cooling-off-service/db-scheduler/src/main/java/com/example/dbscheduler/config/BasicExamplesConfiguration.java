package com.example.dbscheduler.config;

import com.example.dbscheduler.controller.AdminController;
import com.example.dbscheduler.entity.CoolingTask;
import com.example.dbscheduler.utils.CounterService;
import com.example.dbscheduler.utils.EventLogger;
import com.example.dbscheduler.utils.ExampleContext;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.TaskWithDataDescriptor;
import com.github.kagkarlsson.scheduler.task.TaskWithoutDataDescriptor;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static com.github.kagkarlsson.scheduler.task.schedule.Schedules.fixedDelay;

@Configuration
public class BasicExamplesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BasicExamplesConfiguration.class);

//    public static final TaskWithoutDataDescriptor BASIC_ONE_TIME_TASK =
//            new TaskWithoutDataDescriptor("sample-one-time-task");
    public static final TaskWithoutDataDescriptor BASIC_RECURRING_TASK =
            new TaskWithoutDataDescriptor("recurring-sample-task");
    public static TaskWithDataDescriptor BASIC_ONE_TIME_TASK =
            new TaskWithDataDescriptor("sample-one-time-task", CoolingTask.class);
    private static final Logger log = LoggerFactory.getLogger(BasicExamplesConfiguration.class);
    private static int ID = 1;

    /** Start the example */
    public static void triggerOneTime(ExampleContext ctx) {
        ctx.log(
                "Scheduling a basic one-time task to run 'Instant.now()+seconds'. If seconds=7, the scheduler will pick ");

        ctx.schedulerClient.schedule(BASIC_ONE_TIME_TASK.instance(String.valueOf(ID++), CoolingTask.class), Instant.now().plusSeconds(7));
    }

    /**
     * Define a recurring task with a dependency, which will automatically be picked up by the Spring
     * Boot autoconfiguration.
     */
//    @Bean
//    Task<Void> sampleOneTimeTask(CounterService counter) {
//        return Tasks.recurring(BASIC_ONE_TIME_TASK, fixedDelay(Duration.ofMinutes(1)))
//                .execute(
//                        (instance, ctx) -> {
//                            log.info("Running recurring-simple-task. Instance: {}, ctx: {}", instance, ctx);
//                            counter.increase();
//                            EventLogger.logTask(
//                                    BASIC_ONE_TIME_TASK, "Ran. Run-counter current-value=" + counter.read());
//                        });
//    }

//    @Bean
//    Task<TaskExample> recurringSampleTask(CounterService counter) {
//        return Tasks.oneTime("sample-one-time-task", TaskExample.class)
//                .execute(
//                        (instance, ctx) -> {
//                            log.info("Running recurring-simple-task. Instance: {}, ctx: {}", instance, ctx);
//                            counter.increase();
//                            EventLogger.logTask(
//                                    BASIC_ONE_TIME_TASK, "Ran. Run-counter current-value=" + counter.read());
//                        });
//    }


//    @Bean
//    Task<Void> recurringSampleTask(CounterService counter) {
//        return Tasks.recurring(BASIC_RECURRING_TASK, fixedDelay(Duration.ofMinutes(1)))
//                .execute(
//                        (instance, ctx) -> {
//                            log.info("Running recurring-simple-task. Instance: {}, ctx: {}", instance, ctx);
//                            counter.increase();
//                            EventLogger.logTask(
//                                    BASIC_RECURRING_TASK, "Ran. Run-counter current-value=" + counter.read());
//                        });
//    }

    /** Define a one-time task which have to be manually scheduled. */
    @Bean
    Task<Void> sampleOneTimeTask() {
        return Tasks.oneTime(BASIC_ONE_TIME_TASK)
                .execute(
                        (instance, ctx) -> {
                            log.info("I am a one-time task!");
//                        return null;
                        });
    }
}

