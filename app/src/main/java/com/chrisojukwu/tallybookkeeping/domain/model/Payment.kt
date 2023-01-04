package com.chrisojukwu.tallybookkeeping.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime


data class Payment(
    val id: String,

    var paymentAmount: BigDecimal = BigDecimal.ZERO,

    var paymentDate: OffsetDateTime,

    var paymentMode: PaymentMode

)



