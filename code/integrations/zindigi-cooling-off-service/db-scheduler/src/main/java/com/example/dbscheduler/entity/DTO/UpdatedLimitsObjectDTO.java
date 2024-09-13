package com.example.dbscheduler.entity.DTO;

import java.io.Serializable;
import java.util.List;

public class UpdatedLimitsObjectDTO implements Serializable {

    private static final long serialversionUID = 99L;
    private List<LimitDTO> updatedLimitsObject;

    public List<LimitDTO> getUpdatedLimitsObject() {
        return updatedLimitsObject;
    }

    public void setUpdatedLimitsObject(List<LimitDTO> updatedLimitsObject) {
        this.updatedLimitsObject = updatedLimitsObject;
    }

    public static class LimitDTO implements Serializable {

        private static final long serialversionUID = 98L;
        private Long limitId;
        private Long updateMaximum;

        // Getters and setters

        public Long getLimitId() {
            return limitId;
        }

        public void setLimitId(Long limitId) {
            this.limitId = limitId;
        }

        public Long getUpdateMaximum() {
            return updateMaximum;
        }

        public void setUpdateMaximum(Long updateMaximum) {
            this.updateMaximum = updateMaximum;
        }
    }
}
