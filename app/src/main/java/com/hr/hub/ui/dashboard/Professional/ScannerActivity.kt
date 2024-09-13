package com.hr.hub.ui.dashboard.Professional

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hr.hub.R
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class ScannerActivity : AppCompatActivity() {
    private lateinit var img_camview: PreviewView
    private lateinit var imgView: ImageView
    private lateinit var btnflash: ImageButton
    private lateinit var delbtn: ImageButton
    private lateinit var galarybtn: ImageButton
    private lateinit var cropbtn: ImageButton
    private lateinit var cambtn: FloatingActionButton
    private lateinit var rotatebtn: ImageButton
    private lateinit var pdfbtn: ImageButton

    private var newImgCapture: ImageCapture? = null
    private val capturedImage = mutableListOf<Bitmap>()
    private var isFlashon = false

    companion object {
        private const val REQUEST_CODE_PERMISSION = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_GALLERY_CODE = 1001
        private const val REQUEST_CROP_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        setContentView(R.layout.activity_scanner)

        initViews()

        if (allPermissionsGranted()) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
        }

        setupClickListeners()
    }

    override fun onPause() {
        super.onPause()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()) {
            openCamera()
        }
    }

    private fun initViews() {
        img_camview = findViewById(R.id.cv_image)
        imgView = findViewById(R.id.iv_captured_image)
        btnflash = findViewById(R.id.ibtn_flash)
        delbtn = findViewById(R.id.ibtn_delete)
        galarybtn = findViewById(R.id.ibtn_galarychoose)
        cropbtn = findViewById(R.id.ibtn_crop)
        cambtn = findViewById(R.id.fbtn_doccam)
        rotatebtn = findViewById(R.id.ibtn_rotate)
        pdfbtn = findViewById(R.id.ibtn_pdfsave)
    }

    private fun setupClickListeners() {
        btnflash.setOnClickListener { toggleFlash() }
        delbtn.setOnClickListener { deleteCurrent() }
        galarybtn.setOnClickListener { selectGalleryImage() }
        cropbtn.setOnClickListener { cropImage() }
        cambtn.setOnClickListener { captureImage() }
        rotatebtn.setOnClickListener { rotateImage() }
        pdfbtn.setOnClickListener { convertToPdf() }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(img_camview.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            newImgCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, newImgCapture)
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val imageCapture = newImgCapture ?: return

        val outputImg = ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
        ).build()

        imageCapture.takePicture(outputImg, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(baseContext, "Picture Saved", Toast.LENGTH_SHORT).show()

                    val imageUri = outputFileResults.savedUri
                    imageUri?.let { uri ->
                        GlobalScope.launch(Dispatchers.IO) {
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                bitmap?.let {
                                    withContext(Dispatchers.Main) {
                                        capturedImage.add(it)
                                        imgView.setImageBitmap(it)
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }
    private fun selectGalleryImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                UCrop.REQUEST_CROP -> {
                    val resultUri = data?.let { UCrop.getOutput(it) }
                    resultUri?.let { uri ->
                        GlobalScope.launch(Dispatchers.IO) {
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                withContext(Dispatchers.Main) {
                                    bitmap?.let {
                                        imgView.setImageBitmap(it)
                                        capturedImage.add(it)
                                    }
                                }
                            }
                        }
                    }
                }
                UCrop.RESULT_ERROR -> {
                    val error = UCrop.getError(data!!)
                    Log.e("UCrop", "Crop error: $error")
                }
                REQUEST_GALLERY_CODE -> {
                    val imageUri = data?.data
                    imageUri?.let { uri ->
                        GlobalScope.launch(Dispatchers.IO) {
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                withContext(Dispatchers.Main) {
                                    bitmap?.let {
                                        imgView.setImageBitmap(it)
                                        capturedImage.add(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun toggleFlash() {
        isFlashon = !isFlashon
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val camera = cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA)
            camera.cameraControl.enableTorch(isFlashon)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun deleteCurrent() {
        if (capturedImage.isNotEmpty()) {
            capturedImage.removeAt(capturedImage.size - 1)
            imgView.setImageBitmap(capturedImage.lastOrNull())
            Toast.makeText(baseContext, "Image deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, "No image to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cropImage() {
        val lastBitmap = capturedImage.lastOrNull() ?: return
        val sourceUri = getImageUri(lastBitmap) ?: return
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))

        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .start(this)
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "temp_image.jpg")
        return try {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Uri.fromFile(file)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun rotateImage() {
        val lastBitmap = capturedImage.lastOrNull() ?: return
        val matrix = Matrix().apply { postRotate(90f) }
        val rotatedBitmap = Bitmap.createBitmap(lastBitmap, 0, 0, lastBitmap.width, lastBitmap.height, matrix, true)
        capturedImage[capturedImage.size - 1] = rotatedBitmap
        imgView.setImageBitmap(rotatedBitmap)
    }

    private fun convertToPdf() {
        if (capturedImage.isEmpty()) {
            Toast.makeText(this, "No images to convert", Toast.LENGTH_SHORT).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val pdfDocument = PdfDocument()
            for ((index, bitmap) in capturedImage.withIndex()) {
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas
                val paint = android.graphics.Paint()
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
                canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)
                pdfDocument.finishPage(page)
            }

            val pdfFile = File(getExternalFilesDir(null), "document_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())}.pdf")
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ScannerActivity, "PDF saved to ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun savePdfToMediaStore(pdfDocument: PdfDocument) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "document.pdf")
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/")
        }

        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            Toast.makeText(this, "PDF saved to ${uri.path}", Toast.LENGTH_LONG).show()
        } ?: run {
            Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show()
        }
    }

}
