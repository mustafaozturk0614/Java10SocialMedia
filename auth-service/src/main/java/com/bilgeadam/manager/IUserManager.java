package com.bilgeadam.manager;

import com.bilgeadam.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.bilgeadam.constant.EndPoints.ACTIVATE_STATUS;
import static com.bilgeadam.constant.EndPoints.SAVE;

@FeignClient(url = "http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")
public interface IUserManager {

    @PostMapping(SAVE)
   ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto);

    @PostMapping(ACTIVATE_STATUS)
    ResponseEntity<String> activateStatus(@RequestParam String token);


}
