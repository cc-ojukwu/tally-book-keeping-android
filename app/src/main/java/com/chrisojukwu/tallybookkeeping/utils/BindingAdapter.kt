package com.chrisojukwu.tallybookkeeping.utils

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.EditExpenseProductListAdapter
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.EditIncomeProductListAdapter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("imageLoadingStatus")
fun showHideProgressBar(view: ProgressBar, isLoading: Boolean?) {
    if (isLoading == true) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }


}

@BindingAdapter("buttonLoadingStatus")
fun showHideButtonText(view: Button, isLoading: Boolean?) {
    if (isLoading == true) {
        view.isEnabled = false
        view.text = ""
    } else {
        view.isEnabled = true
        view.setText(R.string.sign_in)
    }
}

@BindingAdapter("incomeProductList")
fun bindEditIncomeRecyclerView(recyclerView: RecyclerView, data: MutableList<Product>?) {
    val adapter = recyclerView.adapter as EditIncomeProductListAdapter
    adapter.updateItems(data)
}

@BindingAdapter("expenseProductList")
fun bindEditExpenseRecyclerView(recyclerView: RecyclerView, data: MutableList<Product>?) {
    val adapter = recyclerView.adapter as EditExpenseProductListAdapter
    adapter.updateItems(data)
}

@BindingAdapter("formatBigDecimal")
fun formatBigDecimal(textView: TextView, bigDecimal: BigDecimal?) {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.isGroupingUsed = true
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = RoundingMode.DOWN
    if (bigDecimal != null) {
        textView.text = numberFormat.format(bigDecimal)
    }
}

@BindingAdapter("formatPercentage")
fun formatDoubleToPercent(textView: TextView, double: Double?) {
    val decFormat = DecimalFormat("0.0")
    textView.text = "(${decFormat.format(double)}%)"
}

@BindingAdapter("formatDate")
fun formatDate(textView: TextView, date: OffsetDateTime?) {
    when (date?.toLocalDate()) {
        LocalDate.now() -> textView.text = "Today"
        LocalDate.now().minusDays(1) -> textView.text = "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d MMM, yy")
            textView.text = date?.format(formatter)
        }
    }
}

@BindingAdapter("formatDateText")
fun formatDateText(textView: TextView, date: OffsetDateTime?) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
    textView.text = date?.format(formatter)
}

@BindingAdapter("formatDateAndTime")
fun formatDateAndTime(textView: TextView, date: OffsetDateTime?) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy - H:m")
    textView.text = date?.format(formatter)
}