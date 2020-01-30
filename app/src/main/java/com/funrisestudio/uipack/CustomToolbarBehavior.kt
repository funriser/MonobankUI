package com.funrisestudio.uipack

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CustomToolbarBehavior@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): CoordinatorLayout.Behavior<MotionLayout>(context, attrs) {

    var collapseOffset: ((Float) -> Unit)? = null

    private var initialDependentViewHeight = -1

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: MotionLayout,
        dependency: View
    ): Boolean {
        val lp = dependency.layoutParams as CoordinatorLayout.LayoutParams
        if (lp.behavior is BottomSheetBehavior<*>) {
            return true
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: MotionLayout,
        dependency: View
    ): Boolean {
        if (initialDependentViewHeight == -1) {
            initialDependentViewHeight = dependency.top
        }
        val endHeight = child.minimumHeight

        collapseOffset?.invoke(1 - (dependency.top.toFloat() / (initialDependentViewHeight - endHeight)))
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = dependency.top
        child.layoutParams = lp
        return true
    }

}