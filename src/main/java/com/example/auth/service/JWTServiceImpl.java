package com.example.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.auth.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

	@Override
	public String create(Authentication auth) throws IOException {
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

		return Jwts.builder().setClaims(claims).setSubject(username)
				.signWith(
						Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(
								"secure.password.for.login.and.authenticate.and.should.be.secure.enough")),
						SignatureAlgorithm.HS256)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 14000000L)).compact();
	}

	@Override
	public boolean validate(String token) {
		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(Keys.hmacShaKeyFor(DatatypeConverter
						.parseBase64Binary("secure.password.for.login.and.authenticate.and.should.be.secure.enough")))
				.parseClaimsJws(resolve(token)).getBody();
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");
		return Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			return token.replace("Bearer ", "");
		}
		return null;
	}

}
