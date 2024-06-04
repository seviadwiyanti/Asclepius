package com.dicoding.asclepius.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            galleryButton.setOnClickListener {
                startGallery()
            }
            analyzeButton.setOnClickListener {
                currentImageUri?.let {
                    analyzeImage(it)
                }
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_1 -> {
                        startActivity(Intent(this@MainActivity, RiwayatActivity::class.java))
                        true
                    }

                    else -> false
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            currentImageUri = resultUri
            showImage(resultUri)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = data?.let { UCrop.getError(it) }
            Log.e("uCrop", "Error cropping image: $cropError")
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.previewImageView.setImageURI(null)
            startUCrop()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startUCrop() {
        val outputUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        currentImageUri?.let {
            UCrop.of(it, outputUri)
                .withOptions(UCrop.Options().apply {
                    setCompressionFormat(Bitmap.CompressFormat.JPEG)
                    setCompressionQuality(90)
                })
                .start(this)
        }
    }

    private fun showImage(resultUri: Uri?) {
        resultUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        binding.progressIndicator.visibility = View.VISIBLE
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)

                        binding.progressIndicator.visibility = View.GONE
                    }
                }


                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()

                                    }
                                moveToResult(currentImageUri!!, displayResult)
                            }
                        }
                        binding.progressIndicator.visibility = View.GONE
                    }
                }


            }
        )
        imageClassifierHelper.classifyStaticImage(uri)
    }



    private fun moveToResult(uri: Uri, result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}