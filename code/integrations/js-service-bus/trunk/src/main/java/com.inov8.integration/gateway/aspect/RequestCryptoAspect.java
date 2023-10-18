package com.inov8.integration.gateway.aspect;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.inov8.integration.util.EncryptionUtil.*;

@Component
@Aspect
public class RequestCryptoAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.inov8.integration.gateway.invoker..*)")
    public void loggingPointcut() {
    }

    @Around("loggingPointcut()")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            }

            // Decrypt Received Object
            decryptRequest(joinPoint.getArgs());
            Object result = joinPoint.proceed();

            // Returning Object
            encryptRequest(result);

            if (log.isDebugEnabled())

                return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
        return null;
    }

    private void encryptRequest(Object obj) {
        if (obj != null && obj instanceof I8SBSwitchControllerRequestVO) {
            I8SBSwitchControllerRequestVO messageVO = (I8SBSwitchControllerRequestVO) obj;
            // Do Encryption Again
            if (StringUtils.isNotEmpty(messageVO.getSmsText())) {
                messageVO.setSmsText(encrypt(messageVO.getSmsText()));
            }
            if (StringUtils.isNotEmpty(messageVO.getFPIN())) {
                messageVO.setFPIN(encrypt(messageVO.getFPIN()));
            }
            if (StringUtils.isNotEmpty(messageVO.getPinData())) {
                messageVO.setPinData(encrypt(messageVO.getPinData()));
            }
            if (StringUtils.isNotEmpty(messageVO.getPAN())) {
                messageVO.setPAN(encrypt(messageVO.getPAN()));
            }
            if (StringUtils.isNotEmpty(String.valueOf(messageVO.getRequestType().equalsIgnoreCase(I8SBConstants.RequestType_ONE_LINK_IBFT_TITLE_FETCH)))) {
                messageVO.setAccountId1(messageVO.getAccountId1());
                messageVO.setAccountId2(messageVO.getAccountId2());
            } else {
                if (StringUtils.isNotEmpty(messageVO.getAccountId1())) {
                    messageVO.setAccountId1(encrypt(messageVO.getAccountId1()));
                }
                //log.debug("After Returning AccountId1=============================================="+messageVO.getAccountId1());
                if (StringUtils.isNotEmpty(messageVO.getAccountId2())) {
                    messageVO.setAccountId2(encrypt(messageVO.getAccountId2()));
                }
            }
            //log.debug("Before Returning AccountId1=============================================="+messageVO.getAccountId1());
            if (StringUtils.isNotEmpty(messageVO.getAccountNumber())) {
                messageVO.setAccountNumber(encrypt(messageVO.getAccountNumber()));
            }
            if (StringUtils.isNotEmpty(messageVO.getPassword())) {
                messageVO.setPassword(encrypt(messageVO.getPassword()));
            }
            if (StringUtils.isNotEmpty(messageVO.getNewPassword())) {
                messageVO.setNewPassword(encrypt(messageVO.getNewPassword()));
            }
            if (StringUtils.isNotEmpty(messageVO.getPasscode())) {
                messageVO.setPasscode(encrypt(messageVO.getPasscode()));
            }
        }
    }

    private void decryptRequest(Object[] params) {

        if (params != null && params.length > 0) {
            Object param = params[0];
            if (param instanceof I8SBSwitchControllerRequestVO) {
                I8SBSwitchControllerRequestVO messageVO = (I8SBSwitchControllerRequestVO) param;
                // Do Decryption Here
                //below line comment for meezan
//                if (StringUtils.isNotEmpty(messageVO.getSmsText())) {
//                    messageVO.setSmsText(decrypt(messageVO.getSmsText()));
//                }
                if (StringUtils.isNotEmpty(messageVO.getFPIN())) {
                    messageVO.setFPIN(decrypt(messageVO.getFPIN()));
                }
                if (StringUtils.isNotEmpty(messageVO.getPinData())) {
                    messageVO.setPinData(decrypt(messageVO.getPinData()));
                }
                if (StringUtils.isNotEmpty(messageVO.getPAN())) {
                    messageVO.setPAN(decrypt(messageVO.getPAN()));
                }
                if (StringUtils.isNotEmpty(String.valueOf(messageVO.getRequestType().equalsIgnoreCase(I8SBConstants.RequestType_ONE_LINK_IBFT_TITLE_FETCH)))) {
                    messageVO.setAccountId1(messageVO.getAccountId1());
                    messageVO.setAccountId2(messageVO.getAccountId2());
                } else {
                    if (StringUtils.isNotEmpty(messageVO.getAccountId1())) {
                        messageVO.setAccountId1(encrypt(messageVO.getAccountId1()));
                    }
                    //log.debug("After Returning AccountId1=============================================="+messageVO.getAccountId1());
                    if (StringUtils.isNotEmpty(messageVO.getAccountId2())) {
                        messageVO.setAccountId2(encrypt(messageVO.getAccountId2()));
                    }
                }
                if (StringUtils.isNotEmpty(messageVO.getAccountNumber())) {
                    messageVO.setAccountNumber(decrypt(messageVO.getAccountNumber()));
                }
                if (StringUtils.isNotEmpty(messageVO.getPassword())) {
                    messageVO.setPassword(decrypt(messageVO.getPassword()));
                }
                if (StringUtils.isNotEmpty(messageVO.getNewPassword())) {
                    messageVO.setNewPassword(decrypt(messageVO.getNewPassword()));
                }
                if (StringUtils.isNotEmpty(messageVO.getPasscode())) {
                    messageVO.setPasscode(decrypt(messageVO.getPasscode()));
                }
                if (StringUtils.isNotEmpty(messageVO.getDebitAccountNumber())) {
                    messageVO.setDebitAccountNumber(decrypt(messageVO.getDebitAccountNumber()));
                }
                if (StringUtils.isNotEmpty(messageVO.getCreditAccount())) {
                    messageVO.setCreditAccount(decrypt(messageVO.getCreditAccount()));
                }
                if (StringUtils.isNotEmpty(messageVO.getChargedebitAccountNumber())) {
                    messageVO.setChargedebitAccountNumber(decrypt(messageVO.getChargedebitAccountNumber()));
                }
                if (StringUtils.isNotEmpty(messageVO.getSenderAccountNumber())) {
                    messageVO.setSenderAccountNumber(decrypt(messageVO.getSenderAccountNumber()));
                }

                if (StringUtils.isNotEmpty(messageVO.getCardId())) {
                    messageVO.setCardId(decrypt(messageVO.getCardId()));
                }
                if (StringUtils.isNotEmpty(messageVO.getFromAccountNumber())) {
                    messageVO.setFromAccountNumber(decrypt(messageVO.getFromAccountNumber()));
                }
                if (StringUtils.isNotEmpty(messageVO.getToAccountNumber())) {
                    messageVO.setToAccountNumber(decrypt(messageVO.getToAccountNumber()));
                }


            }
        }
    }
}
