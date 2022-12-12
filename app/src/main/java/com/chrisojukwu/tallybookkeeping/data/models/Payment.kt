package com.chrisojukwu.tallybookkeeping.data.models

import java.math.BigDecimal
import java.time.LocalDateTime

data class Payment(
    var paymentAmount: BigDecimal = BigDecimal.ZERO,
    var date: LocalDateTime,
    var paymentMode: PaymentMode
)
