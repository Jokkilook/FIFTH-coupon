package com.FIFTH.coupon.small
import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.dto.User
import com.FIFTH.coupon.repository.CouponRepository
import com.FIFTH.coupon.repository.UserRepository
import com.FIFTH.coupon.service.CouponService
import com.FIFTH.coupon.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.mockito.Mockito
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
}