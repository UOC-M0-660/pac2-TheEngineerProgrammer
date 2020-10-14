package edu.uoc.pac2.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.data.FirestoreBookData

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"
    private val db = Firebase.firestore

    private lateinit var adapter: BooksListAdapter

    private val myApplication by lazy { application as MyApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        //FirestoreBookData.addBooksDataToFirestoreDatabase() // Ejecutado solo una vez. Para añadir los datos a firestore


    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun getBooks() {
        Thread{
            loadBooksFromLocalDb() //primero intentamos a cargar desde bd local
            if (myApplication.hasInternetConnection()){ //si hay conexion lo cargamos desde firestore y lo guardamos
                getBooksFromFirestoreAndSaveToLocalDb()
            }
        }.start()
    }

    //He elegido la primera opción, recoger los libros solo una vez, porque esta aplicación no es de tipo chat
    //No necesitamos estar continuamente escuchando para actualizar los cambios a tiempo real.
    private fun getBooksFromFirestoreAndSaveToLocalDb(){
        db.collection(FirestoreBookData.COLLECTION_BOOKS).get().addOnSuccessListener {snapShot ->
            val books = snapShot.documents.mapNotNull { it.toObject(Book::class.java) }
            adapter.setBooks(books)
            saveBooksToLocalDatabase(books) //guardamos al bd local
        }
    }

    // cargar libros desde la base de datos local
    private fun loadBooksFromLocalDb() {
        val books = myApplication.getBooksInteractor().getAllBooks()
        adapter.setBooks(books)
    }

    // guardar libros al base de datos local
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        Thread{
            myApplication.getBooksInteractor().saveBooks(books)
        }.start()
    }



}