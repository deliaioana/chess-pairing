package eu.chessout.v2.ui.playerdashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import eu.chessout.shared.Constants
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.player_dashboard_fragment.*

class PlayerDashboardFragment : Fragment() {

    companion object {
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE_READ_CODE = 1000
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE_WRITE_CODE = 1001
        private const val IMAGE_PICK_CODE = 1002
        fun newInstance() = PlayerDashboardFragment()
    }

    lateinit var clubId: String
    lateinit var playerId: String
    private val viewModel: PlayerDashboardViewModel by viewModels()
    private val storage = FirebaseStorage.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.clubId = requireArguments().getString("clubId")!!
            this.playerId = requireArguments().getString("playerId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_dashboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profilePicture.setOnClickListener {
            if (askForPermissions()) {
                pickImageFromGallery()
            } else {
                Toast.makeText(
                    requireContext(), "Permission denied by rationale",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        viewModel.initModel()
    }

    private fun isPermissionsAllowed(permisionKey:String): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }



    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Log.d(Constants.LOG_TAG, "Permissions not ok READ")
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_EXTERNAL_STORAGE_READ_CODE
                    )
                }
            }
            return false
        }

        if (!isPermissionsAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Log.d(Constants.LOG_TAG, "Permissions not ok WRITE")
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_EXTERNAL_STORAGE_WRITE_CODE
                    )
                }
            }
            return false
        }

        return true
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IMAGE_PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "Image selection error: Not able to select image")
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    setImage(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(Constants.LOG_TAG, "Crop error: ${result.error}")
                }
            }
        }

        if (resultCode == -100 /*Activity.RESULT_OK*/ && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data!!.data
            Log.d(Constants.LOG_TAG, "imageUri: $imageUri")
            val cursor =
                requireContext().contentResolver.query(imageUri!!, null, null, null, null)!!
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val displayName = cursor.getString(nameIndex)!!


            val locPlayerMedia = Constants.LOCATION_PLAYER_MEDIA
                .replace(Constants.CLUB_KEY, clubId)
                .replace(Constants.PLAYER_KEY, playerId) + "/testImage"
            val storageReference = storage.reference.child(locPlayerMedia)
            //val stream = FileInputStream(File(imageUri.toString()))

            val metadata = storageMetadata {
                contentType = getContentType(displayName)
            }
            val uploadTask = storageReference.putFile(imageUri!!, metadata)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.d(Constants.LOG_TAG, "Upload is $progress% done")
            }.addOnPausedListener {
                println("Upload is paused")
            }
        }
    }

    private fun setImage(uri: Uri) {
        Log.d(Constants.LOG_TAG, "Time to use glide to show the image: $uri")
    }

    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireContext(), this)
    }

    private fun getContentType(displayName: String): String {
        val items = displayName.split(".")
        val extension = items[1]
        if (extension == "jpg") {
            return "image/jpeg"
        }
        throw IllegalStateException("Not supported content type")

    }
}
