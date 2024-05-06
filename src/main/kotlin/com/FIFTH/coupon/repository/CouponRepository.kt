package com.FIFTH.coupon.repository

import com.FIFTH.coupon.dto.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository :JpaRepository<Coupon, String>{
    fun findCouponById(id:Long):Coupon?

    fun findCouponsByUserId(userId:String):Array<Coupon>?
}