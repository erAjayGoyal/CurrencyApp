package com.poc.currency.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.poc.currency.databinding.RatesHisotryItemBinding
import com.poc.currency.ui.model.RateItem

class CurrencyHistoryViewHolder(
    private val binding: RatesHisotryItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(rateItem: RateItem) {
        binding.rates = rateItem
    }
}