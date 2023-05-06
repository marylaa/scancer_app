package com.example.tom.image

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tom.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.*
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject

class GetImageActivity : AppCompatActivity(), View.OnClickListener {

    private var selectedImage: Uri? = null
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }

        setContentView(R.layout.activity_get_image)

        val addPhoto = findViewById<Button>(R.id.button)
        addPhoto.setOnClickListener(this)

        imageView = findViewById(R.id.image)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.button -> {
                    if (selectedImage === null) {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, 3)
                    } else {
                        sendHttpRequest()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.data
            Log.d("ZDJECIE", selectedImage.toString())
            imageView.setImageURI(selectedImage)

            val addPhoto = findViewById<Button>(R.id.button)
            addPhoto.text = "Dokonaj analizy"
        }
    }

    private fun sendHttpRequest() {

        if (selectedImage != null) {

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

            val task = DownloadImageTask(imageView, bitMap)
            task.execute()
        }
    }
}

class DownloadImageTask(private val imageView: ImageView, private val selectedImage: Bitmap) :
    AsyncTask<String, Void, Array<Any>>() {
    override fun doInBackground(vararg urls: String): Array<Any> {
        val client = OkHttpClient()
        val url = "http://192.168.1.18:5000" // IP na którym stoi serwer

        val stream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        val MEDIA_TYPE_JPEG = "image/jpeg".toMediaType()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "image.jpg", RequestBody.create(MEDIA_TYPE_JPEG, byteArray))
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseData = response.body?.string()

        val jsonObject = JSONObject(responseData)
        val imageString = jsonObject.getString("image")
        val chance = jsonObject.getString("chance")
        val label = jsonObject.getString("label")

        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return arrayOf(chance, label, bitmap)
    }

    override fun onPostExecute(result: Array<Any>) {
        if (result.isNotEmpty()) {
            val bitmap = result[2] as Bitmap
            imageView.setImageBitmap(bitmap)

            val chance = result[0] as String
            val label = result[1] as String
            var message = ""

            if(label.equals("Lagodna_Zmiana")) {
                val labelPl = "Zmiana łagodna"
                message = "Przeanalizowano wskazaną zmianę: \n\nTyp zmiany: " + labelPl + "\nDokładność analizy: " + chance + "\n\nZdjęcie wskazuje, że obecnie nie ma powodów do niepokoju, zmiana nie wykazuje niebezpiecznych cech. \n\nPamiętaj jednak o regulanym samobadaniu i monitorowaniu ewolucji zmiany!"
            } else {
                val labelPl = "Zmiana niepokojąca"
                message = "Przeanalizowano wskazaną zmianę: \n\nTyp zmiany: " + labelPl + "\nDokładność analizy: " + chance + "\n\nZdjęcie wskazuje podejrzenie występowania nowotworów złośliwych skóry. Zaleca się szybkie udanie do specjalisty w celu weryfikacji diagnozy. Zapoznaj się z zakładką ZNAJDŹ DERMATOLOGA na stronie głównej aplikacji. \n\nPamiętaj, że szybkie wykrycie choroby zwiększa szanse na udane leczenie!"

            }

            val builder = AlertDialog.Builder(imageView.context)
            builder.setMessage(message)
                .setPositiveButton("Rozumiem") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.color.background)
            dialog.show()
        }
    }
}

