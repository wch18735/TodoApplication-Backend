package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
@SuppressWarnings(value = { "deprecation" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
			.csrf().disable()		// csrf는 현재 사용하지 않기 때문에 disable
			.httpBasic().disable()	// token을 사용하기 때문에 basic 인증 disable
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests().antMatchers("/", "/auth/**").permitAll()
			.anyRequest().authenticated();	// antMatchers() 등록된 경로 외에는 모두 인증 필요
		
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
	}
}
