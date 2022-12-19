package com.inov8.microbank.common.util;

/**
 * @author Soofiafa
 */
public interface RegistrationStateConstants {
    Long BULK_REQUEST_RECEIVED = 1L;
    Long REQUEST_RECEIVED = 2L;
    Long VERIFIED = 3L;
    Long DISCREPANT = 4L;
    Long DECLINE = 5L;
    Long REJECTED = 6L;
    Long BLACK_LISTED = 7L;
    Long DECEASED = 8L;
    Long CLSPENDING = 11L;
    Long BLINK_PENDING = 12L;
    Long BLOCKED = 14L;
    Long DISCREPANT_REQUEST_RECEIVED = 15L;
}