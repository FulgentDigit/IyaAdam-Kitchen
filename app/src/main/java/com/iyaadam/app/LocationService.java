package com.iyaadam.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

public class LocationService implements LocationListener {
    private LocationManager locationManager;
    private Location currentLocation;
    private static LocationService instance;

    private LocationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationService getInstance(Context context) {
        if (instance == null) {
            instance = new LocationService(context);
        }
        return instance;
    }

    public void startLocationUpdates(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public String getLocationString() {
        if (currentLocation != null) {
            return currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        }
        return "Location not available";
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
