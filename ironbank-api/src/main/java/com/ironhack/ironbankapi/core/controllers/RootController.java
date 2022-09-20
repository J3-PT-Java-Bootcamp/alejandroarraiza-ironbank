package com.ironhack.ironbankapi.core.controllers;

import com.ironhack.ironbankapi.core.dto.HealthDto;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping("/health")
    HealthDto getHealth() {
        return new HealthDto("Healthy");
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String test(Principal principal) {
        return principal.getName();
    }

}
