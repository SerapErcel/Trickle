package com.serapercel.trickle.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serapercel.trickle.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)
    }
}