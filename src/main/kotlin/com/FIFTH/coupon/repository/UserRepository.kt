package com.FIFTH.coupon.repository

import com.FIFTH.coupon.dto.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    fun findByUsernameAndPassword(username: String,password:String): User?
}