package com.chrisojukwu.tallybookkeeping.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Payable(
    val recordId: String,
    val amount: BigDecimal,
    val supplier: Supplier,
    val date: OffsetDateTime
)
