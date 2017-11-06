package uk.co.agfsoft.skyweather.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.agfsoft.skyweather.R;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter;



public class AddFavouriteAdapter extends RecyclerView.Adapter<AddFavouriteAdapter.ViewHolder> {
    private List<WeatherCity> weatherCities;
    private AddFavouritePresenter addFavouritePresenter;

    public AddFavouriteAdapter(AddFavouritePresenter addFavouritePresenter) {
        this.addFavouritePresenter = addFavouritePresenter;
        weatherCities = new ArrayList<WeatherCity>(0);
    }

    @Override
    public AddFavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_item_add_favourite, parent, false);
        return new AddFavouriteAdapter.ViewHolder(itemView, addFavouritePresenter);
    }

    @Override
    public void onBindViewHolder(AddFavouriteAdapter.ViewHolder holder, int position) {
        WeatherCity weatherCity = weatherCities.get(position);
        holder.updateContent(weatherCity);

    }

    @Override
    public int getItemCount() {
        return weatherCities.size();
    }

    public void setWeatherCities(List<WeatherCity> weatherCities) {
        this.weatherCities = weatherCities;
    }

    protected static class ViewHolder  extends RecyclerView.ViewHolder {

        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;
        @BindView(R.id.latLongTextView)
        TextView latlongTextView;

        Context context;
        private AddFavouritePresenter addFavouritePresenter;
        private WeatherCity weatherCity;

        public ViewHolder(View itemView, AddFavouritePresenter addFavouritePresenter) {
            super(itemView);
            this.addFavouritePresenter = addFavouritePresenter;
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void updateContent(WeatherCity weatherCity) {
            this.weatherCity = weatherCity;
            descriptionTextView.setText(context.getString(R.string.favourite_description_format, weatherCity.getName(), weatherCity.getCountry()));
            latlongTextView.setText(context.getString(R.string.lat_long_format, weatherCity.getLatitiude(), weatherCity.getLongitude()));
        }

        @OnClick(R.id.selectButton)
        public void onCitySelected() {
            addFavouritePresenter.citySelected(weatherCity);
        }
    }

}
