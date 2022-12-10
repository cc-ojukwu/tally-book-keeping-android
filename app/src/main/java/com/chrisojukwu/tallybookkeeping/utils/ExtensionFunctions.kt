package com.chrisojukwu.tallybookkeeping.utils

import android.view.View
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun View.setupMaxHeight(maxHeight: Double = 0.8) {
    val displayMetrics = context?.resources?.displayMetrics
    val height = displayMetrics?.heightPixels
    val maximalHeight = (height?.times(maxHeight))?.toInt()
    val layoutParams = this.layoutParams
    maximalHeight?.let {
        layoutParams.height = it
    }
    this.layoutParams = layoutParams
}