package io.github.lucasferreira.correios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CorreiosApplication {
	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		SpringApplication.run(CorreiosApplication.class, args);
	}

	public static void close(int code){
		SpringApplication.exit(ctx,()-> code);
	}
}
