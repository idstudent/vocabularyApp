package com.ljyVoca.vocabularyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljyVoca.vocabularyapp.databinding.ItemSaveWordBinding
import com.ljyVoca.vocabularyapp.model.VocaWord

class SaveWordListAdapter: ListAdapter<VocaWord, SaveWordViewHolder>(
    object: DiffUtil.ItemCallback<VocaWord>() {
        override fun areItemsTheSame(oldItem: VocaWord, newItem: VocaWord): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VocaWord, newItem: VocaWord): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveWordViewHolder {
        val binding = ItemSaveWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SaveWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveWordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}