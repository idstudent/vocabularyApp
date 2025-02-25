package com.ljyVoca.vocabularyapp.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ljyVoca.vocabularyapp.adapter.SaveWordListAdapter
import com.ljyVoca.vocabularyapp.databinding.ActivityWordListBinding
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordListActivity: BaseActivity<ActivityWordListBinding>() {
    private val vocabularyViewModel: VocabularyViewModel by viewModels()
    private lateinit var saveWordListAdapter: SaveWordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveWordListAdapter = SaveWordListAdapter()

        vocabularyViewModel.getAllWord()
        binding.rvWord.layoutManager = LinearLayoutManager(this@WordListActivity)
        binding.rvWord.adapter = saveWordListAdapter

        lifecycleScope.launch {
            vocabularyViewModel.saveWordList.collect  {
                Log.e("ljy", "이거 $it")
                saveWordListAdapter.submitList(it)
            }
        }
    }

    override fun getViewBinding(): ActivityWordListBinding {
        return ActivityWordListBinding.inflate(layoutInflater)
    }
}