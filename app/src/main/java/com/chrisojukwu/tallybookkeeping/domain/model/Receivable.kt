package com.chrisojukwu.tallybookkeeping.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Receivable(
    val recordId: String,
    val amount: BigDecimal,
    val customer: Customer,
    val date: OffsetDateTime
)
