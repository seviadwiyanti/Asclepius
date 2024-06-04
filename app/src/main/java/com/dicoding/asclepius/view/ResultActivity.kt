package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.entity.RiwayatEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.viewmodel.RiwayatViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date



class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var riwayatViewModel: RiwayatViewModel
    private var riwayatEntity: RiwayatEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        riwayatViewModel = obtainViewModel(this@ResultActivity)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val result = intent.getStringExtra(EXTRA_RESULT)

        if (imageUriString != null && result != null) {
            val imageUri = Uri.parse(imageUriString)
            Log.d("Image URI", "showImage: $imageUri")
            binding.resultImage.setImageURI(imageUri)
            binding.resultText.text = result

            binding.save.setOnClickListener {
                val sdf = SimpleDateFormat("'Tanggal\n'dd-MM-yyyy '\n\nwaktu\n'HH:mm:ss z")
                val currentDateAndTime = sdf.format(Date())
                riwayatEntity = RiwayatEntity(
                    score = result,
                    createdAt = currentDateAndTime.toString()
                )
                riwayatViewModel.insert(riwayatEntity!!)
                finish()
            }
        } else {
            Toast.makeText(this, "Invalid data passed", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun obtainViewModel(resultActivity: ResultActivity): RiwayatViewModel {
        val factory = ViewModelFactory.getInstance(resultActivity.application)
        return ViewModelProvider(resultActivity, factory)[RiwayatViewModel::class.java]
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
