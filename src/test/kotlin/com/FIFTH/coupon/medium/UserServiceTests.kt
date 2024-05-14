package com.FIFTH.coupon.medium

import com.FIFTH.coupon.dto.UserDto
import com.FIFTH.coupon.entity.UserEntity
import com.FIFTH.coupon.repository.UserRepository
import com.FIFTH.coupon.service.UserService
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.random.Random

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
        randomUser = UserEntity(
            username = username,
            password = password
        )
        println("from setup() / randomUser의 id : ${randomUser.id} / username : ${randomUser.username} / password : ${randomUser.password} ")
    }

    @Test //001
    @DisplayName("중복된 사용자가 있는지 확인, 중복 - 회원가입 실패")
    fun `중복된 사용자가 있는지 확인, 중복 - 회원가입 실패`() {

        println("from Test 001 / randomUser의 id : ${randomUser.id} / username : ${randomUser.username} / password : ${randomUser.password} ")
        // Given
        given(userRepository.findByUsername(randomUser.username)).willReturn(randomUser)

        // When
        val exception = assertThrows(IllegalStateException::class.java) {
            userService.registerUser(randomUser.username, randomUser.password)
        }

        println("Test 001 / 중복된 사용자 확인")
        // Then
        assertEquals("Username already exists", exception.message)
    }

    @Test // 002
    @DisplayName("중복된 사용자가 있는지 확인하고 회원가입 성공")
    fun `중복된 사용자가 있는지 확인하고 회원가입 성공`() {
        // Given
        given(userRepository.findByUsername(randomUser.username)).willReturn(null)
        given(userRepository.save(randomUser)).willReturn(randomUser)

        // When
        val savedUser = userService.registerUser(randomUser.username, randomUser.password)

        println("Test 002 / savedUser username : ${savedUser.username} \n" +
                "Test 002 / randomUser id - username - password : ${randomUser.id} - ${randomUser.username} -${randomUser.password}")
        // Then
        assertNotNull(savedUser)
        assertEquals(randomUser.username, savedUser.username)
        //이 둘이 같은지 확인하는 과정
    }
}