package com.nlefler.foursquarewear;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.nlefler.nlfoursquare.Model.FoursquareResponse.NLFoursquareResponse;
import com.nlefler.nlfoursquare.Model.NLFoursquareClientParameters;
import com.nlefler.nlfoursquare.Model.Venue.NLFoursquareVenue;
import com.nlefler.nlfoursquare.Model.Venue.NLFoursquareVenueSearchResponse;
import com.nlefler.nlfoursquare.Search.NLFoursquareVenueSearch;
import com.nlefler.nlfoursquare.Search.NLFoursquareVenueSearchParametersBuilder;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.QueryMap;


public class FoursquareWearActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foursquare_wear);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.foursquare_wear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String EXTRA_NOOP = "noop";
        public static final String ACTION_OEPN = "com.nlefler.foursquarewear.action.open";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_foursquare_wear, container, false);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            showNotification();
        }

        @Override
        public void onPause() {
            NotificationManagerCompat.from(getActivity()).cancel(0);
            super.onPause();
        }

        private void showNotification() {


            NLFoursquareClientParameters clientParameters = new NLFoursquareClientParameters(
                    getString(R.string.foursq_client_id),
                    getString(R.string.foursq_client_secret)
            );
            NLFoursquareVenueSearchParametersBuilder paramsBuilder = new NLFoursquareVenueSearchParametersBuilder();
            paramsBuilder.latLon(40.705622, -74.013584);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.foursquare.com/v2")
                    .build();
            NLFoursquareVenueSearch searchEndpoint = restAdapter.create(NLFoursquareVenueSearch.class);
            searchEndpoint.search(paramsBuilder.buildWithClientParameters(clientParameters),
                    new Callback<NLFoursquareResponse<NLFoursquareVenueSearchResponse>>() {
                        @Override
                        public void success(NLFoursquareResponse<NLFoursquareVenueSearchResponse> foursquareResponse,
                                            Response response) {
                            NLFoursquareVenue firstVenue = foursquareResponse.response.venues.get(0);
                            showNotification(firstVenue.name, firstVenue.location.address);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            showNotification("Error", "Error");
                        }
                    });


        }

        private void showNotification(String title, String text) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                    .setContentTitle(title)
                    .setContentText(text);
            Intent intent = new Intent(ACTION_OEPN);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
            Notification notification = new WearableNotifications.Builder(builder)
                    .setMinPriority()
                    .build();
            NotificationManagerCompat.from(getActivity()).notify(0, notification);
        }
    }
}
