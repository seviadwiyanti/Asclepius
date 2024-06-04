package com.dicoding.asclepius.view.Adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.RiwayatEntity
import com.dicoding.asclepius.databinding.ItemRiwayatBinding
import com.dicoding.asclepius.view.ResultActivity

class RiwayatAdapter : ListAdapter<RiwayatEntity, RiwayatAdapter.RiwayatViewHolder>(DIFF_CALLBACK) {
    class RiwayatViewHolder(private val binding: ItemRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(riwayat: RiwayatEntity) {
            binding.apply {
                val result = riwayat.score

                category.text = "$result"
                tvTanggal.text = riwayat.createdAt
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiwayatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RiwayatEntity>() {
            override fun areItemsTheSame(
                oldItem: RiwayatEntity,
                newItem: RiwayatEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RiwayatEntity,
                newItem: RiwayatEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}