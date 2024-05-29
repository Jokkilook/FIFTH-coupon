package com.FIFTH.coupon.small
import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.dto.UserDto
import com.FIFTH.coupon.repository.CouponRepository
import com.FIFTH.coupon.repository.UserRepository
import com.FIFTH.coupon.service.CouponService
import com.FIFTH.coupon.service.UserService
import org.apache.catalina.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.mockito.Mockito
import java.util.concurrent.TimeUnit
import org.mockito.Mock
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.`when`

@SpringBootTest
class CouponSpec(
    @Autowired val userRepository:UserRepository,
    @Autowired val userService: UserService,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val couponService: CouponService,
    @Autowired val redisTemplate: StringRedisTemplate,
) {
    //여기에 테스트 코드 입력
    @Test
    fun `쿠폰번호중복체크`() {
        // Given
        val couponRepository = Mockito.mock(CouponRepository::class.java)
        val couponService = CouponService(couponRepository, redisTemplate)
        val couponCode = "ASDF8426"
        `when`(couponRepository.findCouponsByUserId(couponCode)).thenReturn(null)

        // When
        val coupon = couponService.findCoupon(couponCode)

        // Then
        assertNull(coupon)
    }
}