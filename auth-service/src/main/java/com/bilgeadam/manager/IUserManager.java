package com.bilgeadam.manager;

import com.bilgeadam.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.EndPoints.*;

@FeignClient(url = "${feign.user}",decode404 = true,name = "auth-userprofile")
public interface IUserManager {

    @PostMapping(SAVE)
   ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto,@RequestHeader("Authorization") String token);

    @PostMapping(ACTIVATE_STATUS)
    ResponseEntity<String> activateStatus(@RequestParam String token);

    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<String> deleteById(@RequestHeader("Authorization") String token);

}
