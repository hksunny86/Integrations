package com.inov8.microbank.webapp.listener;

import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;





public class I8MicrobankHttpSessionEventPublisher extends HttpSessionEventPublisher {
    private static final Log logger = LogFactory.getLog(I8MicrobankHttpSessionEventPublisher.class);
   

    public void sessionDestroyed(HttpSessionEvent event) {
//        logger.info("unpublishing session");
//        if (SecurityContextHolder.getContext(). == null) {
//            
//                        WebApplicationContextUtils.
//                             getWebApplicationContext(
//                              event.getSession().getServletContext()).getBean(name)
//                       
//        }

//        this.userContext.invalidate();
        super.sessionDestroyed(event);
    }

//    private  T lookupBean(final ApplicationContext applicationContext, final String beanName, final Class c) {
//        //noinspection unchecked
//        return (T) applicationContext.getBean(beanName, c);
//    }
}