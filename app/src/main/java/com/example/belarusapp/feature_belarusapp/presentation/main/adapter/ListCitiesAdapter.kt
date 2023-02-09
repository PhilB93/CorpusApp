package com.example.belarusapp.feature_belarusapp.presentation.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.belarusapp.R
import com.example.belarusapp.databinding.LayoutSearchItemBinding
import com.example.belarusapp.feature_belarusapp.domain.model.CityItem


private const val ROUNDED_CORNERS = 16

class ListCitiesAdapter(private val listener: CityOnClickListener) :
    ListAdapter<CityItem, ListCitiesAdapter.MainViewHolder>(ItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            LayoutSearchItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false,
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            item?.let { holder.bindGame(it) }
        }
    }

    inner class MainViewHolder(
        val binding: LayoutSearchItemBinding,
        private val listener: CityOnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindGame(item: CityItem?) = with(binding) {
            tvName.text = item?.name
            Glide.with(ivGameImage).load(item?.logo).apply(
                RequestOptions()
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .transform(RoundedCorners(ROUNDED_CORNERS))
            ).into(ivGameImage)
            initButtonsListeners(item)
        }

        private fun initButtonsListeners(game: CityItem?) {
            binding.searchGamesItem.setOnClickListener {
                game?.let { listener.onClick(it) }
            }
        }
    }

    class ItemDiffUtil : DiffUtil.ItemCallback<CityItem>() {
        override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return oldItem == newItem
        }
    }
}