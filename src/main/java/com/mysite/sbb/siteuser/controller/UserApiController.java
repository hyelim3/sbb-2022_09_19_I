package com.mysite.sbb.siteuser.controller;

import com.mysite.sbb.siteuser.domain.SiteUser;
import com.mysite.sbb.siteuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public SiteUser create(@RequestBody SiteUser user) {
        return userService.create(user.getUsername(), user.getEmail(), user.getPassword());
    }
}
