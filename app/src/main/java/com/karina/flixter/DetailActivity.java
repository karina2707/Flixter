package com.karina.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.karina.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "X";
    //the id of movie should be passed where the %d is now. Now we have to format the string
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    YouTubePlayerView youTubePlayerView;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);
        //retrieve data corresponding to that key
        // String title = getIntent().getStringExtra("title");
        // tvTitle.setText(title);
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        //It complains because the setRating method returns float
        ratingBar.setRating((float) movie.getRating());
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        AsyncHttpClient client = new AsyncHttpClient(); //get means get request. Parameter for format method is an id. And we need ro pass a callback
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    /*Now we want to be able to extract out the key, and so what we want to do actually grab the first element of the result array and get the value
                    corresponding to key, so first thing we have to to is to check how large is that result JSONArray, but if it empty we don't want to extract any
                    data out and we should return */
                    if (results.length() == 0) {
                      /*right now we are making an assumption that the site is always youtube, but that might not always be the case
                      and so you should probably make some better handling here to just make sure that site is Youtube before we parse out the key
                      so now we have to place some image like the background image instead of returning nothing */
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubeKey);
                    initializeYoutube(youtubeKey);
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON", e);
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    private void initializeYoutube(final String youtubeKey) {
            youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    Log.d("DetailActivity", "onInitializationSuccess");
                    // do any work here to cue video, play video, etc.
                    //So as we use the key inside of listener, we have to make it final
                    youTubePlayer.cueVideo(youtubeKey);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d("DetailActivity", "onInitializationFailure");
                }
            });
        }
}


