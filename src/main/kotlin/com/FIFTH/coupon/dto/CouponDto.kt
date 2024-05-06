package com.FIFTH.coupon.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id:String,
    var userId:String,
    var couponValue:String,
)
