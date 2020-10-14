package edu.uoc.pac2.data

import android.database.sqlite.SQLiteConstraintException

/**
 * This class Interacts with {@param bookDao} to perform operations in the local database.
 *
 * Could be extended also to interact with Firestore, acting as a single entry-point for every
 * book-related operation from all different datasources (Room & Firestore)
 *
 * Created by alex on 03/07/2020.
 */
class BooksInteractor(private val bookDao: BookDao) {

    // No veo nada que cambiar
    fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks()
    }

    // He añadido que en caso de que ya existe el libro, lo va a actualizar en vez de insertar
    fun saveBook(book: Book) {
        try {
            bookDao.saveBook(book)
        }catch (e: SQLiteConstraintException){
            bookDao.updateBook(book)
        }
    }

    fun saveBooks(books: List<Book>) {
        books.forEach { saveBook(it) }
    }

    // No veo nada que cambiar
    fun getBookById(id: Int): Book? {
        return bookDao.getBookById(id)
    }

}