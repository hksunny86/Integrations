package com.example.dbscheduler.controller;

import com.example.dbscheduler.entity.DTO.UpdatedLimitsObjectDTO;
import com.example.dbscheduler.entity.SystemConfig;
import com.example.dbscheduler.service.AppUserService;
import com.example.dbscheduler.service.BlinkCustomerLimitService;
import com.example.dbscheduler.service.SystemConfigService;
import com.example.dbscheduler.utils.SystemConfigConstants;
import com.example.dbscheduler.vo.*;
import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class SchedulerController {
    Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SystemConfigService configService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private BlinkCustomerLimitService blinkCustomerLimitService;
    private static final String RELEASE_IBFT_SUCCESS_RESP_CODE = "00";

    @Value("${blb.releaseibf.url}")
    private String releaseIbftUrl;

    @Value("${blb.releaseInProcessBalance.url}")
    private String releaseInProcessBalanceUrl;
    @PostMapping("/updateemail")
    public ResponseEntity<?> startEmailTask(@RequestBody EmailUpdateTaskVo emailUpdateTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_EMAIL_UPDATE_COOLING);
            // Save the admin
            OneTimeTask<EmailUpdateTaskVo> myAdhocTask = Tasks.oneTime("email-task", EmailUpdateTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Email Update Task Executed with Data \n" + inst.getTaskName());
                        // Call the service method to update the email
                        appUserService.updateEmail(emailUpdateTaskVo);
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(emailUpdateTaskVo.getAppUserId().toString(), emailUpdateTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Email Update Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Email Update Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetpin")
    public ResponseEntity<?> startResetTask(@RequestBody ResetPintTaskVo resetPintTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_RESET_PIN_COOLING);

            // Save the admin
            OneTimeTask<ResetPintTaskVo> myAdhocTask = Tasks.oneTime("reset-pin-task", ResetPintTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Reset Pin Task Executed with Data \n" + inst.getTaskName());
                        // Call the service method to update the email
                        appUserService.resetPin(resetPintTaskVo);
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(resetPintTaskVo.getAppUserId().toString(), resetPintTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Email Update Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Email Update Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/releaseibft")
    public ResponseEntity<?> startReleaseIBFTTask(@RequestBody ReleaseIbftTaskVo releaseIbftTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_RELEASE_IBFT_COOLING);
            // Save the admin
            OneTimeTask<ReleaseIbftTaskVo> myAdhocTask = Tasks.oneTime("release-ibft-task", ReleaseIbftTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Release IBFT Task Executed with Data \n" + inst.getTaskName());
                        ReleaseIbftTaskVo requestObject = inst.getData();
                        StringBuilder stringText = new StringBuilder()
                                .append(requestObject.getUserName())
                                .append(requestObject.getPassword())
                                .append(requestObject.getMobileNumber())
                                .append(requestObject.getTransactionId())
                                .append(requestObject.getReferenceNo())
                                .append(requestObject.getDateTime())
                                .append(requestObject.getRrn())
                                .append(requestObject.getChannelId())
                                .append(requestObject.getTerminalId())
                                .append(requestObject.getReserved1())
                                .append(requestObject.getReserved2())
                                .append(requestObject.getReserved3())
                                .append(requestObject.getReserved4())
                                .append(requestObject.getReserved5())
                                .append(requestObject.getReserved6())
                                .append(requestObject.getReserved7())
                                .append(requestObject.getReserved8())
                                .append(requestObject.getReserved9())
                                .append(requestObject.getReserved10());
                        String sha256hex =DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseIbftUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!RELEASE_IBFT_SUCCESS_RESP_CODE.equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                            }
                            logger.info("IBFT release Successfuly "+response.getBody());
                        } else {
                            logger.info("IBFT release FAILED "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                        }
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(releaseIbftTaskVo.getMobileNumber(), releaseIbftTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Release IBFT Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Release IBFT Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateblinkcustomerlimit")
    public ResponseEntity<?> updateMaximumLimit(@RequestBody UpdatedLimitsObjectDTO updatedLimitsObjectDTO) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_TRANS_LIMIT_COOLING);

            // Save the admin
            OneTimeTask<UpdatedLimitsObjectDTO> myAdhocTask = Tasks.oneTime("transaction-task", UpdatedLimitsObjectDTO.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Transaction Task Executed with Data \n" + inst.getTaskName());
                        blinkCustomerLimitService.updateMaximumLimit(updatedLimitsObjectDTO);
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(updatedLimitsObjectDTO.getUpdatedLimitsObject().get(0).getLimitId().toString(), updatedLimitsObjectDTO), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/releaseraast")
    public ResponseEntity<?> startReleaseRAASTTask(@RequestBody ReleaseRaastTaskVo releaseRaastTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_RELEASE_RAAST_COOLING);
            // Save the admin
            OneTimeTask<ReleaseRaastTaskVo> myAdhocTask = Tasks.oneTime("release-raast-task", ReleaseRaastTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Release RAAST Task Executed with Data \n" + inst.getTaskName());
                        ReleaseRaastTaskVo requestObject = inst.getData();
                        StringBuilder stringText = new StringBuilder()
                                .append(requestObject.getUserName())
                                .append(requestObject.getPassword())
                                .append(requestObject.getMobileNumber())
                                .append(requestObject.getDateTime())
                                .append(requestObject.getRrn())
                                .append(requestObject.getChannelId())
                                .append(requestObject.getTerminalId())
                                .append(requestObject.getProductId())
                                .append(requestObject.getReceiverMobileNo())
                                .append(requestObject.getTransactionAmount())
                                .append(requestObject.getReserved1())
                                .append(requestObject.getReserved2())
                                .append(requestObject.getReserved3())
                                .append(requestObject.getReserved4())
                                .append(requestObject.getReserved5())
                                .append(requestObject.getReserved6())
                                .append(requestObject.getReserved7())
                                .append(requestObject.getReserved8())
                                .append(requestObject.getReserved9())
                                .append(requestObject.getReserved10());
                        String sha256hex =DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!RELEASE_IBFT_SUCCESS_RESP_CODE.equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                            }
                            logger.info("RAAST release Successfuly "+response.getBody());
                        } else {
                            logger.info("RAAST release FAILED "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                        }
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(releaseRaastTaskVo.getMobileNumber(), releaseRaastTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Release RAAST Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Release RAAST Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/releasecoretowallet")
    public ResponseEntity<?> startReleaseCoreToWalletTask(@RequestBody ReleaseCoreToWalletTaskVo releaseCoreToWalletTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_RELEASE_CORE_TO_WALLET_COOLING);
            // Save the admin
            OneTimeTask<ReleaseCoreToWalletTaskVo> myAdhocTask = Tasks.oneTime("release-coretowallet-task", ReleaseCoreToWalletTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Release Core To Wallet Task Executed with Data \n" + inst.getTaskName());
                        ReleaseCoreToWalletTaskVo requestObject = inst.getData();
                        StringBuilder stringText = new StringBuilder()
                                .append(requestObject.getUserName())
                                .append(requestObject.getPassword())
                                .append(requestObject.getMobileNumber())
                                .append(requestObject.getDateTime())
                                .append(requestObject.getRrn())
                                .append(requestObject.getChannelId())
                                .append(requestObject.getTerminalId())
                                .append(requestObject.getProductId())
                                .append(requestObject.getReceiverMobileNo())
                                .append(requestObject.getTransactionAmount())
                                .append(requestObject.getReserved1())
                                .append(requestObject.getReserved2())
                                .append(requestObject.getReserved3())
                                .append(requestObject.getReserved4())
                                .append(requestObject.getReserved5())
                                .append(requestObject.getReserved6())
                                .append(requestObject.getReserved7())
                                .append(requestObject.getReserved8())
                                .append(requestObject.getReserved9())
                                .append(requestObject.getReserved10());
                        String sha256hex =DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!RELEASE_IBFT_SUCCESS_RESP_CODE.equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                            }
                            logger.info("Core To Wallet release Successfuly "+response.getBody());
                        } else {
                            logger.info("Core To Wallet release FAILED "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                        }
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(releaseCoreToWalletTaskVo.getMobileNumber(), releaseCoreToWalletTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Release Core To Wallet Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Release Core To Wallet Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/releaseztoz")
    public ResponseEntity<?> startReleaseZtoZTask(@RequestBody ReleaseZtoZTaskVo releaseZtoZTaskVo) {
        try {
            long coolingPeriod = getCoolingPeriod(SystemConfigConstants.TASK_RELEASE_Z_TO_Z_COOLING);
            // Save the admin
            OneTimeTask<ReleaseZtoZTaskVo> myAdhocTask = Tasks.oneTime("release-ztoz-task", ReleaseZtoZTaskVo.class)
                    .execute((inst, ctx) -> {
                        System.out.println("One Time Release Z to Z Task Executed with Data \n" + inst.getTaskName());
                        ReleaseZtoZTaskVo requestObject = inst.getData();
                        StringBuilder stringText = new StringBuilder()
                                .append(requestObject.getUserName())
                                .append(requestObject.getPassword())
                                .append(requestObject.getMobileNumber())
                                .append(requestObject.getDateTime())
                                .append(requestObject.getRrn())
                                .append(requestObject.getChannelId())
                                .append(requestObject.getTerminalId())
                                .append(requestObject.getProductId())
                                .append(requestObject.getReceiverMobileNo())
                                .append(requestObject.getTransactionAmount())
                                .append(requestObject.getReserved1())
                                .append(requestObject.getReserved2())
                                .append(requestObject.getReserved3())
                                .append(requestObject.getReserved4())
                                .append(requestObject.getReserved5())
                                .append(requestObject.getReserved6())
                                .append(requestObject.getReserved7())
                                .append(requestObject.getReserved8())
                                .append(requestObject.getReserved9())
                                .append(requestObject.getReserved10());
                        String sha256hex =DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!RELEASE_IBFT_SUCCESS_RESP_CODE.equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                            }
                            logger.info("Z to Z release Successfuly "+response.getBody());
                        } else {
                            logger.info("Z to Z release FAILED "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(coolingPeriod));
                        }
                    });

            final Scheduler scheduler = Scheduler
                    .create(dataSource, myAdhocTask)
                    .registerShutdownHook()
                    .build();

            scheduler.start();
            scheduler.schedule(myAdhocTask.instance(releaseZtoZTaskVo.getReceiverMobileNo(), releaseZtoZTaskVo), Instant.now().plusSeconds(coolingPeriod));

            return new ResponseEntity<>("Release Z to Z Task Scheduled Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<>("Release Z to Z Task Scheduled Failed", HttpStatus.BAD_REQUEST);
        }
    }

    private long getCoolingPeriod(String key) throws Exception {
        Optional<SystemConfig> systemConfigOptional = configService.findById(key);

        // Use primitive long directly and handle invalid values
        long coolingPeriod = systemConfigOptional
                .map(systemConfig -> {
                    try {
                        return Long.parseLong(systemConfig.getValue());  // Parse the value directly to long
                    } catch (NumberFormatException e) {
                        return 0L;  // Return 0 if the value is not a valid number
                    }
                }).orElse(0L);  // Default to 0L if no value found

        // If coolingPeriod is 0, set it to 15 seconds
        if (coolingPeriod == 0L) {
            coolingPeriod = 15L;
        }

        return coolingPeriod;
    }


    @GetMapping("/test/{name}")
    @ResponseBody
    public ResponseEntity<?> test(@PathVariable String name) {
        return new ResponseEntity<>("Hello "+name, HttpStatus.OK);
    }
}
