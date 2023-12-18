package com.joseleonardo.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.joseleonardo.lojavirtual.model")
@ComponentScan(basePackages = { "com.joseleonardo.*" })
@EnableJpaRepositories(basePackages = { "com.joseleonardo.lojavirtual.repository" })
@EnableTransactionManagement
public class LojaVirtualApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaVirtualApiApplication.class, args);
	}

}
