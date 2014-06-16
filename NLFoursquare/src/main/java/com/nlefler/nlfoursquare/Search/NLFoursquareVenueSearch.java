package com.nlefler.nlfoursquare.Search;

import com.nlefler.nlfoursquare.Model.FoursquareResponse.NLFoursquareResponse;
import com.nlefler.nlfoursquare.Model.Venue.NLFoursquareVenue;
import com.nlefler.nlfoursquare.Model.Venue.NLFoursquareVenueSearchResponse;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Nathan Lefler on 5/19/14.
 */
public interface NLFoursquareVenueSearch {
    @GET("/venues/search")
    void search(@QueryMap Map<String, String> parameters,
                Callback<NLFoursquareResponse<NLFoursquareVenueSearchResponse>> callback);
}
