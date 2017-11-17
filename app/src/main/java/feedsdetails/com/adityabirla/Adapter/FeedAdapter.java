package feedsdetails.com.adityabirla.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import feedsdetails.com.adityabirla.Activity.VideoPlayer;
import feedsdetails.com.adityabirla.Pojo.Feeds;
import feedsdetails.com.adityabirla.R;
import feedsdetails.com.adityabirla.Util.CheckInternet;
import feedsdetails.com.adityabirla.Util.Constants;

import static android.app.Activity.RESULT_OK;

public class FeedAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Feeds> new_list;
    Holder holder,holder1;
    String user_id;
    int likecount,sharecount,videocount,doccount;
    MediaController mediaC;
    DisplayMetrics dm;
    String new_wordd;
    String targetFileName;
    String filetype;
    String file_type;
    String IPATH=null;
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
        ImageView iv_image,likeim,shareim,im_download,im_commnet;
        RelativeLayout videoView;
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
            holder.im_download=(ImageView)convertView.findViewById(R.id.download);
            holder.im_commnet=(ImageView)convertView.findViewById(R.id.comment);
            holder.videoView=(RelativeLayout) convertView.findViewById(R.id.video);
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
        holder.shareim.setTag(holder);
        holder.im_commnet.setTag(holder);
        holder.im_download.setTag(holder);
        holder.videoView.setTag(position);
        holder.iv_image.setTag(position);
        if(_pos.getFile_type().contentEquals("image")) {
            holder.iv_image.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            if (!_pos.getFile_name().isEmpty()) {
                Picasso.with(_context).load(_pos.getFile_name()).into(holder.iv_image);
            }
        }
        if(_pos.getFile_type().contentEquals("video")){
            holder.iv_image.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent=new Intent(_context,VideoPlayer.class);
                    intent.putExtra("PATH",_pos.getFile_name());
                    _context.startActivity(intent);*/
                }
            });
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
                holder1=(Holder)v.getTag();
                mp.start();
                String message = _pos.getTitle()+" . "+"Please click to get the file : "+_pos.getFile_name();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                _context.startActivity(Intent.createChooser(share, "Share the title with file"));
                sharecount=Integer.valueOf(_pos.getNo_of_share());
                if(CheckInternet.getNetworkConnectivityStatus(_context)){
                    ShareAsyntask shareAsyntask=new ShareAsyntask();
                    shareAsyntask.execute(user_id,_pos.getId(),"1");
                }
                else{
                    Constants.noInternetDialouge(_context,"No Internet");

                }
            }
        });
        holder.im_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                file_type=_pos.getFile_type();
                videocount=Integer.valueOf(_pos.getNo_of_downloads());
                new_wordd = _pos.getFile_name().substring(_pos.getFile_name().length() - 5);
                if(CheckInternet.getNetworkConnectivityStatus(_context)){
                    DownloadAsyntask downloadAsyntask=new DownloadAsyntask();
                    downloadAsyntask.execute(_pos.getFile_name());
                    DcountAsyntask shareAsyntask=new DcountAsyntask();
                    shareAsyntask.execute(user_id,_pos.getId(),"1");
                }
                else{
                    Constants.noInternetDialouge(_context,"No Internet");

                }
            }
        });
        holder.im_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                mp.start();

            }
        });

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,VideoPlayer.class);
                intent.putExtra("PATH",_pos.getFile_name());
                _context.startActivity(intent);
            }
        });



        return convertView;
    }
    private class DownloadAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Download";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(_context,
                    "Wait", "Downloading . . .");

        }

        @Override
        protected Void doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                if(file_type.contentEquals("image")){
                    targetFileName=getSaltString()+".jpg";
                }
                if(file_type.contentEquals("video")){
                    targetFileName=getSaltString()+".mp4";
                }                int lenghtOfFile = conexion.getContentLength();
                IPATH = Environment.getExternalStorageDirectory() + "/" + "hindalco" + "/";
                File folder = new File(IPATH);
                if (!folder.exists()) {
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(IPATH + targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("Async-Example", "onPostExecute Called");

            progress.dismiss();
            Toast.makeText(_context, "Downnloaded Successfully", Toast.LENGTH_LONG).show();
        }
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    private class LikeAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Share Sync";
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
    private class DcountAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Share Sync";
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

                String link =Constants.ONLINE_URL+ Constants.DOWNLOADS ;
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
                        .appendQueryParameter("downloaded", _liked);

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
            holder1.download_count.setText(String.valueOf(videocount+1));

        }
    }
    private class ShareAsyntask extends AsyncTask<String, Void, Void> {

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

                String link =Constants.ONLINE_URL+ Constants.SHARE ;
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
                        .appendQueryParameter("shared", _liked);

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
                 "message": "File shared has been saved.",
                 "status": 1
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1) {
                        server_message="SHARED";
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
           // Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
            holder1.share_count.setText(String.valueOf(sharecount+1));

        }
    }
}
