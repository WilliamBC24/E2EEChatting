package service.authservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(AuthServiceApplication.class, args);
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("-----------------------------");
        log.info("-----Application Started-----");
        log.info("External Host: {}", hostAddress);
        log.info("-----------------------------");
    }
}
