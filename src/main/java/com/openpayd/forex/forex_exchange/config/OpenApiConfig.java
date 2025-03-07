package com.openpayd.forex.forex_exchange.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Forex Exchange API",
        description = "API for currency exchange rates and conversions",
        version = "1.0"
    ),
    servers = {
        @Server(url = "/", description = "Default Server URL")
    }
)
public class OpenApiConfig {
} 