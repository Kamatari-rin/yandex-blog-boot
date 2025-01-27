package org.example;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class PostServiceApp {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        tomcat.addWebapp("", System.getProperty("java.io.tmpdir"));

        tomcat.start();
        tomcat.getServer().await();
    }
}