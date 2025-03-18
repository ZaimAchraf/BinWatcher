package com.binwatcher.driverservice.client;

import com.binwatcher.driverservice.model.UserInfos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="security-service")
public interface UserClient {
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody UserInfos registerParams);
}
