package com.example;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth.filter.JWTAuthenticationFilter;
import com.example.auth.filter.JWTAuthorizationFilter;
import com.example.auth.handler.LoginSuccessHandler;
import com.example.auth.service.JWTService;
import com.example.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private BCryptPasswordEncoder  passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private JpaUserDetailsService userService;
	
	/*
	 * .csrf().disable()
	 * do not utilize the default token 
	 * */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**","/js/**","/images/**","/list**", "/locale").permitAll()
		/*
		 * .antMatchers("/view/**").hasAnyRole("USER")
		 * .antMatchers("/uploads/**").hasAnyRole("USER")
		 * .antMatchers("/form/**").hasAnyRole("ADMIN")
		 * .antMatchers("/delete/**").hasAnyRole("ADMIN")
		 * .antMatchers("/invoice/**").hasAnyRole("ADMIN")
		 */
		.anyRequest().authenticated()
//		.and()
//		.formLogin().successHandler(successHandler)
//		.loginPage("/login")
//		.permitAll()
//		.and()
//		.logout().permitAll()
//		.and()
//		.exceptionHandling().accessDeniedPage("/error_403")
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService ))
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {

		build.userDetailsService(userService)
		.passwordEncoder(passwordEncoder);
		
		/*
		 * Authentication with datasource build.jdbcAuthentication()
		 * .dataSource(datasource) .passwordEncoder(passwordEncoder)
		 * .usersByUsernameQuery("select username, password, enabled from users where username =?"
		 * )
		 * .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id) where username = ?"
		 * );
		 */
		
		/*
		 * PasswordEncoder encoder =
		 * PasswordEncoderFactories.createDelegatingPasswordEncoder(); UserBuilder users
		 * = User.builder().passwordEncoder(encoder::encode);
		 * 
		 * build.inMemoryAuthentication()
		 * .withUser(users.username("admin").password("12345").roles("ADMIN" , "USER"))
		 * .withUser(users.username("jose").password("12345").roles("USER"));
		 */
        
	}

}
		