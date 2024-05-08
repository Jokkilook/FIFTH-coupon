package com.FIFTH.coupon.service

import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.repository.CouponRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val redisTemplate: StringRedisTemplate
) {
    //여기에 API 에서 실행 될 함수 로직 구현

    val list = arrayOf(
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '0',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'N',
        'M',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'Z',
        'Y',
        'Z'
    )

    //쿠폰코드 생성
    fun createCouponCode(): String {
        var code: String = ""
        for (i in 1..8) {
            code += list.random()
        }
        return code
    }

    //유저아이디에 쿠폰코드 매칭해서 쿠폰데이터 생성
    fun createUserCoupon(userId: String): Coupon {
        val coupon = Coupon(userId = userId, couponCode = createCouponCode())
        return couponRepository.save(coupon)
    }

    //세션 아이디 생성 함수
    private fun generateSessionId(userId: String): String {
        return userId.hashCode().toString() + System.currentTimeMillis().toString()
    }

    //쿠폰 유효 세션 생성, 시간(분)값을 입력해서 유효시간 설정
    fun createSession(coupon: Coupon, minuteTime: Long): String {
        val sessionId = generateSessionId(coupon.userId)
        redisTemplate.opsForValue().set(coupon.userId + coupon.id, sessionId, minuteTime, TimeUnit.MINUTES)
        return sessionId
    }
    
    //유저 쿠폰 
    fun getUserCoupons(userId: String): Array<Coupon>? {
        return couponRepository.findCouponsByUserId(userId)
    }
    
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
