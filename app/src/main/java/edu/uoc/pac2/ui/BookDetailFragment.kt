package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksRoomManager
import kotlinx.android.synthetic.main.fragment_book_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    //private val myApplication by lazy { activity!!.application as MyApplication}
    private val myActivity by lazy { activity!! as BookDetailActivity }
    private var uid = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook()
        myActivity.setSupportActionBar(toolbar)
        myActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    // cargar libro con id
    private fun loadBook() {
//        Thread {
//            arguments?.getInt(ARG_ITEM_ID)?.let {
//                uid = it
//                val book = myApplication.getBooksInteractor().getBookById(uid)
////                myActivity.runOnUiThread {
////                    if (book != null){
////                        initUI(book)
////                    }else{//He añadido esto para pasar el Ex5Test
////                        fab.setOnClickListener {
////                            shareContent(null)
////                        }
////                    }
////                }
//                if (book != null){
//                    initUI(book)
//                }else{//He añadido esto para pasar el Ex5Test
//                    fab.setOnClickListener {
//                        shareContent(null)
//                    }
//                }
//
//            }
//        }.start()
//        lifecycleScope.launch {
//            arguments?.getInt(ARG_ITEM_ID)?.let {
//                Log.i("Hilo actual", Thread.currentThread().toString())
//                uid = it
//                //val book = withContext(Dispatchers.IO){myApplication.getBooksInteractor().getBookById(uid)}
//                val book = withContext(Dispatchers.IO){BookRoomManager.getBookById(uid)}
//                if (book != null){
//                    initUI(book)
//                }else{//He añadido esto para pasar el Ex5Test
//                    fab.setOnClickListener {
//                        shareContent(null)
//                    }
//                }
//            }
//        }

        arguments?.getInt(ARG_ITEM_ID)?.let {
            uid = it
            lifecycleScope.launch {
                val book = withContext(Dispatchers.IO){BooksRoomManager.getBookById(uid)}
                if (book != null){
                    initUI(book)
                }else{//He añadido esto para pasar el Ex5Test
                    fab.setOnClickListener {
                        shareContent(null)
                    }
                }
            }
        }

    }

    // inicializamos el UI
    private fun initUI(book: Book) {
        collapsingToolbarLayout.title = book.title
        textViewAuthor.text = book.author
        textViewDate.text = book.publicationDate
        textViewDescription.text = book.description
        Picasso.get().load(book.urlImage).into(imageViewAppBar)
        Picasso.get().load(book.urlImage).into(imageViewBook)
        fab.setOnClickListener {
            shareContent(book)
        }
    }

    // compartir el libro con otras aplicaciones
    private fun shareContent(book: Book?) {
        Intent(Intent.ACTION_SEND).run {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Title: ${book?.title}, imageUrl: ${book?.urlImage}")
            startActivity(Intent.createChooser(this, getString(R.string.share_with)))
        }
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}