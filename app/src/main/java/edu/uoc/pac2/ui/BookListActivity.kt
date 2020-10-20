package edu.uoc.pac2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksRoomManager
import edu.uoc.pac2.data.FirestoreBookData
import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.view_book_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    //private val db = Firebase.firestore

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

        //Admob
        loadAdMob()

    }

    //Admob
    //Recordar reemplazar el adUnitId del layout cuando lanzes la aplicación
    private fun loadAdMob(){
        MobileAds.initialize(this) {}
        //ya no se necesita el addTestDevice según la documentación, lo hace automaticamente.
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = book_list//findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun getBooks() {
        lifecycleScope.launch(Dispatchers.IO) {
            loadBooksFromLocalDb() //primero intentamos a cargar desde bd local
            if (myApplication.hasInternetConnection()){ //si hay conexion lo cargamos desde firestore y lo guardamos
                getBooksFromFirestoreAndSaveToLocalDb()
            }
        }
    }

    //He elegido la primera opción, recoger los libros solo una vez, porque esta aplicación no es de tipo chat
    //No necesitamos estar continuamente escuchando para actualizar los cambios a tiempo real.
    private fun getBooksFromFirestoreAndSaveToLocalDb(){
//        db.collection(FirestoreBookData.COLLECTION_BOOKS).get().addOnSuccessListener {snapShot ->
//            val books = snapShot.documents.mapNotNull { it.toObject(Book::class.java) }
//            adapter.setBooks(books)
//            saveBooksToLocalDatabase(books) //guardamos al bd local
//        }
        FirestoreBookData.getBooksFromFirestoreThenDo {
            adapter.setBooks(it)
            saveBooksToLocalDatabase(it)
        }
    }

    // cargar libros desde la base de datos local
    private fun loadBooksFromLocalDb() {
//        val books = BookRoomManager.getAllBooks()//myApplication.getBooksInteractor().getAllBooks()
//        books?.let {
//            adapter.setBooks(books)
//        }
//        BooksRoomManager.getAllBooksThenDo {
//            adapter.setBooks(it)
//        }
        lifecycleScope.launch {
            val books = withContext(Dispatchers.IO){BooksRoomManager.getAllBooks()}
            adapter.setBooks(books)
        }

    }

    // guardar libros al base de datos local
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        lifecycleScope.launch(Dispatchers.IO) {
            BooksRoomManager.saveBooks(books)
            //myApplication.getBooksInteractor().saveBooks(books)
        }
    }



}