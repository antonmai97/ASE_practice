package com.tumex.servletapp;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletApplication {

    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        // define port of tomcat (default for tomcat is 8080)
        tomcat.setPort(8082);

        // Each application is mapped to a context (i.e., each context represents a webapplication).
        // With the addContext() method, we create an application that does not support JSP files and has no web.xml file.

        String contextPath = ""; // use default root context
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        // create http servlet which returns HTML code for `Hello World`
        HttpServlet httpServlet = new HttpServlet() {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("<html><head><title>Hello World</title></head><body>");
        printWriter.println("<h1>Hello World</h1>");
        printWriter.println("</body></html>");
        }
    };

    // define name and url pattern (in this case all urls) for servlet
    String servletName = "HelloWorldServlet";
    String urlPattern = "/*";

    // add servlet to embedded tomcat with context
    tomcat.addServlet(contextPath, servletName, httpServlet);
    context.addServletMappingDecoded(urlPattern, servletName);

    // start tomcat and wait for requests
    tomcat.start();
    tomcat.getServer().await();
    }
}