package dev.kei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(InventoryServiceApp.class, args);
    }
}
