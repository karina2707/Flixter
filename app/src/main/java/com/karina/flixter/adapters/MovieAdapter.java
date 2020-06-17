package com.karina.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karina.flixter.R;
import com.karina.flixter.models.Movie;

import java.util.List;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //First thing to do is to create an inner ViewHolder to render the item.
    //When it will complain click "create a constructor matching super"
    //that is an abstract  class, which means that there are methods, that you need to fill out when you're extending this
    //there are some pieces of key data that we need in order to fill these out
    Context context; // so that we can actually inflate a view which we'll use so we need to have where this adapter is being constructed from a context
    List<Movie> movies; //actual data
    //Generate -> Constructor

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }
    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        Log.d("MovieAdapter", "onCreateViewHolder");
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder is a representation of our row in the RecyclerView
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //so here we define where are they coming from
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            //here we use the getter method on our movie and populate each of the views.
            //so android does't have an inbuilt way to render remote images, so we use library
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            //After passing the context you need to load the URL, then you gonna load it into a particular view
            String imageURL;
            //if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //then imageURL = backdrop image
                imageURL = movie.getBackdropPath();
            } else {
                //else = poster image
                imageURL = movie.getPosterPath();}
                Glide.with(context).load(imageURL).into(ivPoster);

            }
        }
    }
