package com.poc.currency.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.poc.currency.databinding.OtherCountryRatesItemBinding
import com.poc.currency.domain.response.currency.CurrencyEntity

class OtherCountryViewHolder(
    private val binding: OtherCountryRatesItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(currency: CurrencyEntity.Currency) {
        binding.countryCurrencyRate = currency
    }
}