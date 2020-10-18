package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.pac2.R

/**
 * An activity representing a single Book detail screen.
 */
class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val itemID = intent.getIntExtra(BookDetailFragment.ARG_ITEM_ID, -1)
            val fragment = BookDetailFragment.newInstance(itemID)
            supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .commit()
        }



    }

    //boton flecha tiene por defecto id = android.R.id.home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            goBackToBookListActivity()
        }
        return true
    }

    //al presionar back
    override fun onBackPressed() {
        goBackToBookListActivity()
    }

    private fun goBackToBookListActivity(){
        finish()
        overridePendingTransition(R.anim.translate_in_top, R.anim.translate_out_top)
    }

}