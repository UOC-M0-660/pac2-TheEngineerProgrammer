package edu.uoc.pac2.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import edu.uoc.pac2.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//He decidido añadir esto porque pienso que será más facil de usar así en Room
object BooksRoomManager {
    private val db: ApplicationDatabase =
            Room.databaseBuilder(MyApplication.instance, ApplicationDatabase::class.java, "database-name").build()

    fun saveBook(book: Book){
        Thread{
            db.bookDao().saveBook(book)
        }.start()
    }

    fun getAllBooksThenDo(doAfter: (List<Book>) -> Unit){
        GlobalScope.launch(Dispatchers.IO){
            val books = db.bookDao().getAllBooks()
            withContext(Dispatchers.Main){doAfter(books)}
        }
    }

    fun saveBooks(books: List<Book>) {
        books.forEach { saveBook(it) }
    }

    fun getBookById(id: Int, doAfter: (Book?) -> Unit){
        GlobalScope.launch(Dispatchers.IO){
            val book = db.bookDao().getBookById(id)
            withContext(Dispatchers.Main){doAfter(book)}
        }
    }
}