package eu.chessout.v2.ui.playerdashboard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.UploadTask
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Picture
import eu.chessout.v2.util.MyFirebaseUtils

class PlayerDashboardViewModel : ViewModel() {
    private lateinit var clubId: String
    private lateinit var playerId: String
    val defaultPictureUri = MutableLiveData<String?>()
    private val myFirebaseUtils = MyFirebaseUtils()

    fun initModel(clubId: String, playerId: String) {
        this.clubId = clubId
        this.playerId = playerId

        registerPictureListener()
    }

    fun setDefaultPicture(uploadTask: UploadTask, pictureName: String) {
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            Log.d(Constants.LOG_TAG, "Upload is $progress% done")
        }.addOnPausedListener {
            println("Upload is paused")
        }.addOnCompleteListener {
            persisDefaultPicture(pictureName)

            Log.d(Constants.LOG_TAG, "Upload complete")
        }
    }

    fun setCachePictureUri(uri: Uri) {
        defaultPictureUri.value = uri.toString()
    }

    private fun persisDefaultPicture(pictureName: String) {
        myFirebaseUtils.setDefaultPicture(clubId, playerId, pictureName)
    }

    private fun registerPictureListener() {
        class PictureListener : MyFirebaseUtils.PictureListener {
            override fun valueUpdated(value: Picture) {
                defaultPictureUri.value = value.stringUri
            }
        }
        myFirebaseUtils.registerDefaultPlayerPictureListener(
            false, clubId, playerId, PictureListener()
        )

    }
}
