package com.example.tom.main_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.tom.R
import com.example.tom.image.GetImageActivity
import com.example.tom.login_and_register.RegisterActivity

class MainUserActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        val addPhoto = findViewById<ImageButton>(R.id.camera)
        addPhoto.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.camera -> {
                    val intent = Intent(this, GetImageActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}


