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
    @MockBean
    private val couponRepo = couponRepository
    
    //쿠폰 발급 대상 유저 유효성 테스트
    @Test
    fun checkTargetUserValidation(){
        //Given
        val user = User(id = 0, username = "test", password = "test",)
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

    fun `쿠폰사용후번호삭제`(){
        val userId = "testUser"
        val couponId = 1L
        val couponCode = "WXYZ9876"
        val coupon = Coupon(id = couponId, userId = userId, couponCode = couponCode)
        couponRepo.save(coupon)

        assertNotNull(couponRepo.findCouponById(1L))

        couponService.useCoupon(coupon)
        assertNull(couponRepo.findCouponById(1L))
    }

    
    fun `쿠폰데이터정상저장여부`(){
        //여기에 API 에서 실행 될 함수 로직 구현
        val list = arrayOf('1','2','3','4','5','6','7','8','9','0','A','B','C','D','E','F','G','H','I','J','K','L','N','M','O','P','Q','R','S','T','U','V','W','Z','Y','Z')

        //쿠폰코드 생성
        fun createCouponCode():String{
            var code:String = ""
            for( i in 1..8){
                code += list.random()
            }
            return code
        }
        var username ="meow"
        var coupontest=createCouponCode()
//        assertNull(couponRepo)
        //유저아이디에 쿠폰코드 매칭해서 쿠폰데이터 생성
        val meow = Coupon(userId = username, couponCode = coupontest)

        fun createUserCoupon(userId:String):Coupon{
            val coupon = Coupon(id = 1, userId=userId, couponCode = createCouponCode())

            return  couponRepo.save(coupon)
        }

//        assertEquals(meow,createUserCoupon("meow") )
        createUserCoupon(username)
        assertNotNull(couponRepo.findCouponsByUserId(username))
    }
}