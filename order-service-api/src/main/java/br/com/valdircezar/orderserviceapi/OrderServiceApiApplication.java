package br.com.valdircezar.orderserviceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrderServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApiApplication.class, args);
    }

}
