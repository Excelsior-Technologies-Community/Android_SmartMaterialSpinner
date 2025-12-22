package com.ext.android_smartmaterialspinner

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ext.smartmaterialspinner.SmartMaterialSpinner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val smatSpinner = findViewById<SmartMaterialSpinner>(R.id.spinner)

        smatSpinner.setItems(listOf("India", "USA", "UK"))

        smatSpinner.setOnItemSelectedListener { item, position ->
            Log.d("Spinner", "Selected: $item at $position")
        }



    }
}