package com.funrisestudio.uipack

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import kotlin.random.Random

data class PaymentItem(
    val title: String,
    val description: String,
    val amount: Spannable,
    @DrawableRes val img: Int
) {
    companion object {
        fun randomList(context: Context, size: Int = 100): List<PaymentItem> {
            val items = mutableListOf<PaymentItem>()
            for (i in 0..size) {
                val randomCompany = Company.allCompanies.random()
                items.add(
                    PaymentItem(
                        title = randomCompany.title,
                        description = randomCompany.category,
                        img = randomCompany.img,
                        amount = randomAmount(context)
                    )
                )
            }
            return items
        }
    }
}

data class Company(
    val title: String,
    val category: String,
    val img: Int
) {
    companion object {
        val allCompanies = listOf(
            Company(
                title = "McDonald's",
                category = "Cafe and restaurants",
                img = R.drawable.ic_mac
            ),
            Company(
                title = "Netflix",
                category = "Streaming",
                img = R.drawable.ic_netflix
            ),
            Company(
                title = "Gap",
                category = "Clothes and shoes",
                img = R.drawable.ic_gap
            ),
            Company(
                title = "Uber",
                category = "Taxi",
                img = R.drawable.ic_uber
            ),
            Company(
                title = "PiedPiper",
                category = "Investments",
                img = R.drawable.ic_pied_piper
            )
        )
    }
}


fun randomAmount(context: Context): Spannable {
    val dFormatter = DecimalFormat("#,##0.00")
    val randomDouble = Random.Default.nextDouble(-1000.0, 0.0)
    val amountStr = dFormatter.format(randomDouble)
    return SpannableString(amountStr).apply {
        setSpan(RelativeSizeSpan(0.8f), length - 3, length, 0)
        val grey = ContextCompat.getColor(context, R.color.colorGreyStrong)
        setSpan(ForegroundColorSpan(grey), length - 3, length, 0)
    }
}