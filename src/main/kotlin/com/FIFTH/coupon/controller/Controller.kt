package com.FIFTH.coupon.controller

import com.FIFTH.coupon.dto.User
import com.FIFTH.coupon.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val userService: UserService) {
    @PostMapping("/register")
    fun register(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.registerUser(username, password))
    }


    @GetMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<String> {
        return if (userService.validateUser(username, password)) {
            ResponseEntity.ok(userService.createSession(username))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    @GetMapping("/check")
    fun check(
        @RequestParam username: String,
        @RequestParam sessionId: String
    ): ResponseEntity<String> {
        return if (userService.checkSession(username, sessionId)) {
            ResponseEntity.ok("Session valid")
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    //이 밑에 쿠폰 관련 API 구현
    
}