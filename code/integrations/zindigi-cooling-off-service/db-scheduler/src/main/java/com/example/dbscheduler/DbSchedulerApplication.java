package com.example.dbscheduler;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@SpringBootApplication
public class DbSchedulerApplication {

	private static final Logger log = LoggerFactory.getLogger(DbSchedulerApplication.class);
	public static void main(String[] args) {

//		SpringApplication.run(DbSchedulerApplication.class, args);
		ConfigurableApplicationContext ctx = SpringApplication.run(DbSchedulerApplication.class, args);
	}


//	@Bean
//	CommandLineRunner executeOnStartup(Scheduler scheduler, @Qualifier("sampleOneTimeTask") Task<Void> sampleOneTimeTask) {
//		log.info("Scheduling one time task to now!");
//
//		return ignored ->
//				scheduler.schedule(sampleOneTimeTask.instance("command-line-runner"), Instant.now());
//	}
}
