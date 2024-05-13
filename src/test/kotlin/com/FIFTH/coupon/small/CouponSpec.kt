package com.FIFTH.coupon.small
import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.dto.User
import com.FIFTH.coupon.repository.CouponRepository
import com.FIFTH.coupon.repository.UserRepository
import com.FIFTH.coupon.service.CouponService
import com.FIFTH.coupon.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.mockito.Mockito
import java.util.concurrent.TimeUnit
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class CouponSpec(
    @Autowired val userRepository:UserRepository,
    @Autowired val userService: UserService,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val couponService: CouponService,
    @Autowired val redisTemplate: StringRedisTemplate,
) {
    //여기에 테스트 코드 입력
    @MockBean
    private val userRepo = userRepository
    private val couponRepo = couponRepository
    
    //쿠폰 발급 대상 유저 유효성 테스트
    @Test
    fun checkTargetUserValidation(){
        //Given
        val user = User(id = 0, username = "test", password = "test")
        userRepo.save(user)

        //When
        couponService.createUserCoupon(user.username)


        //Then
        assertTrue(userRepo.findByUsername(user.username)!=null)
    }


    // 기한이 만료된 쿠폰 체크
    private val couponRepositorys = Mockito.mock(CouponRepository::class.java)
    private val redisTemplates = Mockito.mock(StringRedisTemplate::class.java)
    private val couponServices = CouponService(couponRepositorys, redisTemplates)

    @Test
    fun `만료된 쿠폰은 false를 반환해야 함`() {
        // Given
        val userId = "testUser"
        val couponId = 1L
        val couponCode = "ABCD1234"
        val coupon = Coupon(id = couponId, userId = userId, couponCode = couponCode)

        // Mock Redis to return null (expired session)
        Mockito.`when`(redisTemplates.opsForValue().get(Mockito.anyString())).thenReturn(null)

        // When
        val result = couponServices.checkCoupon(coupon)

        // Then
        assertFalse(result)
    }

    @Test
    fun `유효한 쿠폰은 true를 반환해야 함`() {
        // Given
        val userId = "testUser"
        val couponId = 1L
        val couponCode = "EFGH5678"
        val coupon = Coupon(id = couponId, userId = userId, couponCode = couponCode)

        // Mock Redis to return a valid session ID
        val sessionId = "validSessionId"
        Mockito.`when`(redisTemplates.opsForValue().get(Mockito.anyString())).thenReturn(sessionId)

        // When
        val result = couponServices.checkCoupon(coupon)

        // Then
        assertTrue(result)
    }

    @Test
    fun `createSession은 올바른 유효시간을 가진 세션을 생성해야 함`() {
        // Given
        val userId = "testUser"
        val couponId = 1L
        val couponCode = "WXYZ9876"
        val coupon = Coupon(id = couponId, userId = userId, couponCode = couponCode)
        val minuteTime = 30L // 30 minutes

        // When
        couponServices.createSession(coupon, minuteTime)

        // Then
        Mockito.verify(redisTemplates, Mockito.times(1))
            .opsForValue().set(Mockito.eq("$userId$couponId"), Mockito.anyString(), Mockito.eq(minuteTime), Mockito.eq(TimeUnit.MINUTES))
    }

    @Test
    fun `쿠폰생성`(){
        //given
        val meowcoupon = couponService.createUserCoupon("meow")
        val foundCoupon = couponService.findCoupon("meow")
        assertNotNull(foundCoupon)
    }
}