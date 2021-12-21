package com.example;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.inject.Singleton;
import org.slf4j.bridge.SLF4JBridgeHandler;


@OpenAPIDefinition(
	info = @Info(
		title = "liquibaseaot",
		version = "0.1"
	)
)
@Singleton
public class Application
{

	public static void main(String[] args)
	{
		// Bridge JUL to Slf4j
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Micronaut.build(args)
			.mainClass(Application.class)
			.start();
	}

}
