package com.hr.hub.ui.dashboard.Professional

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hr.hub.R
import java.io.IOException

class TextScanTutorialActivity : AppCompatActivity() {

    private lateinit var tvScanned: TextView
    private lateinit var pbScanned: ProgressBar
    private lateinit var ivScanned: ImageView

    private val REQUEST_IMAGE_CAPTURE = 123
    private val CAMERA_REQUEST_CODE = 100
    private val PICK_IMAGE_REQUEST = 101

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moreprofessional)

        // Initialize UI elements
        tvScanned = findViewById(R.id.tv_scannedtext)
        pbScanned = findViewById(R.id.pb_scanned)
        ivScanned = findViewById(R.id.iv_scan)

        val btnScan = findViewById<ImageButton>(R.id.ibtn_scan)
        val btnRescan = findViewById<ImageButton>(R.id.ibtn_rescan)
        val btnCopy = findViewById<ImageButton>(R.id.ibtn_copy)

        // Set up button click listeners
        btnScan.setOnClickListener {
            requestCameraPermission()
        }

        btnRescan.setOnClickListener {
            requestFilePermission()
        }

        btnCopy.setOnClickListener {
            copyTextToClipboard()
        }
    }

    private fun copyTextToClipboard() {
        val textToCopy = tvScanned.text.toString()
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Scanned Text", textToCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show()
    }

    private fun requestCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            openCamera()
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open camera. ${e.localizedMessage}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun requestFilePermission(){
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),PICK_IMAGE_REQUEST)
        }
        else{
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                openCamera()
            }
            else {
                // Permission denied, explain to the user why it's needed
                Toast.makeText(this, "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show()
            }
        }
        if(requestCode==PICK_IMAGE_REQUEST){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery()
            }
            else{
                Toast.makeText(this,"File permission is required to use this feature",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle camera result
                val bitmap = data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    ivScanned.setImageBitmap(it)
                    detectTextML(it)
                } ?: run {
                    Toast.makeText(this, "Failed to get image from camera.", Toast.LENGTH_SHORT).show()
                }
            }
            else if (requestCode == PICK_IMAGE_REQUEST) {
                val imageUri: Uri? = data?.data
                if (imageUri == null) {
                    Toast.makeText(this, "Failed to get image URI.", Toast.LENGTH_SHORT).show()
                    return
                }

                try {
                    val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    }
                    else {
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                        ImageDecoder.decodeBitmap(source)
                    }
                    ivScanned.setImageBitmap(bitmap)
                    detectTextML(bitmap)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun detectTextML(bitmap: Bitmap) {
        pbScanned.visibility = View.VISIBLE
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizers = listOf(
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS),
            TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()),
            TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build()),
            TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()),
            TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        )

        for (recognizer in recognizers) {
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    pbScanned.visibility = View.GONE
                    displayRecognizedResult(visionText)
                }
                .addOnFailureListener {
                    pbScanned.visibility = View.GONE
                    Toast.makeText(this, "Failed to process image.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun displayRecognizedResult(result: Text) {
        val extractedText = StringBuilder()
        val resultText = result.text
        extractedText.append("Recognized Text:\n$resultText\n\n")

        for (block in result.textBlocks) {
            val blockText = block.text
            extractedText.append("Block Text: $blockText\n")
            /*
            block.cornerPoints?.let { points ->
                extractedText.append("Block Corner Points: ${points.joinToString()}\n")
            }
            block.boundingBox?.let {
                extractedText.append("Block Bounding Box: $it\n")
            }

             */

            for (line in block.lines) {
                val lineText = line.text
                extractedText.append("\tLine Text: $lineText\n")
                /*
                line.cornerPoints?.let { points ->
                    extractedText.append("\tLine Corner Points: ${points.joinToString()}\n")
                }
                line.boundingBox?.let {
                    extractedText.append("\tLine Bounding Box: $it\n")
                }
                 */

                for (element in line.elements) {
                    val elementText = element.text
                    extractedText.append("\t\tElement Text: $elementText\n")
                    /*
                    element.cornerPoints?.let { points ->
                        extractedText.append("\t\tElement Corner Points: ${points.joinToString()}\n")
                    }

                    element.boundingBox?.let {
                        extractedText.append("\t\tElement Bounding Box: $it\n")
                    }
                     */
                }
                extractedText.append("\n")
            }
            tvScanned.text = extractedText.toString()
        }
    }
}
