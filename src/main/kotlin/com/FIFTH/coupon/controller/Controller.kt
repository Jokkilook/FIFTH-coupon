package com.FIFTH.coupon.controller

import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.dto.UserDto
import com.FIFTH.coupon.service.CouponService
import com.FIFTH.coupon.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val userService: UserService, private val couponService: CouponService) {
    @PostMapping("/register")
    fun register(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<UserDto> {
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
    
    //유효시간 입력 받아 쿠폰 만들기
    @GetMapping("/createCoupon")
    fun createCoupon(
        @RequestParam userId:String,
        @RequestParam minuteTime:Long
    ):ResponseEntity<Coupon>{
        val coupon = couponService.createUserCoupon(userId)
        couponService.createSession(coupon,minuteTime)
        return  ResponseEntity.ok(coupon)
    }

    //쿠폰이 유효한 지 확인
    @GetMapping("/checkCoupon")
    fun checkCoupon(
        @RequestParam id: Long,
        @RequestParam userId: String,
        @RequestParam couponCode: String
    ):ResponseEntity<Boolean>{
        val coupon = Coupon(id=id,userId=userId,couponCode=couponCode)
        return ResponseEntity.ok(couponService.checkCoupon(coupon))
    }

    //유저의 쿠폰 리스트 반환
    @GetMapping("/getCoupons")
    fun getCoupons(
        @RequestParam userId: String
    ):ResponseEntity<Array<Coupon>?>{
        return ResponseEntity.ok(couponService.findCoupon(userId))
    }

    // 쿠폰 사용
    @GetMapping("/useCoupon")
    fun useCoupon(
        @RequestParam id: Long,
        @RequestParam userId: String,
        @RequestParam couponCode: String
    ):ResponseEntity<Boolean>{
        val coupon = Coupon(id=id,userId=userId,couponCode=couponCode)
        return ResponseEntity.ok(couponService.useCoupon(coupon))
    }
}