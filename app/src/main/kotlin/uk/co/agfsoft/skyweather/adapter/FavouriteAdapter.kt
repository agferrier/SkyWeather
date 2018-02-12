package uk.co.agfsoft.skyweather.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.recycler_item_favourite.view.*
import uk.co.agfsoft.skyweather.R
import model.WeatherCity
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter


class FavouriteAdapter(private var favourites: List<WeatherCity>?, val favouritesPresenter: FavouritesPresenter) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    fun setFavourites(favourites: List<WeatherCity>) {
        this.favourites = favourites
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recycler_item_favourite, parent, false)
        return ViewHolder(itemView, favouritesPresenter)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherCity = favourites!![position]
        holder.updateContent(weatherCity)

    }

    override fun getItemCount(): Int {
        return favourites!!.size
    }

    class ViewHolder(itemView: View, private val favouritesPresenter: FavouritesPresenter) : RecyclerView.ViewHolder(itemView) {

        internal var context: Context
        private var weatherCity: WeatherCity? = null

        init {
            ButterKnife.bind(this, itemView)
            context = itemView.context
        }

        fun updateContent(weatherCity: WeatherCity) {
            this.weatherCity = weatherCity
            itemView.descriptionTextView!!.text = context.getString(R.string.favourite_description_format, weatherCity.name, weatherCity.country)
            itemView.favouriteLayout.setOnClickListener {
                favouritesPresenter.favouriteClicked(weatherCity)
            }

        }

    }
}
