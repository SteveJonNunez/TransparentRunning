package com.runningtechy.graphview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class ScrollingValuePicker(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    private var leftSpacer: View
    private var rightSpacer: View
    private var scrollView: com.runningtechy.graphview.ObservableHorizontalScrollView =
        com.runningtechy.graphview.ObservableHorizontalScrollView(context, attributeSet)
    private var scrollPositionTextView: TextView = TextView(context)
    private var graphView: com.runningtechy.graphview.LineGraphView

    init {
        graphView = com.runningtechy.graphview.LineGraphView(context)

        scrollView.setOnScrollChangedListener(object: com.runningtechy.graphview.ObservableHorizontalScrollView.OnScrollChangedListener {
            override fun onScrollChanged(
                view: com.runningtechy.graphview.ObservableHorizontalScrollView?,
                horizontalScrollOrigin: Int,
                verticalScrollOrigin: Int
            ) {
                if(view != null) {
                    var scrollPosition = horizontalScrollOrigin
                    //TODO: set it to max/min when over max or under min
                    if(scrollPosition < 0)
                        scrollPosition = 0
                    else if(scrollPosition > graphView.width)
                        scrollPosition = graphView.width
                    scrollPositionTextView.text = "$scrollPosition"
                }
            }
        })
        scrollView.isHorizontalScrollBarEnabled = false

        val parentContainer = LinearLayout(context)
        parentContainer.orientation = LinearLayout.VERTICAL
        parentContainer.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(parentContainer)

        val container = LinearLayout(context)
        container.orientation = LinearLayout.HORIZONTAL
        parentContainer.addView(scrollView)
        scrollView.addView(container)

        graphView.layoutParams = LayoutParams(1000, 200)
        container.addView(graphView)

        leftSpacer = View(context)
        container.addView(leftSpacer, 0)

        rightSpacer = View(context)
        container.addView(rightSpacer)

        scrollPositionTextView.textSize = 40f
        scrollPositionTextView.setText("0")
        parentContainer.addView(scrollPositionTextView)
    }

    fun test(datapoints: List<com.runningtechy.graphview.DataPoint>) {
        graphView.setData(datapoints)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val width: Int = width

            val leftParams: ViewGroup.LayoutParams = leftSpacer.layoutParams
            leftParams.width = width/2
            leftParams.height = 1
            leftSpacer.layoutParams = leftParams

            val rightParams: ViewGroup.LayoutParams = rightSpacer.layoutParams
            rightParams.width = width/2
            rightParams.height = 1
            rightSpacer.layoutParams = rightParams
        }
    }
}