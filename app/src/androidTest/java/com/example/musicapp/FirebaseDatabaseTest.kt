
import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.UUID

@ExperimentalCoroutinesApi
class FirebaseStorageImplTest {

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockDatabase: FirebaseDatabase

    @Mock
    private lateinit var mockStorage: FirebaseStorage

    @Mock
    private lateinit var mockUser: FirebaseUser

    @Mock
    private lateinit var mockReference: DatabaseReference

    @Mock
    private lateinit var mockChildReference: DatabaseReference

    @Mock
    private lateinit var mockStorageReference: StorageReference

    @Mock
    private lateinit var mockUploadTask: UploadTask

    @Mock
    private lateinit var mockUriTask: Task<Uri>

    @Captor
    private lateinit var valueEventListenerCaptor: ArgumentCaptor<ValueEventListener>

    private lateinit var firebaseStorageImpl: FirebaseStorageImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockAuth.currentUser).thenReturn(mockUser)
        `when`(mockDatabase.getReference(anyString())).thenReturn(mockReference)
        `when`(mockReference.child(anyString())).thenReturn(mockChildReference)
        firebaseStorageImpl = FirebaseStorageImpl(mockAuth, mockDatabase, mockStorage)
    }

    @Test
    fun testGetCurrentUser() {
        // Act
        val currentUser = firebaseStorageImpl.getCurrentUser()

        // Assert
        assert(currentUser == mockUser)
    }

    @Test
    fun testSetUserImage() {
        // Arrange
        val uid = "testUid"
        val imageUri = "http://example.com/image.png"
        val randomKey = UUID.randomUUID().toString()
        val mockUploadTask = mock(UploadTask::class.java)
        val mockUploadTaskSnapshot = mock(UploadTask.TaskSnapshot::class.java)
        val mockTaskCompletionSource = TaskCompletionSource<UploadTask.TaskSnapshot>()

        `when`(mockStorage.getReference(anyString())).thenReturn(mockStorageReference)
        `when`(mockStorageReference.child(anyString())).thenReturn(mockStorageReference)
        `when`(mockStorageReference.putFile(any(Uri::class.java))).thenReturn(mockUploadTask)

        doAnswer {
            val onSuccessListener: OnSuccessListener<UploadTask.TaskSnapshot> = it.getArgument(0)
            onSuccessListener.onSuccess(mockUploadTaskSnapshot)
            mockUploadTask
        }.`when`(mockUploadTask).addOnSuccessListener(any())

        // Act
        firebaseStorageImpl.setUserImage(uid, imageUri)

        // Verify
        verify(mockStorageReference).putFile(Uri.parse(imageUri))
    }

    @Test
    fun testSignUpFirebase() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val mockAuthResult = mock(AuthResult::class.java)
        val task = Tasks.forResult(mockAuthResult)
        `when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(task)

        // Act
        firebaseStorageImpl.signUpFirebase(email, password)

        // Assert
        verify(mockAuth).createUserWithEmailAndPassword(email, password)
    }
}
