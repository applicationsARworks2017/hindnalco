package feedsdetails.com.adityabirla.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

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

import feedsdetails.com.adityabirla.Pojo.Feeds;
import feedsdetails.com.adityabirla.R;
import feedsdetails.com.adityabirla.Util.CheckInternet;
import feedsdetails.com.adityabirla.Util.Constants;

public class FeedAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Feeds> new_list;
    Holder holder,holder1;
    String user_id;
    int likecount;

    public FeedAdapter(Context context, ArrayList<Feeds> fList) {
        this._context=context;
        this.new_list=fList;
    }


    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Holder{
        TextView name,time,contentheading,like_count,comment_count,share_count,download_count;
        ImageView iv_image,likeim,shareim;
        VideoView videoView;
        RelativeLayout fileview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Feeds _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_feed_adapter, parent, false);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.time=(TextView)convertView.findViewById(R.id.time);
            holder.contentheading=(TextView)convertView.findViewById(R.id.contentheading);
            holder.like_count=(TextView)convertView.findViewById(R.id.like_count);
            holder.comment_count=(TextView)convertView.findViewById(R.id.comment_count);
            holder.share_count=(TextView)convertView.findViewById(R.id.share_count);
            holder.download_count=(TextView)convertView.findViewById(R.id.download_count);
            holder.iv_image=(ImageView)convertView.findViewById(R.id.img);
            holder.likeim=(ImageView)convertView.findViewById(R.id.likeim);
            holder.shareim=(ImageView)convertView.findViewById(R.id.shareim);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(position);
        holder.time.setTag(position);
        holder.contentheading.setTag(position);
        holder.like_count.setTag(holder);
        holder.comment_count.setTag(holder);
        holder.share_count.setTag(holder);
        holder.download_count.setTag(holder);
        holder.likeim.setTag(holder);
        holder.shareim.setTag(position);
        if(_pos.getFile_type().contentEquals("image")) {
            holder.iv_image.setVisibility(View.VISIBLE);
            if (!_pos.getFile_name().isEmpty()) {
                Picasso.with(_context).load(_pos.getFile_name()).into(holder.iv_image);
            }
        }
        holder.name.setText(_pos.getUser_name());
        holder.time.setText(_pos.getDate());
        holder.contentheading.setText(_pos.getTitle());
        holder.like_count.setText(_pos.getNo_of_like());
        holder.comment_count.setText(_pos.getNo_of_comment());
        holder.share_count.setText(_pos.getNo_of_share());
        holder.download_count.setText(_pos.getNo_of_downloads());
        user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        final MediaPlayer mp = MediaPlayer.create(_context, R.raw.click_sound);
        holder.likeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                likecount=Integer.valueOf(_pos.getNo_of_like());
                mp.start();
                if(CheckInternet.getNetworkConnectivityStatus(_context)){
                    LikeAsyntask likeAsyntask=new LikeAsyntask();
                    likeAsyntask.execute(user_id,_pos.getId(),"1");
                }
                else{
                    Constants.noInternetDialouge(_context,"No Internet");

                }
            }
        });
        holder.shareim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                String message = _pos.getTitle()+" . "+"Please click to get the file : "+_pos.getFile_name();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                _context.startActivity(Intent.createChooser(share, "Share the title with file"));
                holder1=(Holder)v.getTag();
                likecount=Integer.valueOf(_pos.getNo_of_like());
                mp.start();
                if(CheckInternet.getNetworkConnectivityStatus(_context)){
                    LikeAsyntask likeAsyntask=new LikeAsyntask();
                    likeAsyntask.execute(user_id,_pos.getId(),"1");
                }
                else{
                    Constants.noInternetDialouge(_context,"No Internet");

                }
            }
        });


        return convertView;
    }
    private class LikeAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Like Sync";
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
                String _liked = params[2];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINE_URL+ Constants.LIKE ;
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
                        .appendQueryParameter("liked", _liked);

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
                        server_message="Liked";
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
            Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
            holder1.like_count.setText(String.valueOf(likecount+1));

        }
    }
}
