package com.runningtechy.graphview

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class ObservableHorizontalScrollView(context: Context, attributeSet: AttributeSet) :
    HorizontalScrollView(context, attributeSet) {

    /**
     * Interface definition for a callback to be invoked with the scroll
     * position changes.
     */
    interface OnScrollChangedListener {
        /**
         * Called when the scroll position of `view` changes.
         *
         * @param view The view whose scroll position changed.
         * @param horizontalScrollOrigin Current horizontal scroll origin.
         * @param verticalScrollOrigin Current vertical scroll origin.
         */
        fun onScrollChanged(
            view: ObservableHorizontalScrollView?,
            horizontalScrollOrigin: Int,
            verticalScrollOrigin: Int
        )
    }

    private lateinit var onScrollChangedListener: OnScrollChangedListener

    fun setOnScrollChangedListener(onScrollChangedListener: OnScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener
    }

    override fun onScrollChanged(
        currentHorizontalOrigin: Int,
        currentVerticalOrigin: Int,
        previousHorizontalOrigin: Int,
        previousVerticalOrigin: Int
    ) {
        super.onScrollChanged(
            currentHorizontalOrigin,
            currentVerticalOrigin,
            previousHorizontalOrigin,
            previousVerticalOrigin
        )
        onScrollChangedListener.onScrollChanged(this, currentHorizontalOrigin, currentVerticalOrigin)
    }
}