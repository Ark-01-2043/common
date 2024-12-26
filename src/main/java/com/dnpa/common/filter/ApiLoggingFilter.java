package com.dnpa.common.filter;

import com.dnpa.common.entity.dto.Request;
import com.dnpa.common.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.util.TeeOutputStream;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
@Component
@Slf4j
public class ApiLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> requestMap = this.getRequestMap(request);
        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(request);
        BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(response);

        Request requestInput = new Request();
        requestInput.setMethod(request.getMethod());
        requestInput.setUrl(getFullURL(request));
        requestInput.setParameters(requestMap);
        requestInput.setBody(bufferedRequest.getRequestBody());

        filterChain.doFilter(bufferedRequest, bufferedResponse);

        CustomUserDetails customUserDetails = null;
        if (!requestInput.getUrl().contains("actuator/health")) {
            customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        }
        requestInput.setUserDetails(customUserDetails);

        Gson gson = new GsonBuilder().create();
        log.info("request=" + gson.toJson(requestInput)
                .replace("\"{", "{")
                .replace("}\"", "}")
                .replace("\\\"", "\"")
                .replace("\\u003d", "=")
                .replace("\\u0026", "&")
        );
        log.info("response=" + gson.toJson(bufferedResponse.getContent())
                .replace("\"{", "{")
                .replace("}\"", "}")
                .replace("\\\"", "\"")
                .replace("\\u003d", "=")
                .replace("\\u0026", "&")
        );
    }

    private Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue;
            if (StringUtils.containsIgnoreCase(requestParamName, "password")) {
                requestParamValue = "********";
            } else {
                requestParamValue = request.getParameter(requestParamName);
            }
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }

        return typesafeRequestMap;
    }



    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] buffer;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);

            if(req.getContentType()!=null
                    && (req.getContentType().toLowerCase().indexOf("multipart/form-data")<=-1)) {
                // Read InputStream and store its content in a buffer.
                InputStream is = req.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int read;
                while ((read = is.read(buf)) > 0) {
                    baos.write(buf, 0, read);
                }
                this.buffer = baos.toByteArray();
            }
            else
            {
                this.buffer = new byte[1024];
            }
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.buffer);
            return new BufferedServletInputStream(bais);
        }

        String getRequestBody() throws IOException {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(this.getInputStream()));
            String line;
            StringBuilder inputBuffer = new StringBuilder();
            do {
                line = reader.readLine();
                if (null != line) {
                    inputBuffer.append(line.trim());
                }
            } while (line != null);
            reader.close();
            return inputBuffer.toString().trim();
        }

    }
    public static class CustomServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public CustomServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // Do nothing
        }
    }
    private static final class BufferedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // Do nothing
        }
    }
    public static class BufferedResponseWrapper implements HttpServletResponse {

        HttpServletResponse original;
        CustomServletOutputStream tee;
        ByteArrayOutputStream bos;

        public BufferedResponseWrapper(HttpServletResponse response) {
            original = response;
        }

        public String getContent() {
            return bos != null ? bos.toString() : "";
        }

        public PrintWriter getWriter() throws IOException {
            return original.getWriter();
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if (tee == null) {
                bos = new ByteArrayOutputStream();
                tee = new CustomServletOutputStream(original.getOutputStream(), bos);
            }
            return tee;

        }

        @Override
        public String getCharacterEncoding() {
            return original.getCharacterEncoding();
        }

        @Override
        public String getContentType() {
            return original.getContentType();
        }

        @Override
        public void setCharacterEncoding(String charset) {
            original.setCharacterEncoding(charset);
        }

        @Override
        public void setContentLength(int len) {
            original.setContentLength(len);
        }

        @Override
        public void setContentLengthLong(long l) {
            original.setContentLengthLong(l);
        }

        @Override
        public void setContentType(String type) {
            original.setContentType(type);
        }

        @Override
        public void setBufferSize(int size) {
            original.setBufferSize(size);
        }

        @Override
        public int getBufferSize() {
            return original.getBufferSize();
        }

        @Override
        public void flushBuffer() throws IOException {
            tee.flush();
        }

        @Override
        public void resetBuffer() {
            original.resetBuffer();
        }

        @Override
        public boolean isCommitted() {
            return original.isCommitted();
        }

        @Override
        public void reset() {
            original.reset();
        }

        @Override
        public void setLocale(Locale loc) {
            original.setLocale(loc);
        }

        @Override
        public Locale getLocale() {
            return original.getLocale();
        }

        @Override
        public void addCookie(Cookie cookie) {
            original.addCookie(cookie);
        }

        @Override
        public boolean containsHeader(String name) {
            return original.containsHeader(name);
        }

        @Override
        public String encodeURL(String url) {
            return original.encodeURL(url);
        }

        @Override
        public String encodeRedirectURL(String url) {
            return original.encodeRedirectURL(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeUrl(String url) {
            return original.encodeUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeRedirectUrl(String url) {
            return original.encodeRedirectUrl(url);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            original.sendError(sc, msg);
        }

        @Override
        public void sendError(int sc) throws IOException {
            original.sendError(sc);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            original.sendRedirect(location);
        }

        @Override
        public void setDateHeader(String name, long date) {
            original.setDateHeader(name, date);
        }

        @Override
        public void addDateHeader(String name, long date) {
            original.addDateHeader(name, date);
        }

        @Override
        public void setHeader(String name, String value) {
            original.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            original.addHeader(name, value);
        }

        @Override
        public void setIntHeader(String name, int value) {
            original.setIntHeader(name, value);
        }

        @Override
        public void addIntHeader(String name, int value) {
            original.addIntHeader(name, value);
        }

        @Override
        public void setStatus(int sc) {
            original.setStatus(sc);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void setStatus(int sc, String sm) {
            original.setStatus(sc, sm);
        }

        @Override
        public String getHeader(String arg0) {
            return original.getHeader(arg0);
        }

        @Override
        public Collection<String> getHeaderNames() {
            return original.getHeaderNames();
        }

        @Override
        public Collection<String> getHeaders(String arg0) {
            return original.getHeaders(arg0);
        }

        @Override
        public int getStatus() {
            return original.getStatus();
        }
    }
    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getServletPath());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
