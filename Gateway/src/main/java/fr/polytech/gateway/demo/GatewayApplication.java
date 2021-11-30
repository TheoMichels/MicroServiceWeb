package fr.polytech.gateway.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(GatewayApplication.class, args);
	}

	@Value("${service.authentication}")
	private String auth_service_url;

	@Value("${service.profile}")
	private String profile_service_url;

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/profiles")
						.filters(f -> f.prefixPath("/PS"))
						.uri(profile_service_url))
				.route(p -> p
						.path("/profiles/{id}")
						.filters(f -> f.prefixPath("/PS"))
						.uri(profile_service_url))
				.route(p -> p
						.path("/profiles/{id}/name")
						.filters(f -> f.prefixPath("/PS"))
						.uri(profile_service_url))
				.route(p -> p
						.path("/profiles/{id}/email")
						.filters(f -> f.prefixPath("/PS"))
						.uri(profile_service_url))
				.route(p -> p
						.path("/users")
						.and().method(HttpMethod.PUT)
						.filters(f -> f.prefixPath("/AS"))
						.uri(auth_service_url))
				.route(p -> p
						.path("/users/{userId}")
						.and().method(HttpMethod.GET)
						.filters(f -> f.prefixPath("/AS"))
						.uri(auth_service_url))
				.route(p -> p
						.path("/users/{id}/token")
						.filters(f-> f.prefixPath("/AS"))
						.uri(auth_service_url))
				.route(p -> p
						.path("/token")
						.filters(f -> f.prefixPath("/AS"))
						.uri(auth_service_url))
				.build();
	}
}
