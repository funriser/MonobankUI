@file:Suppress("SameParameterValue")

package com.funrisestudio.uipack

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.*
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_payment.view.*

class CollapsibleContentActivity : AppCompatActivity() {

    private lateinit var adapter: PaymentAdapter

    private var insetTop = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNoStatusBar()
        initView()
    }

    private fun setNoStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        ViewCompat.setOnApplyWindowInsetsListener(layoutContent) { v, insets ->
            insetTop = insets.systemWindowInsetTop
            v.setPadding(0, insetTop, 0, 0)
            insets
        }
    }

    private fun initView() {
        val balanceStr = "5 500.55 $"
        tvBalance.text = getBalanceTitle(balanceStr)
        tvBalanceDetails.text = getString(R.string.cc_balance_details, balanceStr)
        setUpRecycler()
        setUpBottomSheet()
    }

    private fun getBalanceTitle(rawBalance: String): Spannable {
        return SpannableString(rawBalance).apply {
            setSpan(RelativeSizeSpan(0.7f), length - 5, length, 0)
        }
    }

    private fun setUpRecycler() {
        adapter = PaymentAdapter()
        rvPayments.layoutManager = LinearLayoutManager(this)
        rvPayments.adapter = adapter
        rvPayments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val headerElevatedZ = resources.getDimension(R.dimen.cc_history_header_elevated_z)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val offset = recyclerView.computeVerticalScrollOffset()
                if (offset <= 100) {
                    layoutHistoryHeader.translationZ = headerElevatedZ * offset/100
                } else if (offset > 100 && layoutHistoryHeader.translationZ != headerElevatedZ) {
                    layoutHistoryHeader.translationZ = headerElevatedZ
                }
            }
        })
        populatePayments()
    }

    private fun setUpBottomSheet() {
        ExtendedBottomSheetBehavior.from(cardPayments).apply {
            addBottomSheetCallback(cardSlideCallback)
            tvBalance.doOnLayout {
                setNoHalfStateExpandedOffset(insetTop + it.height + it.marginTop * 2)
            }
        }
    }

    private val cardSlideCallback = object : ExtendedBottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
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
