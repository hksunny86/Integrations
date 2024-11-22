package com.example.dbscheduler.config;

import com.example.dbscheduler.controller.SchedulerController;
import com.example.dbscheduler.entity.DTO.UpdatedLimitsObjectDTO;
import com.example.dbscheduler.service.AppUserService;
import com.example.dbscheduler.service.BlinkCustomerLimitService;
import com.example.dbscheduler.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Configuration
public class TaskConfig {
    Logger logger = LoggerFactory.getLogger(SchedulerController.class);
    @Bean
    public OneTimeTask<EmailUpdateTaskVo> emailUpdateTask(AppUserService appUserService) {
        return Tasks.oneTime("email-task", EmailUpdateTaskVo.class)
                .execute((inst, ctx) -> appUserService.updateEmail(inst.getData()));
    }

    @Bean
    public OneTimeTask<ResetPintTaskVo> resetPinTask(AppUserService appUserService) {
        return Tasks.oneTime("reset-pin-task", ResetPintTaskVo.class)
                .execute((inst, ctx) -> appUserService.resetPin(inst.getData()));
    }

    @Bean
    public OneTimeTask<ReleaseIbftTaskVo> releaseIbftTask(@Value("${blb.releaseibf.url}") String releaseIbftUrl) {
        return Tasks.oneTime("release-ibft-task", ReleaseIbftTaskVo.class)
                .execute((inst, ctx) -> {
                    System.out.println("One Time Release IBFT Task Executed with Data \n" + inst.getTaskName());
                    ReleaseIbftTaskVo requestObject = inst.getData();
                    ObjectMapper om = new ObjectMapper();
                    String requestStr = null;
                    try {
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
                        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        requestStr = om.writeValueAsString(requestObject);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseIbftUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!"00".equals(responseBody.get("responseCode"))) { // response if not succes
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15)); // retry after 15 seconds
                            }
                            logger.info("IBFT release Successfuly "+response.getBody());
                        } else {
                            logger.info("IBFT release FAILED  :: Rescheduling "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15)); // retry after 15 seconds
                        }
                    } catch (Exception e) {
                        logger.error("IBFT release FAILED  :: "+requestStr);
                        logger.error(e.getMessage());
                    }
                });
    }



    @Bean
    public OneTimeTask<ReleaseRaastTaskVo> releaseRaastTask(@Value("${blb.releaseInProcessBalance.url}") String releaseInProcessBalanceUrl) {
        return Tasks.oneTime("release-raast-task", ReleaseRaastTaskVo.class)
                .execute((inst, ctx) -> {
                    System.out.println("One Time Release RAAST Task Executed with Data \n" + inst.getTaskName());
                    ReleaseRaastTaskVo requestObject = inst.getData();
                    ObjectMapper om = new ObjectMapper();
                    String requestStr = null;
                    try {
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
                        requestStr = om.writeValueAsString(requestObject);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!"00".equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15));
                            }
                             logger.info("RAAST release Successfuly "+response.getBody());
                        } else {
                            logger.info("RAAST release FAILED :: Rescheduling "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15));
                        }
                    } catch (Exception e) {
                        logger.error("RAAST release FAILED "+requestStr);
                        logger.error(e.getMessage());
                    }
                });
    }



    @Bean
    public OneTimeTask<ReleaseCoreToWalletTaskVo> releaseCoreToWalletTask(@Value("${blb.releaseInProcessBalance.url}") String releaseInProcessBalanceUrl) {
        return Tasks.oneTime("release-core2wallet-task", ReleaseCoreToWalletTaskVo.class)
                .execute((inst, ctx) -> {
                    System.out.println("One Time Release Core To Wallet Task Executed with Data \n" + inst.getTaskName());
                    ReleaseCoreToWalletTaskVo requestObject = inst.getData();
                    ObjectMapper om = new ObjectMapper();
                    String requestStr = null;
                    try {
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
                        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
                        requestObject.setHashData(sha256hex);
                        requestStr = om.writeValueAsString(requestObject);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!"00".equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15));
                            }
                             logger.info("Core To Wallet release Successfuly "+response.getBody());
                        } else {
                            logger.info("Core To Wallet release FAILED :: Rescheduling "+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15));
                        }
                    } catch (Exception e) {
                        logger.error("Core To Wallet release FAILED :: "+requestStr);
                        logger.error(e.getMessage());
                    }
                });
    }



    @Bean
    public OneTimeTask<ReleaseZtoZTaskVo> releaseZtoZTask(@Value("${blb.releaseInProcessBalance.url}") String releaseInProcessBalanceUrl) {
        return Tasks.oneTime("release-z2z-task", ReleaseZtoZTaskVo.class)
                .execute((inst, ctx) -> {
                    System.out.println("One Time Release Z to Z Task Executed with Data \n" + inst.getTaskName());
                    ReleaseZtoZTaskVo requestObject = inst.getData();
                    ObjectMapper om = new ObjectMapper();
                    String requestStr = null;
                    try {
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
                        requestStr = om.writeValueAsString(requestObject);
                        RestTemplate restClient = new RestTemplate();
                        ResponseEntity<Map> response = restClient.postForEntity(releaseInProcessBalanceUrl, requestObject, Map.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            Map<String, Object> responseBody = response.getBody();
                            if (!"00".equals(responseBody.get("responseCode"))) {
                                ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(15));
                            }
                            logger.info("Z to Z release Successfuly "+response.getBody());
                        } else {
                            logger.info("Z to Z release FAILED :: Rescheduling :"+response.getBody());
                            ctx.getSchedulerClient().reschedule(inst, Instant.now().plusSeconds(5));
                        }
                    } catch (Exception e) {
                        logger.error("Z to Z release FAILED "+requestStr);
                        logger.error(e.getMessage());
                    }
                });
    }

    @Bean
    public OneTimeTask<UpdatedLimitsObjectDTO> updateLimitsTask(BlinkCustomerLimitService blinkCustomerLimitService) {
        return Tasks.oneTime("transaction-task", UpdatedLimitsObjectDTO.class)
                .execute((inst, ctx) -> blinkCustomerLimitService.updateMaximumLimit(inst.getData()));
    }

    // @Ahsan -- Additional tasks can be defined similarly
}
