package com.example.tintc.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tintc.R;
import com.example.tintc.model.User;
import com.example.tintc.utils.CheckNetwork;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtName, edtSdtRegister, edtPasswordRegister, edtRePasswordRegister;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setUpToolbar();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        edtName = findViewById(R.id.edtName);
        edtSdtRegister = findViewById(R.id.edtSdtRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtRePasswordRegister = findViewById(R.id.edtRePasswordRegister);
    }

    public void ClickResgister(View view) {
        final String fullname = edtName.getText().toString();
        final String sdt      = edtSdtRegister.getText().toString();
        final String pass     = edtPasswordRegister.getText().toString();
        final String repass   = edtRePasswordRegister.getText().toString();
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Tên không được bỏ trống !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(sdt)){
            Toast.makeText(this, "Số điện thoại không được bỏ trống !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Mật khẩu không được bỏ trống !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(repass)){
            Toast.makeText(this, "Nhập lại mật khẩu !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(repass)){
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference()
                .child("Account")
                .child(sdt)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            registerAccount();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void registerAccount() {
        User user = new User();
        user.setFullName(edtName.getText().toString());
        user.setPhoneNumber(edtSdtRegister.getText().toString());
        user.setPassword(edtPasswordRegister.getText().toString());

        FirebaseDatabase.getInstance().getReference()
                .child("Account")
                .child(edtSdtRegister.getText().toString())
                .setValue(user, (databaseError, databaseReference) -> {
                    if(databaseError == null){
                        Toast.makeText(this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Đăng ký thất bại !", Toast.LENGTH_SHORT).show();
                        if(checkWifi(this)){
                            Toast.makeText(this, "Kiểm tra lại wifi !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        finish();
               
    }
    public static boolean checkWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
