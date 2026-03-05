package com.siam.sky.core.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class  LocationHelper{
  companion object{
      fun  checkPermission(context: Context): Boolean {
          return ActivityCompat.checkSelfPermission(
              context, Manifest.permission.ACCESS_FINE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED ||
                  ActivityCompat.checkSelfPermission(
                      context, Manifest.permission.ACCESS_COARSE_LOCATION
                  ) == PackageManager.PERMISSION_GRANTED
      }

      fun isLocationEnabled(context: Context): Boolean {
          val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
          return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                  lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
      }


  }


}