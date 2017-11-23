package feedsdetails.com.adityabirla.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import feedsdetails.com.adityabirla.Pojo.User;
import feedsdetails.com.adityabirla.R;
import feedsdetails.com.adityabirla.Util.CheckInternet;
import feedsdetails.com.adityabirla.Util.Constants;

import static feedsdetails.com.adityabirla.Util.Constants.hasPermissions;

public class Login_Activity extends AppCompatActivity {
    Button lin_signin;
    RelativeLayout rel_login;
    EditText et_username,et_password;
    ArrayList<User> userlist;
    ProgressBar loader_login;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.et_password);
        lin_signin=(Button) findViewById(R.id.lin_signin);
        rel_login=(RelativeLayout)findViewById(R.id.rel_login);
        loader_login=(ProgressBar)findViewById(R.id.loader_login);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(this, PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

            }
        }

        lin_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckInternet.getNetworkConnectivityStatus(Login_Activity.this)){
                    checkValidation();
                }
                else{
                    Constants.noInternetDialouge(Login_Activity.this,"No Internet");
                }
                /*Intent intent=new Intent(Login_Activity.this,HomeActivity.class);
                startActivity(intent);*/
            }
        });
    }

    private void checkValidation() {
        if(et_username.getText().toString().trim().length()<=0){
            showSnackBar("Please enter Username");
        }
        else if (et_password.getText().toString().trim().length()<=0){
            showSnackBar("Please enter password");
        }
        else{
            LoginUserAsyntask loginuser=new LoginUserAsyntask();
            loginuser.execute(et_username.getText().toString().trim(),et_password.getText().toString().trim());
        }
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(rel_login, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#C62828"));

        snackbar.show();
    }
    private class LoginUserAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        String id,username,email_address,contact_no,full_name;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_login.setVisibility(View.VISIBLE);
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String user_name = params[0];
                String password = params[1];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINE_URL+ Constants.LOGIN ;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", user_name)
                        .appendQueryParameter("password", password);

                //.appendQueryParameter("deviceid", deviceid);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /**
                 * {
                 {
                 "users": {
                 "id": 2,
                 "username": "avinash",
                 "email_address": "avinash@gmail.com",
                 "contact_no": 2147483647,
                 "created": "2017-11-11T00:00:00+00:00",
                 "modified": "2017-11-11T00:00:00+00:00",
                 "message": "user available.",
                 "status": 1
                 }
                 }
                 * */
                userlist=new ArrayList<>();

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("users");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1) {
                        id=j_obj.getString("id");
                    username=j_obj.getString("username");
                    email_address=j_obj.getString("email_address");
                    contact_no=j_obj.getString("contact_no");
                    full_name=j_obj.getString("full_name");
                    User user_list=new User(id,username,email_address,contact_no,full_name);
                    userlist.add(user_list);
                    } else {
                        server_message = "Invalid Credentials";
                    }
                }
                return null;
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            loader_login.setVisibility(View.GONE);
            if (server_status == 1) {
                SharedPreferences sharedPreferences = Login_Activity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.MOBILE, contact_no);
                editor.putString(Constants.NAME, username);
                editor.putString(Constants.FULLNAME, full_name);
                editor.commit();
                Intent i=new Intent(Login_Activity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            else{
                showSnackBar(server_message);

            }
        }
    }
}
