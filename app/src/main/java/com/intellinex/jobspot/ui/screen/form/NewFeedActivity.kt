package com.intellinex.jobspot.ui.screen.form

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.contents.ImageAdapter
import com.intellinex.jobspot.models.contents.ImageModel
import com.intellinex.jobspot.utils.ToastMessage

class NewFeedActivity : AppCompatActivity() {

    private var REQUEST_CODE = 1
    private lateinit var imagePickerCardView: CardView
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageList = mutableListOf<ImageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_feed)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }

        imageRecyclerView = findViewById(R.id.recyclerMedia)
        imagePickerCardView = findViewById(R.id.pickImageCardView)
        imageAdapter = ImageAdapter(imageList) {position ->
            removeImage(position)
        }
        imageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageRecyclerView.adapter = imageAdapter

        imagePickerCardView.setOnClickListener {
            openGallery()
        }

        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

    }

    private fun openGallery() {

        if(imageList.size >= 2) {
            val toastMessage = ToastMessage(this, "Sorry, you can only upload up to 2 images.")
            toastMessage.startToast()
            toastMessage.setToastIcon(ContextCompat.getDrawable(this@NewFeedActivity, R.drawable.warning)!!)
            return
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
            if(result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    if(data.clipData != null) {
                        val count = data.clipData!!.itemCount
                        val totalImages = imageList.size

                        if (totalImages + count > 2) {
                            Toast.makeText(this, "You can only upload up to 2 images.", Toast.LENGTH_SHORT).show()
                            return@registerForActivityResult
                        }
                        for (i in 0 until count) {
                            val uri: Uri = data.clipData!!.getItemAt(i).uri
                            imageList.add(ImageModel(uri))
                        }
                        imageAdapter.notifyDataSetChanged()
                    }else {
                        data.data?.let { uri ->
                            if (imageList.size < 2) {
                                imageList.add(ImageModel(uri))
                                imageAdapter.notifyDataSetChanged()
                            } else {
                                Toast.makeText(this, "You can only upload up to 2 images.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } ?: run {
                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun removeImage(position: Int) {
        if (position >= 0 && position < imageList.size) {
            imageList.removeAt(position)
            imageAdapter.notifyItemRemoved(position)
            imageAdapter.notifyItemRangeChanged(position, imageList.size)
        }
    }
}