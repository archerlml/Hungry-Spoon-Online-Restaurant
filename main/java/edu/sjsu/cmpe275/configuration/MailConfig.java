package edu.sjsu.cmpe275.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

    /*
    @Value("${email.host}")
    private String host;
    @Value("${email.port}")
    private Integer port;
    @Value("${email.username}")
    private String username;
    @Value("${email.password}")
    private String password;
*/
    //TODO move to configuration file
    private String host = "smtp.gmail.com";
    private Integer port = 587;
    private String username = "cmpe275.onlineorder@gmail.com";
    private String password = "cmpe275sjsu";
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.smtp.EnableSSL.enable","true");
        properties.setProperty("mail.debug", "false");
        return properties;
    }

}