package com.example.tintc.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
    private static CheckNetwork checkNetwork;
    public static CheckNetwork getInstance(){
        if (checkNetwork ==null){
            checkNetwork = new CheckNetwork();
        }
        return checkNetwork;
    }
    private boolean isNetWorkConnected(Context context){
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // đã được kết nối tới internet
                // kết nối với dữ liệu di động với nhà mọng
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // đã được kết nối wifi thì trả về true
                    return true;
                    // ngược lại thì lấy dữ liệu di động
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

}
