package com.redpath.client;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class FTPClientRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:application.properties");

        // lets shutdown faster in case of in-flight messages stack up
        getContext().getShutdownStrategy().setTimeout(10);

        System.out.println ("trace: " + "{{directory.incoming}}");
        from("file:{{directory.incoming}}?moveFailed={{directory.error}}")
            .log("Uploading file ${file:name}")
            .to("{{ftp.client}}")
            .log("Uploaded file ${file:name} complete.");

        // use system out so it stand out
        System.out.println("*********************************************************************************");
        System.out.println("Camel will route files from target/upload directory to the FTP server: "
                + getContext().resolvePropertyPlaceholders("{{ftp.server}}"));
        System.out.println("You can configure the location of the ftp server in the src/main/resources/ftp.properties file.");
        System.out.println("If the file upload fails, then the file is moved to the target/error directory.");
        System.out.println("Use ctrl + c to stop this application.");
        System.out.println("*********************************************************************************");    }

}