package com.inov8.microbank.webapp.listener;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

import com.inov8.microbank.servlets.FileUploadServlet;

@WebListener
public class FileUploadListener implements ServletContextListener {

    @Override
    public void contextInitialized( ServletContextEvent event ) {
        ServletContext context = event.getServletContext();
        Dynamic upload = context.addServlet( "upload", FileUploadServlet.class );
        upload.addMapping( "/upload" );
        upload.setMultipartConfig( getMultiPartConfig() );
    }
    @Override
    public void contextDestroyed( ServletContextEvent event ) {}

    private MultipartConfigElement getMultiPartConfig() {
        String location = "";
        long maxFileSize = -1;
        long maxRequestSize = -1;
        int fileSizeThreshold = 0;
        return new MultipartConfigElement(
            location,
            maxFileSize,
            maxRequestSize,
            fileSizeThreshold
        );
    }
}