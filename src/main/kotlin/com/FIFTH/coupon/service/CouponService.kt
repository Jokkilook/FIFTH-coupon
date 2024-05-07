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

    // 쿠폰 사용 함수
    fun useCoupon(userId: String, couponCode: String): Boolean {
        val coupon = couponRepository.findByUserIdAndCouponCode(userId, couponCode)
            ?: return false // 해당하는 쿠폰이 없는 경우

        val sessionId = redisTemplate.opsForValue().get(userId + coupon.id)
        if (sessionId == null) {
            // 세션이 만료되거나 존재하지 않는 경우
            return false
        } else {
            // 이미 사용된 쿠폰인지 확인
            if (coupon.used) {
                return false
            }

            // 쿠폰 사용 처리 및 상태 업데이트
            coupon.used = true
            couponRepository.save(coupon)

            // Redis에서 세션 삭제
            redisTemplate.delete(userId + coupon.id)

            return true
        }
    }
}