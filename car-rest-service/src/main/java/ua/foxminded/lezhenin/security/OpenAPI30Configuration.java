package ua.foxminded.lezhenin.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Cars API", version = "1.0", 
	license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), 
	description = "The Cars API is used to get, create, update, and delete cars."), 
	servers = { @Server(url = "http://localhost:8080", description = "Development") })
public class OpenAPI30Configuration {
	@Bean
	public OpenAPI customizeOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.description("Provide the JWT token").bearerFormat("JWT")));

	}
}
