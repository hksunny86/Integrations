package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.XmlRpcErrorLogger;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class XmlRpcUSSDServerController {
    private static Logger logger = LoggerFactory.getLogger(XmlRpcUSSDServerController.class.getSimpleName());

    @Autowired(required = false)
    private String encoding = XmlRpcServerConfigImpl.UTF8_ENCODING;
    @Autowired(required = false)
    private boolean enabledForExceptions = false;
    @Autowired(required = false)
    private boolean enabledForExtensions = true;
    @Autowired
    private Map<String, RPCHandler> handlers;

    private XmlRpcServletServer server;

    @PostConstruct
    protected void init() throws Exception {
        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
        config.setBasicEncoding(encoding);
        config.setEnabledForExceptions(enabledForExceptions);
        config.setEnabledForExtensions(enabledForExtensions);

        server = new CustomXmlRpcServletServer();
        server.setConfig(config);
        server.setErrorLogger(new XmlRpcErrorLogger());
        server.setTypeFactory(new StringTypeFactoryImpl(server));

        // PropertyHandlerMapping handlerMapping = new PropertyHandlerMapping();
        for (String key : handlers.keySet()) {
            //logger.info(key);
        }
        SpringHandlerMapping handlerMapping = new SpringHandlerMapping();
        handlerMapping.setRequestProcessorFactoryFactory(new SpringRequestProcessorFactoryFactory());
        handlerMapping.setHandlerMappings(handlers);

        server.setHandlerMapping(handlerMapping);
    }

    @RequestMapping(value = "sco/ussd")
    public void serve(HttpServletRequest request, HttpServletResponse response) throws XmlRpcException {
        try {
            server.execute(request, response);
        } catch (Exception e) {
            throw new XmlRpcException(e.getMessage(), e);
        }
    }

}
