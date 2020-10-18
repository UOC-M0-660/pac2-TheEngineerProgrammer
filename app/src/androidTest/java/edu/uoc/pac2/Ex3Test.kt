package edu.uoc.pac2

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import edu.uoc.pac2.data.BooksRoomManager
import edu.uoc.pac2.ui.BookListActivity
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


/**
 * Created by alex on 04/10/2020.
 */

@LargeTest
class Ex3Test {

    @get:Rule
    val activityRule = ActivityScenarioRule(BookListActivity::class.java)

    @Test
    fun listContainsBook() {
        Thread.sleep(TestData.networkWaitingMillis)
        onView(withText(TestData.book.title)).check(matches(isDisplayed()))
    }

    @Test
    fun booksInteractorReturnsBook() {
        Thread.sleep(TestData.networkWaitingMillis)
        activityRule.scenario.onActivity {
//            val interactor = (it.application as MyApplication).getBooksInteractor()
//            val signal = CountDownLatch(1)
//            var localBooks = emptyList<Book>()
//            AsyncTask.execute {
//                 localBooks = interactor.getAllBooks()
//                signal.countDown()
//            }
//            signal.await()
//            assertTrue(localBooks.contains(TestData.book))
            BooksRoomManager.getAllBooksThenDo {
                assertTrue(it.contains(TestData.book))
            }

        }
    }
}