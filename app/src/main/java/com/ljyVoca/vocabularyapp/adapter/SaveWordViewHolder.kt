package com.ljyVoca.vocabularyapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.ljyVoca.vocabularyapp.databinding.ItemSaveWordBinding
import com.ljyVoca.vocabularyapp.model.VocaWord

class SaveWordViewHolder(
    private val binding: ItemSaveWordBinding
): RecyclerView.ViewHolder(binding.root) {
    init {

    }

    fun bind(item: VocaWord) {
        binding.run {
            tvWord.text = item.word
            tvMean.text= item.mean
        }
    }
}