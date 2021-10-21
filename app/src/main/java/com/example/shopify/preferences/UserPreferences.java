package com.example.shopify.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.shopify.model.User;

public class UserPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "alamat";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public UserPreferences(Context context) {
        this.context = context;
        /* penamaan bebas namun disini digunakan "userPreferences" */
        sharedPreferences = context.getSharedPreferences("userPreferences",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUser(int id,String name, String alamat, String username, String password){

        /* Menyimpan data login ke sharedPreferences dengan key dan value  */
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_ADDRESS,alamat);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_PASSWORD,password);

        /* Jangan lupa commit karena kalo hanya set editonya saja tidak commit akan sia-sia */
        editor.commit();
    }

    public User getUserLogin(){
        /* Mengembalikan object User untuk menampilkan data user jika user sudah login */
        String name,alamat,username,password;
        int id;

        id = sharedPreferences.getInt(KEY_ID,0);
        name = sharedPreferences.getString(KEY_NAME,null);
        alamat = sharedPreferences.getString(KEY_ADDRESS,null);
        username = sharedPreferences.getString(KEY_USERNAME,null);
        password = sharedPreferences.getString(KEY_PASSWORD,null);

        return new User(id,name,alamat,username,password);
    }

    public boolean checkLogin(){
        /* Mengembalikan nilai is_login, jika sudah login otomatis nilai true jika tidak akan return false */
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void logout(){
        /* Melakukan clear data yang ada pada sharedPreferences  , jangan lupa di commit agar data terubah*/
        editor.clear();
        editor.commit();
    }
}