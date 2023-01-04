package com.chrisojukwu.tallybookkeeping.data

import com.chrisojukwu.tallybookkeeping.data.dto.*
import com.chrisojukwu.tallybookkeeping.domain.model.Payment
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import com.chrisojukwu.tallybookkeeping.utils.toCustomer
import com.chrisojukwu.tallybookkeeping.utils.toNetworkCustomer
import com.chrisojukwu.tallybookkeeping.utils.toNetworkSupplier
import com.chrisojukwu.tallybookkeeping.utils.toSupplier

fun List<NetworkIncome>.asDMModel(): List<RecordHolder.Income> {
    return map { it ->
        RecordHolder.Income(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountReceived = it.amountReceived,
            discount = it.discount,
            subTotal = it.subTotal,
            balanceDue = it.balanceDue,
            description = it.description,
            productList = it.productList?.map { networkProduct ->
                Product(
                    id = networkProduct.id,
                    productName = networkProduct.productName,
                    productPrice = networkProduct.productPrice,
                    productQuantity = networkProduct.productQuantity,
                    productTotalPrice = networkProduct.productTotalPrice
                )
            }?.toMutableList(),
            customer = it.customer?.toCustomer(),
            paymentList = it.paymentList.map { networkPayment ->
                Payment(
                    id = networkPayment.id,
                    paymentAmount = networkPayment.paymentAmount,
                    paymentDate = networkPayment.paymentDate,
                    paymentMode = networkPayment.paymentMode
                )
            }.toMutableList()
        )
    }
}


fun RecordHolder.Income.asNetworkModel(): NetworkIncome {
    return NetworkIncome(
        recordId = this.recordId,
        date = this.date,
        totalAmount = this.totalAmount,
        amountReceived = this.amountReceived,
        discount = this.discount,
        subTotal = this.subTotal,
        balanceDue = this.balanceDue,
        description = this.description,
        productList = this.productList?.map { product ->
            NetworkProduct(
                id = product.id,
                productName = product.productName,
                productPrice = product.productPrice,
                productQuantity = product.productQuantity,
                productTotalPrice = product.productTotalPrice
            )
        },
        customer = this.customer?.toNetworkCustomer(),
        paymentList = this.paymentList.map { payment ->
            NetworkPayment(
                id = payment.id,
                paymentAmount = payment.paymentAmount,
                paymentDate = payment.paymentDate,
                paymentMode = payment.paymentMode
            )
        }
    )
}

fun RecordHolder.Income.asDBModel(): DBIncome {
    return DBIncome(
        recordId = this.recordId,
        date = this.date,
        totalAmount = this.totalAmount,
        amountReceived = this.amountReceived,
        discount = this.discount,
        subTotal = this.subTotal,
        balanceDue = this.balanceDue,
        description = this.description,
        productList = this.productList,
        customer = this.customer,
        paymentList = this.paymentList
    )
}

fun List<NetworkExpense>.toDMModel(): List<RecordHolder.Expense> {
    return map {
        RecordHolder.Expense(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountPaid = it.amountPaid,
            balanceDue = it.balanceDue,
            description = it.description,
            category = it.category,
            productList = it.productList?.map { networkProduct ->
                Product(
                    id = networkProduct.id,
                    productName = networkProduct.productName,
                    productPrice = networkProduct.productPrice,
                    productQuantity = networkProduct.productQuantity,
                    productTotalPrice = networkProduct.productTotalPrice
                )
            }?.toMutableList(),
            supplier = it.supplier?.toSupplier(),
            paymentList = it.paymentList.map { networkPayment ->
                Payment(
                    id = networkPayment.id,
                    paymentAmount = networkPayment.paymentAmount,
                    paymentDate = networkPayment.paymentDate,
                    paymentMode = networkPayment.paymentMode
                )
            }.toMutableList()
        )
    }
}

fun RecordHolder.Expense.asDBModel(): DBExpense {
    return DBExpense(
        recordId = this.recordId,
        date = this.date,
        totalAmount = this.totalAmount,
        amountPaid = this.amountPaid,
        balanceDue = this.balanceDue,
        description = this.description,
        category = this.category,
        productList = this.productList,
        supplier = this.supplier,
        paymentList = this.paymentList
    )
}

fun RecordHolder.Expense.asNetworkModel(): NetworkExpense {
    return NetworkExpense(
        recordId = this.recordId,
        date = this.date,
        totalAmount = this.totalAmount,
        amountPaid = this.amountPaid,
        balanceDue = this.balanceDue,
        description = this.description,
        category = this.category,
        productList = this.productList?.map { product ->
            NetworkProduct(
                id = product.id,
                productName = product.productName,
                productPrice = product.productPrice,
                productQuantity = product.productQuantity,
                productTotalPrice = product.productTotalPrice
            )
        },
        supplier = this.supplier?.toNetworkSupplier(),
        paymentList = this.paymentList.map { payment ->
            NetworkPayment(
                id = payment.id,
                paymentAmount = payment.paymentAmount,
                paymentDate = payment.paymentDate,
                paymentMode = payment.paymentMode
            )
        }
    )
}

fun List<DBIncome>.asDomainModel(): List<RecordHolder.Income> {
    return map {
        RecordHolder.Income(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountReceived = it.amountReceived,
            discount = it.discount,
            subTotal = it.subTotal,
            balanceDue = it.balanceDue,
            description = it.description,
            productList = it.productList,
            customer = it.customer,
            paymentList = it.paymentList
        )
    }
}

fun List<DBExpense>.toDomainModel(): List<RecordHolder.Expense> {
    return map {
        RecordHolder.Expense(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountPaid = it.amountPaid,
            balanceDue = it.balanceDue,
            description = it.description,
            category = it.category,
            productList = it.productList,
            supplier = it.supplier,
            paymentList = it.paymentList
        )
    }
}


fun List<NetworkIncome>.asDBModel(): List<DBIncome> {
    return map {
        DBIncome(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountReceived = it.amountReceived,
            discount = it.discount,
            subTotal = it.subTotal,
            balanceDue = it.balanceDue,
            description = it.description,
            productList = it.productList?.map { networkProduct ->
                Product(
                    id = networkProduct.id,
                    productName = networkProduct.productName,
                    productPrice = networkProduct.productPrice,
                    productQuantity = networkProduct.productQuantity,
                    productTotalPrice = networkProduct.productTotalPrice
                )
            }?.toMutableList(),
            customer = it.customer?.toCustomer(),
            paymentList = it.paymentList.map { networkPayment ->
                Payment(
                    id = networkPayment.id,
                    paymentAmount = networkPayment.paymentAmount,
                    paymentDate = networkPayment.paymentDate,
                    paymentMode = networkPayment.paymentMode
                )
            }.toMutableList()
        )
    }
}

fun List<NetworkExpense>.toDBModel(): List<DBExpense> {
    return map {
        DBExpense(
            recordId = it.recordId,
            date = it.date,
            totalAmount = it.totalAmount,
            amountPaid = it.amountPaid,
            balanceDue = it.balanceDue,
            description = it.description,
            category = it.category,
            productList = it.productList?.map { networkProduct ->
                Product(
                    id = networkProduct.id,
                    productName = networkProduct.productName,
                    productPrice = networkProduct.productPrice,
                    productQuantity = networkProduct.productQuantity,
                    productTotalPrice = networkProduct.productTotalPrice
                )
            }?.toMutableList(),
            supplier = it.supplier?.toSupplier(),
            paymentList = it.paymentList.map { networkPayment ->
                Payment(
                    id = networkPayment.id,
                    paymentAmount = networkPayment.paymentAmount,
                    paymentDate = networkPayment.paymentDate,
                    paymentMode = networkPayment.paymentMode
                )
            }.toMutableList()
        )
    }
}

fun NetworkStockItem.toDomainModel(): StockItem {
    return StockItem(
      stockName = this.stockName,
        sku = this.sku,
        costPrice = this.costPrice,
        sellingPrice = this.sellingPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}

fun NetworkStockItem.toDBModel(): DBInventory {
    return DBInventory(
        stockName = this.stockName,
        sku = this.sku,
        costPrice = this.costPrice,
        sellingPrice = this.sellingPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}

fun StockItem.toDBModel(): DBInventory {
    return DBInventory(
        stockName = this.stockName,
        sku = this.sku,
        costPrice = this.costPrice,
        sellingPrice = this.sellingPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}

fun StockItem.toNetworkModel(): NetworkStockItem {
    return NetworkStockItem(
        stockName = this.stockName,
        sku = this.sku,
        costPrice = this.costPrice,
        sellingPrice = this.sellingPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}

fun DBInventory.toDomain(): StockItem {
    return StockItem(
        stockName = this.stockName,
        sku = this.sku,
        costPrice = this.costPrice,
        sellingPrice = this.sellingPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}


fun List<NetworkStockItem>.toDomain(): List<StockItem> {
    return map {
        StockItem(
            stockName = it.stockName,
            sku = it.sku,
            costPrice = it.costPrice,
            sellingPrice = it.sellingPrice,
            quantity = it.quantity,
            imageUrl = it.imageUrl
        )
    }
}

fun List<NetworkStockItem>.toDB(): List<DBInventory> {
    return map {
        DBInventory(
            stockName = it.stockName,
            sku = it.sku,
            costPrice = it.costPrice,
            sellingPrice = it.sellingPrice,
            quantity = it.quantity,
            imageUrl = it.imageUrl
        )
    }
}

fun List<DBInventory>.asDomain(): List<StockItem> {
    return map {
        StockItem(
            stockName = it.stockName,
            sku = it.sku,
            costPrice = it.costPrice,
            sellingPrice = it.sellingPrice,
            quantity = it.quantity,
            imageUrl = it.imageUrl
        )
    }
}

