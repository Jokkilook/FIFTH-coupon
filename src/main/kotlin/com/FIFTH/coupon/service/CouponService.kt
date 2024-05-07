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

    fun getUserCoupons(userId: String): Array<Coupon>? {
        return couponRepository.findCouponsByUserId(userId)
    }

    // 쿠폰 유효성 체크
    fun checkCoupon(coupon: Coupon):Boolean{
        // redis에 유효한 세션이 있는지 체크
        return !redisTemplate.opsForValue().get(coupon.userId+coupon.id).isNullOrEmpty()
    }

    // 쿠폰 사용
    fun useCoupon(coupon: Coupon):Boolean{
        // 유효성이 확인되면 쿠폰 데이터 삭제 후 true 리턴
        if(checkCoupon(coupon)){
            couponRepository.delete(coupon)
            return true }

        // 만료된 데이터가 남아있는 쿠폰인 경우
        // 삭제 시도 후 false 반환
        couponRepository.delete(coupon)
        return false
    }
}
