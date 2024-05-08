package com.FIFTH.coupon.medium

import com.FIFTH.coupon.dto.User
import com.FIFTH.coupon.entity.UserEntity
import com.FIFTH.coupon.repository.UserRepository
import com.FIFTH.coupon.service.UserService
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class UserServiceTests {
    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    private val faker = Faker()

    private lateinit var randomUser: UserEntity

    @BeforeEach
    fun setup() {
        val username = faker.internet().username()
        val password = faker.internet().password()
        randomUser = User(
            username = username,
            password = password
        )
    }

    @Test
    @DisplayName("중복된 사용자가 있는지 확인, 중복 - 회원가입 실패")
    fun `중복된 사용자가 있는지 확인, 중복 - 회원가입 실패`() {
        // Given
        BDDMockito.given(userRepository.findByUsername(randomUser.username)).willReturn(randomUser)

        // When
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            userService.registerUser(randomUser.username, randomUser.password)
        }

        // Then
        Assertions.assertEquals("Username already exists", exception.message)
    }

    @Test
    @DisplayName("중복된 사용자가 있느지 확인하고 회원가입 성공")
    fun `중복된 사용자가 있느지 확인하고 회원가입 성공`() {
        // Given
        BDDMockito.given(userRepository.findByUsername(randomUser.username)).willReturn(null)
        BDDMockito.given(userRepository.save(randomUser)).willReturn(randomUser)

        // When
        val savedUser = userService.registerUser(randomUser.username, randomUser.password)

        // Then
        Assertions.assertNotNull(savedUser)
        assertEquals(randomUser.username, savedUser.username)
        //이 둘이 같은지 확인하는 과정
    }
}