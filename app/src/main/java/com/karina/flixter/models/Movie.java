package com.karina.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    int movieId;
    String backdropPath; //that's the type of image for a landscape mode
    String posterPath; //property now it's empty
    String title;
    String overview;
    double rating;

    // empty constructor needed by a Parcel Library
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path"); //property = "to the value of jsOnObject posterPath
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
    }
        //Now we gonna take in our JSONArray and return a list of movies.
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>(); // [Movie, Movie, Movie, Movie]
        for (int i = 0; i < movieJsonArray.length(); i++) {
            /*That method's iterating through JSONArray and constructing a movie for each element in this
            JSONArray. We  will add a movie at each position of the array. So we are calling the constructor that we
            just drove above.
            * JsonArray [JsonObject, JsonObject, jsonObject] ->  ArrayList[Movie, Movie, Movie]
            *              0           1              2
            *JsonObject: {"popularity":133.519,"vote_count":7927,"video":false,"poster_path":"\/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg","id":496243,"adult":false,"backdrop_path":"\/ApiBzeaa95TNYliSbQ8pJv4Fje7.jpg","original_language":"ko","original_title":"기생충","genre_ids":[35,18,53],"title":"Parasite","vote_average":8.5,"overview":"All unemployed, Ki-taek's family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.","release_date":"2019-05-30"}
            *
            * movies.add: we are adding smth to array
            * new Movie() : we are creating a new movie class but we have to pass a jsonObject in its constructor
            * movieJsonArray.getJsonObject(i) -> JsonObject
            *
            * */

            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies; //[Movie, Movie, Movie, Movie]
    }
            /*We are able to construct with the objects but we are actually want to be able to get data
            out of these objects
            * So right click -> Generate -> Getters (all fields that we have in out movie object)*/
    public String getPosterPath() {
        /*Now we have to make out poster_path usable. The poster_path that we get currently is actually
        a relative path, so that is not a full URL that we can use to fetch the image data.
        We should make an API request to the configurations API (in "Hints" section) figuring out
        what sized of images are available. Appending that to the base URL and then appending the relative
        path that you get back from the movie now playing API.
        So now we'll just copy and paste the URL and hard-code the size that we want (width of 342)*/

        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
        //%s is basically saying here's what i want to replace with posterPath
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public int getMovieId() {
        return movieId;
    }
}
