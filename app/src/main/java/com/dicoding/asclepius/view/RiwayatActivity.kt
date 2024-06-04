package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.entity.RiwayatEntity
import com.dicoding.asclepius.databinding.ActivityRiwayatBinding
import com.dicoding.asclepius.view.Adapter.RiwayatAdapter
import com.dicoding.asclepius.view.viewmodel.RiwayatViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory

class RiwayatActivity : AppCompatActivity() {

    private lateinit var riwayatViewModel: RiwayatViewModel
    private lateinit var binding: ActivityRiwayatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        riwayatViewModel = obtainViewModel(this@RiwayatActivity)

        riwayatViewModel.getRiwayat().observe(this) {
            showRiwayat(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager

    }


    private fun showRiwayat(data: List<RiwayatEntity>?) {
        if (data?.isEmpty() == true) {
            binding.noHistory.visibility = View.VISIBLE
        } else {
            val adapter = RiwayatAdapter()
            adapter.submitList(data)
            binding.rvHistory.adapter = adapter
        }
    }

    private fun obtainViewModel(riwayatActivity: RiwayatActivity): RiwayatViewModel {
        val factory = ViewModelFactory.getInstance(riwayatActivity.application)
        return ViewModelProvider(riwayatActivity, factory)[RiwayatViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
