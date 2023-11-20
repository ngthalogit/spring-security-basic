package com.example.springsecurity;

import com.example.springsecurity.model.SpringUser;
import com.example.springsecurity.repository.SpringUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {
    private final SpringUserRepository springUserRepository;

    public SpringSecurityApplication(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);

    }

    @Override
    public void run(String... args) {
        SpringUser springUser = new SpringUser();
        springUser.setId("1123");
        springUser.setUsername("ntl0601");
        springUser.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("long"));
        springUserRepository.save(springUser);
        System.out.println(springUser);
    }

}
