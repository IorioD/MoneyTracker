package com.ssd.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration  {
	private UserDetailsService userDetailsService;

	public SpringSecurityConfiguration(UserDetailsService userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
 		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		return daoAuthenticationProvider;
	}

	@Bean
	protected SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers(
			"/home",
			"/lista_spese",
			"/formaddspesa",
			"/saveSpesa",
			"/formRichiestaModifica",
			"/saveRichiestaModifica")
			.authenticated()
		.antMatchers(
			"/accettaModifica",
			"/adminhome",
			"/cancellaModifica",
			"/cancellaSpesa",
			"/formaddutente",
			"/formUpdateUtente",
			"/lista_modifiche",
			"/lista_utenti",
			"/lista_spese_admin",
			"/saveUtente",
			"/updateUtente")
			.hasAuthority("ADMIN")
		.and()
		.formLogin()
		.loginPage("/login").permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/access_denied")
		.and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/logout_success").permitAll();

		return http.build();
	}
}