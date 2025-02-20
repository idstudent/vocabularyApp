package com.ljyVoca.vocabularyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljyVoca.vocabularyapp.databinding.ActivityMainBinding
import com.ljyVoca.vocabularyapp.util.setOnSingleClickListener
import com.ljyVoca.vocabularyapp.view.SaveWordActivity

class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
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