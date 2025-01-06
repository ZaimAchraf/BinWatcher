package com.binwatcher.test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${defaultX}")
    private Integer defaultX;
    @Value("${defaultY}")
    private Integer defaultY;

    @Value("${paramX}")
    private Integer paramX;
    @Value("${paramY}")
    private Integer paramY;

    @GetMapping("/testArchitecture")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>(
                "defaultXY : (" + defaultX + ", " + defaultY + ")\n paramXY : (" + paramX + ", " + paramY + ")",
                HttpStatus.OK);
    }
}
