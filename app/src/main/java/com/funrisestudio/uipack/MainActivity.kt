package com.funrisestudio.uipack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_payment.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        setUpRecycler()
        setUpBottomSheet()
    }

    private fun setUpRecycler() {
        adapter = PaymentAdapter()
        rvPayments.layoutManager = LinearLayoutManager(this)
        rvPayments.adapter = adapter
        populatePayments()
    }

    private fun setUpBottomSheet() {
        ExtendedBottomSheetBehavior.from(cardPayments).apply {
            addBottomSheetCallback(cardSlideCallback)
            setNoHalfStateExpandedOffset(100)
        }
    }

    private val cardSlideCallback = object : ExtendedBottomSheetBehavior.BottomSheetCallback() {
        val interpolator = DecelerateInterpolator()
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            val fraction = interpolator.getInterpolation(slideOffset)
            containerPanelActions.alpha = 1 - fraction
            containerPanelActions.translationY = 300 * fraction
            layoutContent.progress = slideOffset
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
