package com.FIFTH.coupon.service

import com.FIFTH.coupon.dto.UserDto
import com.FIFTH.coupon.entity.UserEntity
import com.FIFTH.coupon.repository.UserRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisTemplate: StringRedisTemplate // Redis 템플릿 추가
) {
    fun registerUser(username: String, password: String): UserDto {
        userRepository.findByUsername(username)?.let {
            throw IllegalStateException("Username already exists")
        }
        val user = UserEntity(username = username, password = password) // 비밀번호는 암호화 처리 필요
        return userRepository.save(user).let {
            UserDto(username = it.username)
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
        return user?.password == password
    }

    fun createSession(username: String): String {
        val sessionId = generateSessionId(username)
        redisTemplate.opsForValue().set("session:$username", sessionId, 30, TimeUnit.MINUTES) // 세션 유효 시간 30분
        return sessionId
    }

    fun checkSession(username: String, sessionId: String): Boolean {
        val storedSessionId = redisTemplate.opsForValue().get("session:$username")
        return sessionId == storedSessionId
    }

    private fun generateSessionId(username: String): String {
        return username.hashCode().toString() + System.currentTimeMillis().toString()
    }
}
