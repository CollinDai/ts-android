package com.lochbridge.peike.demo.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.model.Subtitle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
    private static final String LOGTAG = "NetworkManager";
    private static final String HOSTNAME = "http://aqueous-falls-1653.herokuapp.com/";
    private static final String TOPTEN_URL = HOSTNAME + "movie/";
    private static final String SEARCH_URL = HOSTNAME + "movie/search";
    private static final String SUBTITLE_URL = HOSTNAME + "subtitle/";
    private static final String SUBTITLE_DOWNLOAD_URL = SUBTITLE_URL + "download/";
    private static final int SOCKET_TIMEOUT_MS = 9999;
    private static final int SOCKET_RETRY = 2;

    public static void topTen(Context context, final Callback<List<Movie>> callback) {
        JsonArrayRequest request = new JsonArrayRequest(TOPTEN_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOGTAG, "Raw reseponse: " + response.toString());
                List<Movie> result = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        Movie movie = new Movie();
                        JSONObject movieResp = response.getJSONObject(i);
                        movie.title = movieResp.getString("title");
                        movie.imdbId = movieResp.getString("imdb_id");
                        movie.posterUrl = movieResp.getString("poster_url");
                        movie.backdropUrl = movieResp.getString("backdrop_url");
                        movie.tomatoRating = movieResp.getString("tomato_meter");
                        movie.imdbRating = movieResp.getString("imdb_rating");
                        result.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                SOCKET_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    public static void setPoster(Context context, NetworkImageView imageView, String imgUrl) {
        Log.d(LOGTAG, "Get thumbnail from: " + imgUrl);
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageView.setImageUrl(imgUrl, imageLoader);
    }

    public static void search(Context context, String movieName, final Callback<List<Movie>> callback) {
        String url = SEARCH_URL + "?query=" + buildQuery(movieName);

        Log.d(LOGTAG, "Search: " + movieName);
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOGTAG, "Raw reseponse: " + response.toString());
                List<Movie> result = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        Movie movie = new Movie();
                        JSONObject jsonObject = response.getJSONObject(i);
                        movie.title = jsonObject.getString("title");
                        movie.imdbId = jsonObject.getString("imdb_id");
                        movie.posterUrl = jsonObject.getString("poster_url");
                        movie.backdropUrl = jsonObject.getString("backdrop_url");
                        movie.imdbRating = jsonObject.getString("imdb_rating");
                        movie.tomatoRating = jsonObject.getString("tomato_rating");
                        result.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static String buildQuery(String query) {
        query = query.trim();
        query = query.replaceAll(" +", "+");
        return query;
    }

    public static void searchSubtitle(Context context, String imdbId, List<String> languages, final Callback<List<Subtitle>> callback) {
        String langQuery = "";
        if (languages != null && !languages.isEmpty()) {
            langQuery = buildLangQuery(languages);
        }
        String searchSubUrl = SUBTITLE_URL + imdbId + langQuery;
        Log.d(LOGTAG, "sub url: " + searchSubUrl);
        JsonArrayRequest request = new JsonArrayRequest(searchSubUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOGTAG, "Raw reseponse: " + response.toString());
                List<Subtitle> result = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        Subtitle subtitle = new Subtitle();
                        JSONObject subResp = response.getJSONObject(i);
                        subtitle.imdbId = subResp.getString("imdb_id");
                        subtitle.fileId = subResp.getInt("file_id");
                        subtitle.fileName = subResp.getString("file_name");
                        subtitle.fileSize = subResp.getInt("file_size");
                        subtitle.duration = subResp.getString("duration");
                        subtitle.downloadCount = subResp.getInt("download_count");
                        subtitle.language = subResp.getString("language");
                        subtitle.iso639 = subResp.getString("ISO639");
                        subtitle.addDate = subResp.getString("add_date");
                        result.add(subtitle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void download(final Context context, int fileId, final Callback<String> callback) {
        String url = SUBTITLE_DOWNLOAD_URL + fileId;
        Log.d(LOGTAG, "Download subtitle from: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Download Success!", Toast.LENGTH_SHORT).show();
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler(error);
                        Toast.makeText(context, "Download Fail!", Toast.LENGTH_SHORT).show();
                        callback.onResponse(null);
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private static void errorHandler(VolleyError error) {
        error.printStackTrace();
    }

    private static String buildLangQuery(List<String> languages) {
        String query = "";
        for (int i = 0; i < languages.size(); ++i) {
            query += i == 0 ? "?lang=" : "&";
            query += languages.get(i);
        }
        return query;
    }

    public interface Callback<T> {
        void onResponse(T t);
    }
}
