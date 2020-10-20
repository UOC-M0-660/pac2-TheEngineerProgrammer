package edu.uoc.pac2.data

import android.app.Application
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

    private lateinit var db: ApplicationDatabase

    fun initiate(myApplication: Application){//Esta función tiene que ser llamada antes de usar los métodos
        db = Room.databaseBuilder(myApplication, ApplicationDatabase::class.java, "database-name").build()
    }

    //Los suspends en este caso parecen que son redundantes
    fun saveBook(book: Book){
        db.bookDao().saveBook(book)
    }

    fun getAllBooks(): List<Book>{
        return db.bookDao().getAllBooks()
    }

    fun saveBooks(books: List<Book>) {
        books.forEach { saveBook(it) }
    }

    fun getBookById(id: Int): Book?{
        return db.bookDao().getBookById(id)
    }
}