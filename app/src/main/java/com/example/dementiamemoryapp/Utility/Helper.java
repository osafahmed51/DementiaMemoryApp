package com.example.dementiamemoryapp.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.dementiamemoryapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    private static ProgressDialog progressDialog;

    public static void setSnackBar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static String getFormattedDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static void startActivity(Context context, Class<?> activity){
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void showProgressDialog(Context context){
        progressDialog = new ProgressDialog(context, R.style.MyTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    public static void dismissProgressDialog(){
        progressDialog.dismiss();
    }

}
