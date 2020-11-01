package com.proyects.microservices.app.oauthmicroservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyects.microservices.app.oauthmicroservice.clients.UserFeingClient;
import com.proyects.microservices.app.userscommonsmicroservice.models.UserCommons;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserFeingClient client;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Logger log = LoggerFactory.getLogger(UserService.class);
		
		UserCommons user = client.getUserByUserName(username);
		
		if(user == null) {
			log.error("User: " + user + " is not found");
			throw new UsernameNotFoundException("User: " + user + " is not found");
		}
		
//		Hay que pasar el objeto Roles al objeto de spring SimpleGrantedAuthority.
		List <GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
				.peek(authority -> log.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("User authenticated: " + username);
		
		return new User(user.getUserName(), user.getPassword(), user.isEnabled(), true, 
				true, true, authorities);
	}

}
