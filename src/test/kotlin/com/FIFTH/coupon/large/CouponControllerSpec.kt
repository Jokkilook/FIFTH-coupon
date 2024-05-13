package com.FIFTH.coupon.large
import com.FIFTH.coupon.controller.Controller
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
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(Controller::class)
class CouponControllerSpec@Autowired constructor(
    private val mockMvc: MockMvc,
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

    //쿠폰사용API
    @Test
    @DisplayName("쿠폰사용API")
    fun `쿠폰사용API`() {
        val user = User(id = 1, username = "test", password = "test")
        userRepo.save(user)
        val coupon = couponService.createUserCoupon(user.username)
        couponRepo.save(coupon)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/useCoupon")
                .param("id", coupon.id.toString())
                .param("userId", coupon.userId)
                .param("couponCode",coupon.couponCode)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.username))
    }


}