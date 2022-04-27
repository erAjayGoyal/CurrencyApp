package com.poc.currency.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.poc.currency.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyActivity : AppCompatActivity() {

    private var appProgressView: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appProgressView = findViewById(R.id.progressBarView)
    }

    fun showLoading() {
        appProgressView?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        appProgressView?.visibility = View.GONE
    }

}