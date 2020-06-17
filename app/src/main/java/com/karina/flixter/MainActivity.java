package com.karina.flixter;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;
        import android.util.Log;

        import com.codepath.asynchttpclient.AsyncHttpClient;
        import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
        import com.karina.flixter.adapters.MovieAdapter;
        import com.karina.flixter.models.Movie;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

        import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    //Here we actually embedding the API key in the URL itself, so there's no need to pass that in separately
    //to requests parameters
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    //In order to easily log data we create the following tag
    public static final String TAG = "MainActivity";

    List<Movie> movies; // [Movie, Movie, Movie, Movie]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        //Bind the adapter to the data source to populate the RecyclerView
        //First: create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);// the activity is an instance of the context

        //Set the adapter on the Recycler View
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        /*We want to make a get request on the URL to get the currently playing movies.
            So we gonna make a get request on that URL and we need to pass in a callback here: */
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            /*The reason we are doing the JsonHttpResponseHandler and not the textResponseHandler or some other one
            is because we did see that the movie database API is returning JSON. It speaks JSON which is why we
            make sure that we're using this responseHandler */
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                //Now we know that the data we want is inside that json object
                /*We can actually pull out the data. We know that it is a json object
                 json.jsonObject (then click "introduce local variable")
                 Returns to us the actual json object.*/
                JSONObject jsonObject = json.jsonObject;
                try {
                    /*Inside jsonObject we want to get a JSONArray, which is told "results".
                    Here the JSON exception is needed because you are trying to parse out your JSON, this key may not exist
                    or there might be some other issue as you're trying yo examine and parse the data in JSON.
                    It's your responsibility as a developer to handle that, which is why you need to either have a try-catch
                    or have your method threw exception*/
                    JSONArray results = jsonObject.getJSONArray("results"); //[JsonObject, JsonObject, ...
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results)); //movies =  [Movie, Movie, Movie]
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.d(TAG, "Hit json exception", e);
                }
                //simeple comment
            }

            @Override

            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}
