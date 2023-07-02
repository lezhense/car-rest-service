package ua.foxminded.lezhenin.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Value("${auth0.audience}")
	private String audience;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.httpBasic(AbstractHttpConfigurer::disable)
		.csrf(AbstractHttpConfigurer::disable)
		.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET, "/**").permitAll()
				.anyRequest().authenticated())
		.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
				.jwt(jwt -> jwt.decoder(jwtDecoder())));
		return http.build();
	}

	JwtDecoder jwtDecoder() {
		OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
		OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);
		NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
		jwtDecoder.setJwtValidator(validator);
		return jwtDecoder;
	}

	@Bean
	public UserDetailsService users() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("pass")).roles("USER").build();
		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("pass")).roles("USER", "ADMIN")
				.build();
		manager.createUser(user);
		manager.createUser(admin);
		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
