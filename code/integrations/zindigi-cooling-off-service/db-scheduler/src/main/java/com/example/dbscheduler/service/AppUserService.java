package com.example.dbscheduler.service;

import com.example.dbscheduler.repository.AppUserRepository;
import com.example.dbscheduler.vo.EmailUpdateTaskVo;
import com.example.dbscheduler.vo.ResetPintTaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;


    public void resetPin(ResetPintTaskVo resetPintTaskVo) {
        Long appUserId = resetPintTaskVo.getAppUserId();
        appUserRepository.updateCoolingOffPeriod(appUserId);
    }

    public void updateEmail(EmailUpdateTaskVo emailUpdateTaskVo) {
        Long appUserId = emailUpdateTaskVo.getAppUserId();
        String email = emailUpdateTaskVo.getUpdatedEmail();
        appUserRepository.updateEmail(appUserId, email);
    }

    public void updateLimits() {

    }
}
