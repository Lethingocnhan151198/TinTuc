package com.example.tintc.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String password;
    private String phoneNumber;

    public User() {
    }

    public User(String fullName, String password, String phoneNumber) {
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
