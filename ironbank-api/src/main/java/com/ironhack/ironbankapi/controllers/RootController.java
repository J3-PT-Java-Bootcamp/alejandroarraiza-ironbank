package com.ironhack.ironbankapi.controllers;

import com.ironhack.ironbankapi.dto.HealthDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping("/health")
    HealthDto getHealth() {
        return new HealthDto("Healthy");
    }

}
