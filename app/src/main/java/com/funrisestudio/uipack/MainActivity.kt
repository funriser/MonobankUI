package com.funrisestudio.uipack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.FrameMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_payment.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var collapsibleBehavior: CustomToolbarBehavior

    private val adapter = PaymentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPayments.layoutManager = LinearLayoutManager(this)
        rvPayments.adapter = adapter
        populatePayments()

        bottomSheetBehavior = BottomSheetBehavior.from(cardPayments)
        bottomSheetBehavior.addBottomSheetCallback(cardSlideCallback)
        collapsibleBehavior = (layoutToolbar.layoutParams as CoordinatorLayout.LayoutParams).behavior as CustomToolbarBehavior
        collapsibleBehavior.collapseOffset = {
            Log.d("CollapseOffset", "$it")
            layoutToolbar.progress = it
        }
    }

    private val cardSlideCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        val interpolator = DecelerateInterpolator()
        var prevOffset = -1f
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            val fraction = interpolator.getInterpolation(slideOffset)
            containerPanelActions.alpha = 1 - fraction
            containerPanelActions.translationY = 300 * fraction
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_DRAGGING) {
                if (prevOffset < slideOffset) {
                    if (bottomSheetBehavior.isFitToContents) {
                        bottomSheetBehavior.isFitToContents = false
                        bottomSheetBehavior.setExpandedOffset(100)
                    }
                } else {
                    if (!bottomSheetBehavior.isFitToContents) {
                        bottomSheetBehavior.isFitToContents = true
                        bottomSheetBehavior.setExpandedOffset(0)
                    }
                }
            }
            prevOffset = slideOffset
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
        }
    }

    private fun populatePayments() {
        val paymentItems = mutableListOf<String>()
        for (i in 0..200) {
            paymentItems.add(i, "Item $i")
        }
        adapter.paymentItems = paymentItems
    }

    class PaymentAdapter: RecyclerView.Adapter<PaymentViewHolder>() {

        var paymentItems: List<String> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
            return PaymentViewHolder(view)
        }

        override fun getItemCount(): Int {
            return paymentItems.size
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
            holder.itemView.tvPayment.text = paymentItems[position]
        }
    }

    class PaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}
