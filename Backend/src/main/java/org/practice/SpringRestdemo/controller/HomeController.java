package org.practice.SpringRestdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// This controller handles the home endpoint
// It returns a simple greeting message
// It is accessible without authentication
public class HomeController {

    @GetMapping("/api/v1")
    public String demo() {
        return "Hello World";
    }

}
