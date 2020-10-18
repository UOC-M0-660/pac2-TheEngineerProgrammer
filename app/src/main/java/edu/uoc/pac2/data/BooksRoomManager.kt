package edu.uoc.pac2.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//He decidido añadir esto porque pienso que será más facil de usar así en Room
object BooksRoomManager {

    private var db: ApplicationDatabase? = null
    private const val TAG = "BookRoomManager"

    fun initiate(context: Context){
        db =  Room.databaseBuilder(context, ApplicationDatabase::class.java, "database-name").build()
    }

    fun saveBook(book: Book){
        checkDB()
        Thread{
            db!!.bookDao().saveBook(book)
        }.start()
    }

    fun getAllBooksThenDo(doAfter: (List<Book>) -> Unit){
        checkDB()
        GlobalScope.launch(Dispatchers.IO){
            val books = db!!.bookDao().getAllBooks()
            withContext(Dispatchers.Main){doAfter(books)}
        }
    }

    fun saveBooks(books: List<Book>) {
        checkDB()
        books.forEach { saveBook(it) }
    }

    fun getBookById(id: Int, doAfter: (Book?) -> Unit){
        checkDB()
        GlobalScope.launch(Dispatchers.IO){
            val book = db!!.bookDao().getBookById(id)
            withContext(Dispatchers.Main){doAfter(book)}
        }
    }

    private fun checkDB(){
        if (db == null){
            Log.e(TAG, "No puedes usar este método sin haber inicializado BookRoomManager en MyApplication")
            return
        }
    }
}