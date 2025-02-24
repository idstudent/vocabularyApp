package com.ljyVoca.vocabularyapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ljyVoca.vocabularyapp.databinding.ActivityCheckBinding
import com.ljyVoca.vocabularyapp.util.setOnSingleClickListener
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckActivity: BaseActivity<ActivityCheckBinding>() {
    private val vocabularyViewModel: VocabularyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        lifecycleScope.launch {
            vocabularyViewModel.currentWord.collect {
                it?.let {
                   binding.tvWord.text = it.mean
                }
            }
        }
    }

    private fun initListener() {
        binding.btnNext.setOnSingleClickListener {
            vocabularyViewModel.nextGetWord()
        }
    }

    override fun getViewBinding(): ActivityCheckBinding {
        return ActivityCheckBinding.inflate(layoutInflater)
    }
}