package com.proyects.microservices.app.oauthmicroservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyects.microservices.app.userscommonsmicroservice.models.UserCommons;

@FeignClient(name = "users-microservice")
public interface UserFeingClient {
//	El path es el del microservicio users-microservice.
	@GetMapping("users/search/search-username")
	public UserCommons getUserByUserName(@RequestParam (value = "username") String username);

}
