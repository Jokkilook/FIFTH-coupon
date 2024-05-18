package com.FIFTH.coupon.large

import com.FIFTH.coupon.dto.Coupon
import com.FIFTH.coupon.repository.CouponRepository
import com.FIFTH.coupon.service.CouponService
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class LargeCouponGenerationTest {

    @Autowired
    private lateinit var couponService: CouponService

    @MockBean
    private lateinit var couponRepository: CouponRepository

    @Test
    fun `쿠폰 대량 발급 테스트`() {
        val numberOfCoupons = 1000
        val numberOfThreads = 100
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        runBlocking {
            repeat(numberOfThreads) { threadIndex ->
                executor.submit {
                    repeat(numberOfCoupons / numberOfThreads) {
                        val username = "user$threadIndex"
                        couponService.createUserCoupon(username)
                    }
                    latch.countDown()
                }
            }
        }
        latch.await()
    }
}
