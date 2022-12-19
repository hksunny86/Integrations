package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

//import com.inov8.integration.channel.tutuka.util.CustomLoggingUtils;
import org.apache.xmlrpc.server.XmlRpcServerConfig;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class CustomXmlRpcServletServer extends XmlRpcServletServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setConfig(XmlRpcServerConfig pConfig) {
        super.setConfig(pConfig);
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            super.execute(new LoggingRequestWrapper(request), new LoggingResponseWrapper(response));
        } else {
            super.execute(request, response);
        }
    }

    private class LoggingRequestWrapper extends HttpServletRequestWrapper {

        private HttpServletRequest originalRequest;
        private LoggingServletInputStream loggingInputStream = null;

        public LoggingRequestWrapper(HttpServletRequest request) {
            super(request);
            this.originalRequest = request;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            loggingInputStream = new LoggingServletInputStream(
                    originalRequest.getInputStream());
            return loggingInputStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    private class LoggingServletInputStream extends ServletInputStream {

        private ServletInputStream istream;
        private ByteArrayInputStream standinInputStream;

        public LoggingServletInputStream(ServletInputStream istream) throws IOException {
            this.istream = istream;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n = 0;
            while (true) {
                n = istream.read(buf);
                if (n == -1) {
                    break;
                }
                bos.write(buf, 0, n);
            }
            this.standinInputStream = new ByteArrayInputStream(bos.toByteArray());
//            CustomLoggingUtils.logRequest(logger, new String(bos.toByteArray()));
        }

        @Override
        public int read() throws IOException {
            int c = standinInputStream.read();
            return c;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }

    private class LoggingResponseWrapper extends HttpServletResponseWrapper {

        private HttpServletResponse originalResponse;
        private LoggingServletOutputStream loggingOutputStream = null;

        public LoggingResponseWrapper(HttpServletResponse response) {
            super(response);
            this.originalResponse = response;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            this.loggingOutputStream = new LoggingServletOutputStream(originalResponse.getOutputStream());
            return loggingOutputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new OutputStreamWriter(getOutputStream()));
        }
    }

    private class LoggingServletOutputStream extends ServletOutputStream {

        private ServletOutputStream ostream;
        private StringBuilder buf;

        public LoggingServletOutputStream(ServletOutputStream ostream) {
            this.ostream = ostream;
            this.buf = new StringBuilder();
        }

        @Override
        public void write(int b) throws IOException {
            buf.append((char) b);
            ostream.write(b);
        }

        @Override
        public void close() throws IOException {
            if (buf.length() > 0) {
//                CustomLoggingUtils.logResponse(logger, buf.toString());
            }
            ostream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}
