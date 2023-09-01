package com.bilgeadam.controller;
import com.bilgeadam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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




}
