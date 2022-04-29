package com.poc.currency.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poc.currency.databinding.OtherCountryRatesItemBinding
import com.poc.currency.domain.response.currency.CurrencyEntity

/**
 * Recycler view adapter class used to show favourite country conversation rate
 * based on from currency value
 * @param currencyList -> currency conversation rate list for favourite country
 */
class OtherCountryCurrencyAdapter(
    private val currencyList: List<CurrencyEntity.Currency>
) : RecyclerView.Adapter<OtherCountryViewHolder>() {

    private lateinit var binding: OtherCountryRatesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherCountryViewHolder {
        binding =
            OtherCountryRatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtherCountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OtherCountryViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int = currencyList.size

}
