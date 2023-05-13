package com.example.tom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tom.image.GetImageActivity
import com.example.tom.interfaces.BodyMapActivity


class InfoActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_bacics)

        val profilaktyka = findViewById<ImageButton>(R.id.profilaktyka)
        profilaktyka.setOnClickListener(this)

        val objawy = findViewById<ImageButton>(R.id.objawy)
        objawy.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.profilaktyka -> {
                    val intent = Intent(this, ProfilaktykaActivity::class.java)
                    startActivity(intent)
                }

                R.id.objawy -> {
                    val intent = Intent(this, ObjawyActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}