package com.example.dbscheduler.service;

import com.example.dbscheduler.entity.DTO.UpdatedLimitsObjectDTO;
import com.example.dbscheduler.repository.BlinkCustomerLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlinkCustomerLimitService {

    @Autowired
    private BlinkCustomerLimitRepository customerLimitRepository;

    public void updateMaximumLimit(UpdatedLimitsObjectDTO updatedLimitsObjectDTO) {
        for (UpdatedLimitsObjectDTO.LimitDTO limit : updatedLimitsObjectDTO.getUpdatedLimitsObject()) {
            customerLimitRepository.updateMaximum(limit.getLimitId(), limit.getUpdateMaximum());
        }
    }

}
