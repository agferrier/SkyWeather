package uk.co.agfsoft.skyweather.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import uk.co.agfsoft.skyweather.R;
import uk.co.agfsoft.skyweather.dagger.ActivityComponent;
import uk.co.agfsoft.skyweather.dagger.ActivityModule;
import uk.co.agfsoft.skyweather.dagger.InjectableActivity;
import uk.co.agfsoft.skyweather.dagger.InjectableApplication;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter;
import uk.co.agfsoft.skyweather.view.adapter.AddFavouriteAdapter;

public class AddFavouriteActivity extends AppCompatActivity implements InjectableActivity, AddFavouriteView {

    private ActivityComponent activityComponent;
    private AddFavouriteAdapter addFavouriteAdapter;

    @BindView(R.id.search_view)
    EditText searchView;
    @BindView(R.id.resultRecyclerView)
    RecyclerView resultRecyclerView;
    @BindView(R.id.progressLayout)
    ViewGroup progressLayout;

    @Inject
    AddFavouritePresenter addFavouritePresenter;


    //<editor-fold desc="Lifecycle methods">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourite);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivityComponent().inject(this);
        initialiseViews();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Event handlers">
    @OnEditorAction(R.id.search_view)
    public boolean onSearchViewAction(EditText view, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(searchView);
            resultRecyclerView.setVisibility(View.GONE);
            progressLayout.setVisibility(View.VISIBLE);
            addFavouritePresenter.startSearch(view.getText().toString());
            return true;
        } else {
            return false;
        }

    }
    //</editor-fold>

    //<editor-fold desc="AddFavouriteView implementation">
    @Override
    public void updateResults(List<WeatherCity> response) {
        resultRecyclerView.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        addFavouriteAdapter.setWeatherCities(response);
        addFavouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        progressLayout.setVisibility(View.GONE);
        resultRecyclerView.setVisibility(View.GONE);
        new AlertDialog.Builder(this).setTitle("Network Error").setMessage("Sorry - we are unable to complete your request at this time").create().show();
    }

    @Override
    public void dismiss() {
        finish();
    }
    //</editor-fold>

    //<editor-fold desc="InjectableActivity implementation">
    @Override
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ((InjectableApplication) getApplication())
                    .getApplicationComponent()
                    .plus(new ActivityModule(this));
        }
        return activityComponent;

    }
    //</editor-fold>

    //<editor-fold desc="Service routines">
    private void initialiseViews() {
        initialiseRecyclerView();
        progressLayout.setVisibility(View.GONE);
        resultRecyclerView.setVisibility(View.GONE);
    }

    private void initialiseRecyclerView() {
        addFavouriteAdapter = new AddFavouriteAdapter(addFavouritePresenter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setAdapter(addFavouriteAdapter);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
    //</editor-fold>


}
