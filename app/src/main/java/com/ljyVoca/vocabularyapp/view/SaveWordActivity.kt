package com.ljyVoca.vocabularyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljyVoca.vocabularyapp.BaseActivity
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.databinding.ActivitySaveWordBinding
import com.ljyVoca.vocabularyapp.util.setOnSingleClickListener

class SaveWordActivity: BaseActivity<ActivitySaveWordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.btnSave.setOnSingleClickListener {
            
        }
    }
    override fun getViewBinding(): ActivitySaveWordBinding {
        return ActivitySaveWordBinding.inflate(layoutInflater)
    }
}