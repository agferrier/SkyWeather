package uk.co.agfsoft.skyweather.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.agfsoft.skyweather.R;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<WeatherCity> favourites;
    private FavouritesPresenter favouritesPresenter;

    public void setFavourites(List<WeatherCity> favourites) {
        this.favourites = favourites;
    }

    public FavouriteAdapter(List<WeatherCity> favourites, FavouritesPresenter favouritesPresenter) {

        this.favourites = favourites;
        this.favouritesPresenter = favouritesPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_item_favourite, parent, false);
        return new ViewHolder(itemView, favouritesPresenter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherCity weatherCity = favourites.get(position);
        holder.updateContent(weatherCity);

    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    protected static class ViewHolder  extends RecyclerView.ViewHolder {

        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;
        @BindView(R.id.favouriteLayout)
        View favouriteLayout;
        Context context;
        private FavouritesPresenter favouritesPresenter;
        private WeatherCity weatherCity;

        public ViewHolder(View itemView, FavouritesPresenter favouritesPresenter) {
            super(itemView);
            this.favouritesPresenter = favouritesPresenter;
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void updateContent(WeatherCity weatherCity) {
            this.weatherCity = weatherCity;
            descriptionTextView.setText(context.getString(R.string.favourite_description_format, weatherCity.getName(), weatherCity.getCountry()));
        }

        @OnClick(R.id.favouriteLayout)
        public void onFavouriteClicked() {
            favouritesPresenter.favouriteClicked(weatherCity);
        }
    }
}
