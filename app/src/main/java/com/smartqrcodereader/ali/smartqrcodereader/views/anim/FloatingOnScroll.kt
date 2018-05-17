package com.smartqrcodereader.ali.smartqrcodereader.views.anim

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.animation.LinearInterpolator

class FloatingOnScroll : FloatingActionButton.Behavior {

    constructor() : super()

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0) {
            val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val fabBottomMargin = layoutParams.bottomMargin
            child.animate()?.translationY((child.height + fabBottomMargin).toFloat())?.setInterpolator(LinearInterpolator())?.start()
        } else if (dyConsumed < 0) {
            child.animate()?.translationY(0f)?.setInterpolator(LinearInterpolator())?.start()
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}