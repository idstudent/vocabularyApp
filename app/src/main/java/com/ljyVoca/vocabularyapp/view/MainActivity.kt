package com.ljyVoca.vocabularyapp.view

import android.content.Intent
import android.os.Bundle
import com.ljyVoca.vocabularyapp.databinding.ActivityMainBinding
import com.ljyVoca.vocabularyapp.util.setOnSingleClickListener

class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
            btnStart.setOnSingleClickListener {
                val intent = Intent(this@MainActivity, CheckActivity::class.java)
                startActivity(intent)
            }
            btnInsert.setOnSingleClickListener {
                val intent = Intent(this@MainActivity, SaveWordActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}