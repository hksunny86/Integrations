package com.inov8.integration.middleware.util;

import com.inov8.integration.vo.MiddlewareMessageVO;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.inov8.integration.middleware.util.EncryptionUtil.decrypt;
import static com.inov8.integration.middleware.util.EncryptionUtil.encrypt;

@Component
@Aspect
public class RequestCryptoAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.inov8.integration.middleware.controller..*)")
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

            // Encrypt Returning Object
            encryptRequest(result);

            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }



            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    private void encryptRequest(Object obj) {
        if (obj != null && obj instanceof MiddlewareMessageVO) {
            MiddlewareMessageVO messageVO = (MiddlewareMessageVO) obj;
            // Do Encryption Again

            if (StringUtils.isNotEmpty(messageVO.getPAN())) {
                messageVO.setPAN(encrypt(messageVO.getPAN()));
            }

            if (StringUtils.isNotEmpty(messageVO.getCardNo())) {
                messageVO.setCardNo(encrypt(messageVO.getCardNo()));
            }

            if (StringUtils.isNotEmpty(messageVO.getPinBlock())) {
                messageVO.setPinBlock(encrypt(messageVO.getPinBlock()));
            }

            if (StringUtils.isNotEmpty(messageVO.getNewPinBlock())) {
                messageVO.setNewPinBlock(encrypt(messageVO.getNewPinBlock()));
            }
            if (StringUtils.isNotEmpty(messageVO.getPinBlock())) {
                messageVO.setNewPinBlock(encrypt(messageVO.getPinBlock()));
            }

        }
    }

    private void decryptRequest(Object[] params) {
        if (params != null && params.length > 0) {
            Object param = params[0];
            if (param instanceof MiddlewareMessageVO) {
                MiddlewareMessageVO messageVO = (MiddlewareMessageVO) param;
                // Do Decryption Here

                if (StringUtils.isNotEmpty(messageVO.getPAN())) {
                    messageVO.setPAN(decrypt(messageVO.getPAN()));
                }

                if (StringUtils.isNotEmpty(messageVO.getCardNo())) {
                    messageVO.setCardNo(decrypt(messageVO.getCardNo()));
                }

                if (StringUtils.isNotEmpty(messageVO.getPinBlock())) {
                    messageVO.setPinBlock(decrypt(messageVO.getPinBlock()));
                }

                if (StringUtils.isNotEmpty(messageVO.getNewPinBlock())) {
                    messageVO.setNewPinBlock(decrypt(messageVO.getNewPinBlock()));
                }

            }
        }
    }
}
