package com.example.auth.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.Base64Utils;

import com.example.auth.SimpleGrantedAuthoritiesMixin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	/**
	 * 
	 * Authenticate credentials in the request
	 * SecurityContextHolder.getContext().setAuthentication(authentication)
	 *
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		UsernamePasswordAuthenticationToken authentication = null;
		Claims token = null;
		String header = request.getHeader("Authorization");
		boolean validToken;

		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		try {
			token = Jwts.parser()
					.setSigningKey(Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(
							"secure.password.for.login.and.authenticate.and.should.be.secure.enough")))
					.parseClaimsJws(header.replace("Bearer ", "")).getBody();
			validToken = true;
		} catch (JwtException | IllegalArgumentException ex) {
			ex.printStackTrace();
			validToken = false;
		}
		if (validToken) {
			String username = token.getSubject();
			Object roles = token.get("authorities");

			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
					new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
							.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
			authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	protected boolean requiresAuthentication(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return false;
		}
		return true;
	}
}