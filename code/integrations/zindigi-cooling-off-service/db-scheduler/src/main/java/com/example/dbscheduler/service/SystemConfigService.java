package com.example.dbscheduler.service;

import com.example.dbscheduler.entity.SystemConfig;
import com.example.dbscheduler.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository configRepository;

    public Optional<SystemConfig> findById(String key) {
        return configRepository.findById(key);
    }
}
