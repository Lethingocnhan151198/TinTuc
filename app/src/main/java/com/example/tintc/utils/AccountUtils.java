package com.example.tintc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tintc.config.Constant;
import com.example.tintc.model.User;
import com.google.gson.Gson;

public class AccountUtils {
    private static AccountUtils accountUtils;
    private SharedPreferences sharedPreferences;
    public  static  AccountUtils getInstance(Context context){
        if (accountUtils == null){
            accountUtils = new AccountUtils(context);
        }
        return accountUtils;
    }
    private AccountUtils(Context context){
        sharedPreferences = context.getSharedPreferences(Constant.SHARE_PRE_NAME,Context.MODE_PRIVATE);
    }
    public void setUser(User user){
        sharedPreferences.edit()
                .putString(Constant.KEY_PUT_DATA,user.toString())
                .apply();

        // còn commit hoạt động theo cơ chế đồng bộ Synchronous , nếu có 2 edit thì nó sẽ thực hiện tuần tự
        // và báo kết quả về
    }
    public User getUser(){
        String data = sharedPreferences.getString(Constant.KEY_PUT_DATA,null);
        if (data ==null){
            return null;
        }
        return new Gson().fromJson(data,User.class);
    }
    public void logout(){
        sharedPreferences.edit()
                .clear()
                .apply();
    }
}
