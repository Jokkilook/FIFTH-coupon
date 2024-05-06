package com.FIFTH.coupon.service

import com.FIFTH.coupon.repository.CouponRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val redisTemplate: StringRedisTemplate
) {
    //여기에 API 에서 실행 될 함수 로직 구현
}