package budi.dapuraco;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public Context context;
    public SharedPreferences.Editor editor;
    int PRIVATE_MODE=1;

    private static final String PREF_NAMA="LOGIN";
    private static final String LOGIN="IS_LOGIN";
    public static final String NAMA="NAMA";
    public static final String EMAIL="EMAIL";
    public static final String ID="ID";

    @SuppressLint("WrongConstant")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NAMA, context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void createsession(String nama, String email,String id){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAMA,nama);
        editor.putString(EMAIL,email);
        editor.putString(ID,id);
        editor.apply();
    }
    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }
    public void checklogin(){
        if (!this.isLogin()){
            Intent i=new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity)context).finish();
        }
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user= new HashMap<>();
        user.put(NAMA,sharedPreferences.getString(NAMA,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(ID,sharedPreferences.getString(ID,null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        //checklogin();
        //Intent i=new Intent(context, LoginActivity.class);
        //context.startActivity(i);
        //((EditUserActivity)context).finish();
    }



}
