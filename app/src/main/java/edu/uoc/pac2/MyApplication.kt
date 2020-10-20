package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    //private lateinit var booksInteractor: BooksInteractor
    override fun onCreate() {
        super.onCreate()
        // inicializando room
        //val db =  Room.databaseBuilder(applicationContext, ApplicationDatabase::class.java, "database-name").build()
        // inicializaondo booksInteractor
        //booksInteractor = BooksInteractor(db.bookDao())
        BooksRoomManager.initiate(this) //he cambiado a esto,
        //así cade vez que se recrea la aplicación nos va a actualizar el BooksRoomManager
        //no se me ocurre de otra manera
    }

//    fun getBooksInteractor(): BooksInteractor {
//        return booksInteractor
//    }

    fun hasInternetConnection(): Boolean {
        var isConnected: Boolean = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Esta deprecated
//        val activeNetwork  = cm.activeNetworkInfo
//        isConnected = activeNetwork?.isConnectedOrConnecting == true

        //Segun la documentación hay que usar el networkcallback para comprobar si tiene internet
//        val networkRequest = NetworkRequest.Builder()
//                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                .build()
//
//        cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                isConnected = true
//                Toast.makeText(this@BookListActivity, "conectado", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                isConnected = false
//                Toast.makeText(this@BookListActivity, "desconectado", Toast.LENGTH_SHORT).show()
//            }
//        })
        //Pero no necesito tener un listener escuchando si tengo conexión

        //He comprobado que lo siguiente funciona
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        networkCapabilities?.run {
            isConnected = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            //compruebo si estan conectados con wifi o con internet movil o con ethernet
        }
        return isConnected
    }
}