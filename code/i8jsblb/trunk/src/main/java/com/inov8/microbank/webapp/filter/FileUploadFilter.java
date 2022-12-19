package com.inov8.microbank.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter( filterName = "upload" )
public class FileUploadFilter implements Filter {
    @Override
    public void init( FilterConfig config ) throws ServletException {}

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
    throws IOException, ServletException {
        request
            .getRequestDispatcher( "/upload" )
            .include( request, response );

//        request
//            .getRequestDispatcher( "/hello.jsp" )
//            .include( request, response );
    }

    @Override
    public void destroy() {}
}