import android.net.Uri
import android.util.Log
import com.example.musicapp.data.remote.models.TrackDTO
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.TrackVO
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageImpl :
    com.example.musicapp.data.storage.interfaces.FirebaseStorage {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val storage by lazy { com.google.firebase.storage.FirebaseStorage.getInstance() }

    override suspend fun signUp(email: String, password: String, nickname: String, imageUri: Uri, callback: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            writeNickname(user.uid, nickname) { success ->
                                if (success) {
                                    setUserImage(user.uid, imageUri.toString())
                                    callback(true)
                                } else {
                                    callback(false)
                                }
                            }
                        } else {
                            callback(false)
                        }
                    } else {
                        callback(false)
                    }
                }
        }
    }

    override fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun getUserNickname(uid: String): String {
        return suspendCoroutine { continuation ->
            database.getReference("users").child(uid).child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val nickname = snapshot.getValue(String::class.java) ?: ""
                        continuation.resume(nickname)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "Failed to read nickname.", error.toException())
                        continuation.resume("")
                    }
                })
        }
    }

    override suspend fun getUserImage(uid: String): String? {
        return try {
            val storageRef = storage.getReference("users").child(uid).child("image")
            val listResult = storageRef.listAll().await()
            if (listResult.items.isNotEmpty()) {
                val uri = listResult.items[0].downloadUrl.await()
                uri.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUserFavourites(uid: String): List<TrackVO> {
        return suspendCoroutine { continuation ->
            database.getReference("users").child(uid).child("favourites")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val favourites = mutableListOf<TrackVO>()
                        for (childSnapshot in snapshot.children) {
                            val favourite = childSnapshot.getValue(TrackVO::class.java)
                            if (favourite != null) {
                                favourites.add(favourite)
                            }
                        }
                        continuation.resume(favourites)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "Failed to read favourites.", error.toException())
                        continuation.resume(emptyList())
                    }
                })
        }
    }

    override suspend fun addToFavourites(uid: String, track: TrackVO, callback: (Boolean) -> Unit) {
        val trackRef = database.getReference("users").child(uid).child("favourites").push()
        trackRef.setValue(track).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    override suspend fun deleteFromFavourites(uid: String, trackId: Long, callback: (Boolean) -> Unit) {
        val favouritesRef = database.getReference("users").child(uid).child("favourites")
        favouritesRef.orderByChild("id").equalTo(trackId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var success = true
                        var pendingTasks = snapshot.childrenCount.toInt()

                        for (childSnapshot in snapshot.children) {
                            childSnapshot.ref.removeValue().addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    success = false
                                }
                                pendingTasks--
                                if (pendingTasks == 0) {
                                    callback(success)
                                }
                            }
                        }
                    } else {
                        callback(false) // No matching items found
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to delete from favourites.", error.toException())
                    callback(false)
                }
            })
    }


    override fun setUserImage(uid: String, imageUri: String) {
        val randomKey = UUID.randomUUID().toString()
        val riversRef = storage.getReference("users").child(uid).child("image/$randomKey")
        riversRef.putFile(Uri.parse(imageUri))
            .addOnSuccessListener { OnSuccessListener<UploadTask.TaskSnapshot> {
                // Image upload success
            } }
            .addOnFailureListener {
                // Handle unsuccessful uploads
            }
    }

    private fun writeNickname(uid: String, nickname: String, callback: (Boolean) -> Unit) {
        val nicknameRef: DatabaseReference = database.getReference("users").child(uid).child("nickname")
        nicknameRef.setValue(nickname).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun signOut() = auth.signOut()

    companion object {
        private const val TAG = "AuthStorageImpl"
    }
}

