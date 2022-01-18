# LocationManager

`LocationManager`位于`android.location`包下

> This class provides access to the system location services. These services allow applications to obtain periodic updates of the device's geographical location, or to be notified when the device enters the proximity of a given geographical location.
>
> 此类提供对系统位置服务的访问。 这些服务允许应用程序获得设备地理位置的定期更新，或者在设备进入给定地理位置附近时得到通知。



感觉这个类比较旧了，在官方网站上介绍的比较少，不过在[Using the Location Manager](https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/location/locationmanager.html)中对其有一定的介绍



**1.权限**

大致位置vs确切位置

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission. ACCESS_COARSE_LOCATION" />
```

另外如果是基于network的location provider，则需要网络权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
```



**2.获取`LocationManager`**

```java
LocationManager locationManager =
        (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
```



**3.Location Provider**

Android设备可以通过多种技术来接收位置的更新，如GPS、NETWORK

如下获取GPS provider

```java
LocationProvider provider =
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
```

也可以通过`Criteria`，来匹配provider

```java
// Retrieve a list of location providers that have fine accuracy, no monetary cost, etc
Criteria criteria = new Criteria();
criteria.setAccuracy(Criteria.ACCURACY_FINE);
criteria.setCostAllowed(false);
...
String providerName = locManager.getBestProvider(criteria, true);

// If no suitable provider is found, null is returned.
if (providerName != null) {
   ...
}
```



## 确认Location Provider 是否启用

一些Location Provider，例如GPS，是可以在Setting中进行设置的

通过`isProviderEnabled()`方法来检查当前是否启用了所需的Location Provider

如果没有启用，可以通过`ACTION_LOCATION_SOURCE_SETTINGS`触发Intent，为用户提供在设置中启用它的机会

```java
@Override
protected void onStart() {
    super.onStart();

    // This verification should be done during onStart() because the system calls
    // this method when the user returns to the activity, which ensures the desired
    // location provider is enabled each time the activity resumes from the stopped state.
    LocationManager locationManager =
            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    if (!gpsEnabled) {
        // Build an alert dialog here that requests that the user enable
        // the location services, then when the user clicks the "OK" button,
        // call enableLocationSettings()
    }
}

private void enableLocationSettings() {
    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivity(settingsIntent);
}
```



## 获取当前的位置

1.设置位置监听器

注册事件监听器，在 `onLocationChanged()`回调中得到位置改变的通知

在下面的示例代码片段中，位置侦听器设置为至少每 10 秒接收一次通知，并且如果设备移动超过 10 米。

```java
private final LocationListener listener = new LocationListener() {

    @Override
    public void onLocationChanged(Location location) {
        // A new location update is received.  Do something useful with it.  In this case,
        // we're sending the update to a handler which then updates the UI with the new
        // location.
        Message.obtain(mHandler,
                UPDATE_LATLNG,
                location.getLatitude() + ", " +
                location.getLongitude()).sendToTarget();

            ...
        }
    ...
};

mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        10000,          // 10-second interval.
        10,             // 10 meters.
        listener);
```



2.Handle Multiple Sources of Location Updates

在实践中，最好是同时注册GPS和Network Provider，所以在`onLocationChanged()`中，可能会从多个location provider接收到位置更新，这些位置更新可能会有不同的时间戳和精度等级

你需要结合逻辑来消除位置提供者的歧义，并丢弃陈旧且不太准确的更新。

```java
private static final int TWO_MINUTES = 1000 * 60 * 2;

/** Determines whether one Location reading is better than the current Location fix
  * @param location  The new Location that you want to evaluate
  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
  */
protected boolean isBetterLocation(Location location, Location currentBestLocation) {
    if (currentBestLocation == null) {
        // A new location is always better than no location
        return true;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    // If it's been more than two minutes since the current location, use the new location
    // because the user has likely moved
    if (isSignificantlyNewer) {
        return true;
    // If the new location is more than two minutes older, it must be worse
    } else if (isSignificantlyOlder) {
        return false;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
            currentBestLocation.getProvider());

    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
        return true;
    } else if (isNewer && !isLessAccurate) {
        return true;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
        return true;
    }
    return false;
}

/** Checks whether two providers are the same */
private boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }
    return provider1.equals(provider2);
}
```



3.Use `getLastKnownLocation()` Wisely

`getLastKnownLocation()`方法查询任何Location Provider先前收到的最后一次位置更新

需要知道的是，返回的位置可能是过时的。应该检查返回位置的时间戳和准确性，并确定它是否对您的应用程序有用。



4.结束位置更新

```java
protected void onStop() {
    super.onStop();
    mLocationManager.removeUpdates(listener);
}
```





## 定位位置

可以理解为地理位置反编码，即通过经纬度，反编码知道具体的位置

通过`Geocoder` API，`getFromLocation()`是一个同步方法，不应该在UI线程调用

```java
private final LocationListener listener = new LocationListener() {

    public void onLocationChanged(Location location) {
        // Bypass reverse-geocoding if the Geocoder service is not available on the
        // device. The isPresent() convenient method is only available on Gingerbread or above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
            // Since the geocoding API is synchronous and may take a while.  You don't want to lock
            // up the UI thread.  Invoking reverse geocoding in an AsyncTask.
            (new ReverseGeocodingTask(this)).execute(new Location[] {location});
        }
    }
    ...
};

// AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
// we do not want to invoke it from the UI thread.
private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
    Context mContext;

    public ReverseGeocodingTask(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Void doInBackground(Location... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        Location loc = params[0];
        List<Address> addresses = null;
        try {
            // Call the synchronous getFromLocation() method by passing in the lat/long values.
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            // Update UI field with the exception.
            Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            // Format the first line of address (if available), city, and country name.
            String addressText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
            // Update the UI via a message handler.
            Message.obtain(mHandler, UPDATE_ADDRESS, addressText).sendToTarget();
        }
        return null;
    }
}
```





