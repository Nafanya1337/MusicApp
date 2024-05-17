import android.net.Uri
import android.util.Log
import com.example.musicapp.data.storage.interfaces.AuthStorage
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthStorageImpl : AuthStorage {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

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

