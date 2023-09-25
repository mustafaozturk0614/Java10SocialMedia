package com.bilgeadam.manager;

import com.bilgeadam.dto.response.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:7072/api/v1/user", name = "post-user", decode404 = true)
public interface IUserManager {
    @GetMapping("/find-user-simple-data/{authId}")
    ResponseEntity<UserProfileResponseDto> findByUserSimpleDataWithAuthId(@PathVariable Long authId);
}
