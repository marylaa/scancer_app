package com.example.tom.main_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.tom.interfaces.BodyMapActivity
import com.example.tom.interfaces.DoctorsMapActivity
import com.example.tom.InfoActivity
import com.example.tom.R
import com.example.tom.image.GetImageActivity

class MainUserActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        val addPhoto = findViewById<ImageButton>(R.id.camera)
        addPhoto.setOnClickListener(this)

        val bodyMap = findViewById<ImageButton>(R.id.human)
        bodyMap.setOnClickListener(this)

        val doctorMap = findViewById<ImageButton>(R.id.doctors)
        doctorMap.setOnClickListener(this)

        val info = findViewById<ImageButton>(R.id.info)
        info.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.camera -> {
                    val intent = Intent(this, GetImageActivity::class.java)
                    startActivity(intent)
                }

                R.id.human -> {
                    val intent = Intent(this, BodyMapActivity::class.java)
                    startActivity(intent)
                }

                R.id.doctors -> {
                    val intent = Intent(this, DoctorsMapActivity::class.java)
                    startActivity(intent)
                }

                R.id.info -> {
                    val intent = Intent(this, InfoActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}


