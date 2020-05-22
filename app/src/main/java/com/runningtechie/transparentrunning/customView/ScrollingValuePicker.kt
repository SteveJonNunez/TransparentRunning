package com.runningtechie.transparentrunning.customView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.runningtechie.transparentrunning.R

class ScrollingValuePicker(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    private var leftSpacer: View
    private var rightSpacer: View
    private var scrollView: ObservableHorizontalScrollView = ObservableHorizontalScrollView(context, attributeSet)

    init {
        scrollView.setOnScrollChangedListener(object: ObservableHorizontalScrollView.OnScrollChangedListener {
            override fun onScrollChanged(
                view: ObservableHorizontalScrollView?,
                horizontalScrollOrigin: Int,
                verticalScrollOrigin: Int
            ) {
                //TODO:
            }
        })
        scrollView.isHorizontalScrollBarEnabled = false
        addView(scrollView)

        val container = LinearLayout(context)
        container.orientation = LinearLayout.HORIZONTAL
        scrollView.addView(container)

        val sliderBg = ImageView(context)
        sliderBg.setImageResource(R.drawable.scroll_img)
        sliderBg.adjustViewBounds = true
        container.addView(sliderBg)

        leftSpacer = View(context)
        container.addView(leftSpacer, 0)

        rightSpacer = View(context)
        container.addView(rightSpacer)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val width: Int = width

            val leftParams: ViewGroup.LayoutParams = leftSpacer.layoutParams
            leftParams.width = width/2
            leftSpacer.layoutParams = leftParams

            val rightParams: ViewGroup.LayoutParams = rightSpacer.layoutParams
            rightParams.width = width/2
            rightSpacer.layoutParams = rightParams
        }
    }
}