package com.bilgeadam.controller;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.UserProfileFindAllResponseDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.service.UserService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.bilgeadam.constant.EndPoints.*;
/*
    findbystatus metodu yazıp cacheleme yapalım

 */
@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;


    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto,@RequestHeader("Authorization") String token){
        System.out.println("Authdan gelen token=>"+token);
        return  ResponseEntity.ok(userService.createNewUser(dto));
    }
    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<String> activateStatus(@RequestParam String token){
        return  ResponseEntity.ok(userService.activateStatus(token));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userService.updateUserProfile(dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserProfileFindAllResponseDto>> findAll(){
        return ResponseEntity.ok(userService.findAllUserProfile());
    }

    @GetMapping("/find_by_username/{username}")
    public ResponseEntity<UserProfile> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }
    @GetMapping("/find_by_status/{status}")
    public ResponseEntity<List<UserProfile>> findByStatus(@PathVariable EStatus status){
        return ResponseEntity.ok(userService.findByStatus(status));
    }

    @GetMapping("/find_by_status2/{status}")
    public ResponseEntity<List<UserProfile>> findByStatus(@PathVariable String status){
        return ResponseEntity.ok(userService.findByStatus(status));
    }
    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<String> deleteById(@RequestHeader("Authorization") String token){

      return   ResponseEntity.ok(userService.deleteUserProfile(token));
    }

    @GetMapping("/find_all_by_pageable")
    public ResponseEntity<Page<UserProfile>> findAllByPageable(int pageSize, int pageNumber,@RequestParam(required = false,defaultValue = "ASC") String direction,@RequestParam(required = false,defaultValue = "id") String sortParameter){

        return ResponseEntity.ok(userService.findAllByPageable(pageSize,pageNumber,direction,sortParameter));
    }

    @GetMapping("/find_all_by_slice")
    public ResponseEntity<Slice<UserProfile>> findAllBySlice(int pageSize, int pageNumber, @RequestParam(required = false,defaultValue = "ASC") String direction, @RequestParam(required = false,defaultValue = "id") String sortParameter){

        return ResponseEntity.ok(userService.findAllBySLice(pageSize,pageNumber,direction,sortParameter));
    }

    @GetMapping("/find-user-simple-data/{authId}")
    public ResponseEntity<UserProfileResponseDto> findByUserSimpleDataWithAuthId(@PathVariable Long authId){
        return ResponseEntity.ok(userService.findByUserSimpleDataWithAuthId(authId));
    }
}
