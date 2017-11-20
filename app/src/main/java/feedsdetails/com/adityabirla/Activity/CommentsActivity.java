package feedsdetails.com.adityabirla.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

import feedsdetails.com.adityabirla.Adapter.CommentsAdapter;
import feedsdetails.com.adityabirla.Adapter.FeedAdapter;
import feedsdetails.com.adityabirla.Pojo.Comments;
import feedsdetails.com.adityabirla.Pojo.Feeds;
import feedsdetails.com.adityabirla.R;
import feedsdetails.com.adityabirla.Util.CheckInternet;
import feedsdetails.com.adityabirla.Util.Constants;

public class CommentsActivity extends AppCompatActivity {
    String file_id,user_id;
    ImageView im_send;
    EditText et_comments;
    ListView lv_comments;
    ProgressBar loader_comments;
    ArrayList<Comments> cList;
    SwipeRefreshLayout swipecomments;
    TextView blank_coment_list;
    Comments list;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        cList=new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            file_id = extras.getString("FILEID");
            // and get whatever type user account id is
        }
        user_id=CommentsActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        im_send=(ImageView)findViewById(R.id.cmntsend);
        et_comments=(EditText)findViewById(R.id.et_comments);
        lv_comments=(ListView)findViewById(R.id.lv_comments);
        swipecomments=(SwipeRefreshLayout)findViewById(R.id.swipecomments);
        loader_comments=(ProgressBar)findViewById(R.id.loader_comments);
        blank_coment_list=(TextView)findViewById(R.id.blank_coment_list);
        im_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_comments.getText().toString().trim().length()<=0){
                    Toast.makeText(CommentsActivity.this,"Please enter comments",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(CheckInternet.getNetworkConnectivityStatus(CommentsActivity.this)){
                        CommentAsyntask likeAsyntask=new CommentAsyntask();
                        likeAsyntask.execute(user_id,file_id,et_comments.getText().toString().trim());
                    }
                    else{
                        Constants.noInternetDialouge(CommentsActivity.this,"No Internet");

                    }
                }
            }
        });
        swipecomments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cList.clear();
                if(CheckInternet.getNetworkConnectivityStatus(CommentsActivity.this)) {
                    swipecomments.setRefreshing(false);
                    CommentList comentlist = new CommentList();
                    comentlist.execute(String.valueOf(file_id));
                }
                else{
                    Constants.noInternetDialouge(CommentsActivity.this,"No Internet");

                }
            }
        });

        if (CheckInternet.getNetworkConnectivityStatus(CommentsActivity.this)) {
            CommentList comentlist=new CommentList();
            comentlist.execute(String.valueOf(file_id));
        } else {
            Constants.noInternetDialouge(CommentsActivity.this, "No internet");
        }
    }
    private class CommentAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Comments Sync";
        String server_message;
        String id,username,email_address,contact_no;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _user_id = params[0];
                String _file_id = params[1];
                String _text = params[2];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINE_URL+ Constants.COMMENTS ;
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
                        .appendQueryParameter("user_id", _user_id)
                        .appendQueryParameter("file_id", _file_id)
                        .appendQueryParameter("comment", _text);

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
                 "res": {
                 "message": "File liked has been saved.",
                 "status": 1
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1) {
                        server_message="Success";
                    } else {
                        server_message = "Error";
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
            Toast.makeText(CommentsActivity.this,server_message,Toast.LENGTH_SHORT).show();
            if(server_status==1){
                et_comments.setText("");
                CommentList comentlist = new CommentList();
                comentlist.execute(String.valueOf(file_id));
            }

        }
    }

/*
  * GET FEED LIST ASYNTASK
*/
    private class CommentList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
    int server_status;
    String server_message;

        @Override
        protected void onPreExecute() {
            loader_comments.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _file_id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.COMMENT_LIST;
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
                        .appendQueryParameter("file_id", _file_id);

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

/*
*
* {
    "Comments": [
        {
            "user_name": "am",
            "user_id": 1,
            "file_id": 21,
            "comment": "demo",
            "date": "18-11-2017 06:01 PM"
        },
        {
            "user_name": "am",
            "user_id": 1,
            "file_id": 21,
            "comment": "hello",
            "date": "19-11-2017 02:40 AM"
        },
* */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray feedListArray = res.getJSONArray("Comments");
                    if(feedListArray.length()<=0){
                        server_status=0;
                        server_message="No Comments";
                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < feedListArray.length(); i++) {
                            JSONObject o_list_obj = feedListArray.getJSONObject(i);
                            String user_name = o_list_obj.getString("user_name");
                            String user_id = o_list_obj.getString("user_id");
                            String file_id = o_list_obj.getString("file_id");
                            String comment = o_list_obj.getString("comment");
                            String date = o_list_obj.getString("date");
                            list = new Comments(user_name,user_id,file_id,comment,date);
                            cList.add(list);
                        }
                    }



                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1) {
                // Collections.reverse(fList);
                commentsAdapter = new CommentsAdapter(CommentsActivity.this, cList);
                lv_comments.setAdapter(commentsAdapter);
                lv_comments.setSelection(cList.size()-1);

            }
            else{
                swipecomments.setVisibility(View.GONE);
                blank_coment_list.setVisibility(View.VISIBLE);
            }
            loader_comments.setVisibility(View.GONE);


        }
    }

}
