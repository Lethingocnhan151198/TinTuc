package com.example.tintc.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tintc.R;
import com.example.tintc.model.User;
import com.example.tintc.utils.AccountUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edtSdt, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        if (AccountUtils.getInstance(this).getUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        init();
    }

    private void init() {
        edtSdt = findViewById(R.id.edtSdt);
        edtPassword = findViewById(R.id.edtPassword);
    }

    public void clickRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void ClickLogin(View view) {
        String sdt = edtSdt.getText().toString();
        String pass = edtPassword.getText().toString();
        if (TextUtils.isEmpty(sdt)) {
            Toast.makeText(this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference()
                .child("Account")
                .child(sdt)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getPassword().equals(pass)) {
                                AccountUtils.getInstance(getApplicationContext()).setUser(user);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Mật khẩu không đúng !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
