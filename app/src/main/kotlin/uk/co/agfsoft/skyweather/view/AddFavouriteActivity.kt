package uk.co.agfsoft.skyweather.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import butterknife.ButterKnife
import butterknife.OnEditorAction
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_add_favourite.*
import uk.co.agfsoft.skyweather.R
import model.WeatherCity
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter
import uk.co.agfsoft.skyweather.utils.ContextLogger
import uk.co.agfsoft.skyweather.utils.showAlert
import uk.co.agfsoft.skyweather.adapter.AddFavouriteAdapter
import javax.inject.Inject

class AddFavouriteActivity : AppCompatActivity(), AddFavouriteView {

    lateinit private var addFavouriteAdapter: AddFavouriteAdapter

    @Inject
    lateinit internal var addFavouritePresenter: AddFavouritePresenter
    @Inject
    lateinit internal var contextLogger: ContextLogger


    //<editor-fold desc="Lifecycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        contextLogger.logContext()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favourite)
        ButterKnife.bind(this)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initialiseViews()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //</editor-fold>

    //<editor-fold desc="Event handlers">
    @OnEditorAction(R.id.searchView)
    fun onSearchViewAction(view: EditText, actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(searchView)
            resultRecyclerView.visibility = View.GONE
            progressLayout.visibility = View.VISIBLE
            addFavouritePresenter.startSearch(view.text.toString())
            return true
        } else {
            return false
        }

    }
    //</editor-fold>

    //<editor-fold desc="AddFavouriteView implementation">
    override fun updateResults(response: List<WeatherCity>) {
        resultRecyclerView.visibility = View.VISIBLE
        progressLayout.visibility = View.GONE
        addFavouriteAdapter.setWeatherCities(response)
        addFavouriteAdapter.notifyDataSetChanged()
    }

    override fun showError() {
        progressLayout.visibility = View.GONE
        resultRecyclerView.visibility = View.GONE
        showAlert(this, R.string.network_error, R.string.network_error_message)
    }

    override fun dismiss() {
        finish()
    }
    //</editor-fold>

    //<editor-fold desc="Service routines">
    private fun initialiseViews() {
        initialiseRecyclerView()
        progressLayout.visibility = View.GONE
        resultRecyclerView.visibility = View.GONE
    }

    private fun initialiseRecyclerView() {
        addFavouriteAdapter = AddFavouriteAdapter(addFavouritePresenter)
        resultRecyclerView.layoutManager = LinearLayoutManager(this)
        resultRecyclerView.adapter = addFavouriteAdapter
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
    //</editor-fold>


}
