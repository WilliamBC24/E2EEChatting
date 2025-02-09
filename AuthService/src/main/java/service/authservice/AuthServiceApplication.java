package service.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(AuthServiceApplication.class, args);
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("-----------------------------");
        System.out.println("-----Application Started-----");
        System.out.println("External Host: " + hostAddress);
        System.out.println("-----------------------------");
    }
}
