package org.practice.SpringRestdemo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
// This controller handles the home endpoint
// It returns a simple greeting message
// It is accessible without authentication
// The CORS configuration allows requests from the specified origin
// and sets a maximum age for the preflight request cache
public class HomeController {

    @GetMapping("/api/v1")
    public String demo() {
        return "Hello World";
    }

}
