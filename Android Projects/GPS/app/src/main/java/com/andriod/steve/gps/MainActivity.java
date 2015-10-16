package com.andriod.steve.gps;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity implements LocationListener {
    private static final String[] A = {"invalid", "n/a", "fine", "coarse"};
    private static final String[] P = {"invalid", "n/a", "low", "medium",
            "high"};
    private static final String[] S = {"out of service",
            "temporarily unavailable", "available"};

    private LocationManager mgr;
    private TextView output;
    private String best;
    TextView mLatitudeText;
    TextView mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        output = (TextView) findViewById(R.id.textViewLocation);

        log("Location providers:");
        dumpProviders();
        try {
            Criteria criteria = new Criteria();
            best = mgr.getBestProvider(criteria, true);
            log("\nBest provider is: " + best);

            log("\nLocations (starting with last known):");
            Location location = mgr.getLastKnownLocation(best);
            if (location != null) {
                mLatitudeText.setText(String.valueOf(location.getLatitude()));
                mLongitudeText.setText(String.valueOf(location.getLongitude()));
            } else {
                mLatitudeText.setText("unknown");
                mLongitudeText.setText("unknown");

            }
            dumpLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView mLatitudeText = (TextView) findViewById(R.id.textViewLatitude);
        TextView mLongitudeText = (TextView) findViewById(R.id.textViewLogitude);

        String address = "";
        GPSServices mGPSService = new GPSServices(getBaseContext());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getBaseContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            Toast.makeText(getBaseContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

            mLongitudeText.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            mLatitudeText.setText("Address: " + address);
        }

        Toast.makeText(getBaseContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        dumpLocation(location);
        mLatitudeText.setText(String.valueOf(location.getLatitude()));
        mLongitudeText.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        log("\nProvider status changed: " + provider + ", status="
                + S[status] + ", extras=" + extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        log("\nProvider disabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        log("\nProvider disabled: " + provider);

    }

    /**
     * Write information from all location providers
     */
    private void dumpProviders() {
        List<String> providers = mgr.getAllProviders();
        for (String provider : providers) {
            dumpProvider(provider);
        }
    }

    /**
     * Write information from a single location provider
     */
    private void dumpProvider(String provider) {
        LocationProvider info = mgr.getProvider(provider);
        StringBuilder builder = new StringBuilder();
        builder.append("LocationProvider[")
                .append("name=")
                .append(info.getName())
                .append(",enabled=")
                .append(mgr.isProviderEnabled(provider))
                .append(",getAccuracy=")
                .append(A[info.getAccuracy() + 1])
                .append(",getPowerRequirement=")
                .append(P[info.getPowerRequirement() + 1])
                .append(",hasMonetaryCost=")
                .append(info.hasMonetaryCost())
                .append(",requiresCell=")
                .append(info.requiresCell())
                .append(",requiresNetwork=")
                .append(info.requiresNetwork())
                .append(",requiresSatellite=")
                .append(info.requiresSatellite())
                .append(",supportsAltitude=")
                .append(info.supportsAltitude())
                .append(",supportsBearing=")
                .append(info.supportsBearing())
                .append(",supportsSpeed=")
                .append(info.supportsSpeed())
                .append("]\n");
        log(builder.toString());
    }

    /**
     * Describe the given location, which might be null
     */
    private void dumpLocation(Location location) {
        if (location == null)
            log("\nLocation[unknown]");
        else
            log("\n" + location.toString());
    }

    /**
     * Write a string to the output window
     */
    private void log(String string) {
        output.append(string + "\n");
    }
}