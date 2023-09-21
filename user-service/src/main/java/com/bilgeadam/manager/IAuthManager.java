package com.bilgeadam.manager;

import com.bilgeadam.dto.request.AuthUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.EndPoints.*;

@FeignClient(url = "http://localhost:7071/api/v1/auth",decode404 = true,name = "userprofile-auth")
public interface IAuthManager {


    @PutMapping(UPDATE)
    ResponseEntity<String> updateAuth(@RequestBody AuthUpdateRequestDto dto, @RequestHeader("Authorization")String token);

}
