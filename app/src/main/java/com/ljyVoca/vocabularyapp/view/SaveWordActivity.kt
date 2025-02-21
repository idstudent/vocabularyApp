package com.ljyVoca.vocabularyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ljyVoca.vocabularyapp.BaseActivity
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.SaveWordViewModel
import com.ljyVoca.vocabularyapp.databinding.ActivitySaveWordBinding
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveWordActivity: BaseActivity<ActivitySaveWordBinding>() {
    private val saveWordViewModel: SaveWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
            btnSave.setOnSingleClickListener {
                saveWordViewModel.insertVoca(VocaWord(etWord.text.toString(), etMean.text.toString()))
                finish()
            }
        }
    }
    override fun getViewBinding(): ActivitySaveWordBinding {
        return ActivitySaveWordBinding.inflate(layoutInflater)
    }
}