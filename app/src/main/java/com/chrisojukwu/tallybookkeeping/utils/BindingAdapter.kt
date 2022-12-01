package com.chrisojukwu.tallybookkeeping.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.EditIncomeProductListAdapter
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.IncomeExpenseAdapter

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

@BindingAdapter("productList")
fun bindEditIncomeRecyclerView(recyclerView: RecyclerView, data: MutableList<Product>?) {
    val adapter = recyclerView.adapter as EditIncomeProductListAdapter
    adapter.updateItems(data)
}

@BindingAdapter("recordsList")
fun bindRecordsRecyclerView(recyclerView: RecyclerView, list: MutableList<RecordHolder>?) {
    val adapter = recyclerView.adapter as IncomeExpenseAdapter
    //adapter.submitList(list)
}