package feedsdetails.com.adityabirla.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import feedsdetails.com.adityabirla.Adapter.FeedAdapter;
import feedsdetails.com.adityabirla.Pojo.Feeds;
import feedsdetails.com.adityabirla.R;
import feedsdetails.com.adityabirla.SplashScreen;
import feedsdetails.com.adityabirla.Util.CheckInternet;
import feedsdetails.com.adityabirla.Util.Constants;
import feedsdetails.com.adityabirla.Util.Getpath;
import feedsdetails.com.adityabirla.Util.MultipartUtility;

import static android.app.Activity.RESULT_OK;

public class FeedList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    com.github.clans.fab.FloatingActionButton add_files,add_video,add_images,add_questions;
    SwipeRefreshLayout swipe_feeds;
    ListView lv_feeds;
    TextView tv_nofeeds;
    RelativeLayout rel_file;
    ProgressBar loader_feeds,loader_file;
    String server_message;
    ArrayList<Feeds> fList;
    FeedAdapter fadapter;
    ImageView iv_selected_image;
    VideoView vv_selectedvideo;
    EditText et_contentheading,et_question;
    Button submit_file;
    String file_type,user_id,server_response;
    int server_status;
    File imageFile,videofile,doc_file;
    MediaController mediaC;
    String file_path;
    String question;
    File blanck=new File("");
    RelativeLayout rel_feedlist,activityresult;
    int page=1,firstVisibleItemCount,total;
    String scroll_allow="true";
    Feeds list1 = new Feeds();

    int FILE_REQUEST_CODE=001;
    int VIDEO_REQUEST_CODE=002;
    int IMAGE_REQUEST_CODE=003;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedList() {
        // Required empty public constructor
    }

    public static FeedList newInstance(String param1, String param2) {
        FeedList fragment = new FeedList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_feed_list, container, false);
        user_id = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        swipe_feeds=(SwipeRefreshLayout)v.findViewById(R.id.swipe_feeds);
        tv_nofeeds=(TextView)v.findViewById(R.id.tv_nofeeds);
        lv_feeds=(ListView)v.findViewById(R.id.lv_feeds);
        loader_feeds=(ProgressBar)v.findViewById(R.id.loader_feeds);
        loader_file=(ProgressBar)v.findViewById(R.id.loader_file);
        loader_feeds.setVisibility(View.GONE);
        vv_selectedvideo=(VideoView)v.findViewById(R.id.vv_selectedvideo);
        et_contentheading=(EditText)v.findViewById(R.id.et_contentheading);
        et_question=(EditText)v.findViewById(R.id.et_question);
        submit_file=(Button)v.findViewById(R.id.submit_file);
        iv_selected_image=(ImageView)v.findViewById(R.id.iv_selected_image);
        activityresult=(RelativeLayout)v.findViewById(R.id.activityresult);
        rel_feedlist=(RelativeLayout)v.findViewById(R.id.rel_feedlist);
        rel_file=(RelativeLayout)v.findViewById(R.id.rel_file);
        fList=new ArrayList<>();
        if(savedInstanceState==null) {
            if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
                getFeeds();
            } else {
                Constants.noInternetDialouge(getActivity(), "No internet");
            }
        }

        lv_feeds.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
               /* if(scroll_allow.contentEquals("true")){
                    page=page+1;
                    feedList feeds=new feedList();
                    feeds.execute(String.valueOf(page));

                }*/
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItemCount>0) {
                    if (scroll_allow.contentEquals("true") && firstVisibleItemCount == firstVisibleItem) {
                        page = page + 1;
                        total = (firstVisibleItem + visibleItemCount)-1;
                        feedList feeds = new feedList();
                        feeds.execute(String.valueOf(page));
                    }
                }

            }
        });
        swipe_feeds.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_feeds.setRefreshing(false);
                firstVisibleItemCount=0;
                total=0;
                if(CheckInternet.getNetworkConnectivityStatus(getActivity())){
                   // fList.clear();
                    getFeeds();
                }
                else{
                    Constants.noInternetDialouge(getActivity(),"No internet");
                }
            }
        });

        add_files=(com.github.clans.fab.FloatingActionButton)v.findViewById(R.id.add_files);
        add_video=(com.github.clans.fab.FloatingActionButton)v.findViewById(R.id.add_video);
        add_images=(com.github.clans.fab.FloatingActionButton)v.findViewById(R.id.add_images);
        add_questions=(com.github.clans.fab.FloatingActionButton)v.findViewById(R.id.add_questions);
        add_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword","application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                //comma-separated MIME types
                mediaChooser.setType("video/*");
                startActivityForResult(mediaChooser, VIDEO_REQUEST_CODE);
            }
        });
        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                //comma-separated MIME types
                mediaChooser.setType("image/*");
                startActivityForResult(mediaChooser, IMAGE_REQUEST_CODE);
            }
        });
        add_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_feedlist.setVisibility(View.GONE);
                activityresult.setVisibility(View.VISIBLE);
                iv_selected_image.setVisibility(View.GONE);
                vv_selectedvideo.setVisibility(View.GONE);
                et_question.setVisibility(View.VISIBLE);
                rel_file.setVisibility(View.GONE);
                file_type="question";


            }
        });
        submit_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file_type==null && file_type==""){
                    showSnackBar("Please select file ");

                }
                else if(et_contentheading.getText().toString().trim().length()<=0 && !file_type.contentEquals("question")){
                    showSnackBar("Please give the comment");

                }
                else{
                    if(file_type.contentEquals("image")) {
                        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
                            if (iv_selected_image.getDrawable() != null) {
                                Bitmap bitmap = ((BitmapDrawable) iv_selected_image.getDrawable()).getBitmap();
                                imageFile = persistImage(bitmap, "picture");
                                String title = et_contentheading.getText().toString().trim();
                                FileUpload fileUpload = new FileUpload();
                                fileUpload.execute(user_id, file_type, title);
                            }
                        } else {
                            Constants.noInternetDialouge(getActivity(), "No internet");
                        }
                    }
                    else if(file_type.contentEquals("video")){
                        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
                                String title = et_contentheading.getText().toString().trim();
                                FileUpload fileUpload = new FileUpload();
                                fileUpload.execute(user_id, file_type, title);

                        } else {
                            Constants.noInternetDialouge(getActivity(), "No internet");
                        }
                    }
                    else if(file_type.contentEquals("docs")){
                        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
                            String title = et_contentheading.getText().toString().trim();
                            FileUpload fileUpload = new FileUpload();
                            fileUpload.execute(user_id, file_type, title);

                        } else {
                            Constants.noInternetDialouge(getActivity(), "No internet");
                        }
                    }
                    else{
                        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
                            String title = et_contentheading.getText().toString().trim();
                            if(et_contentheading.getText().toString().trim().length()<=0){
                                title=" ";
                            }
                             question = et_question.getText().toString().trim();
                            FileUpload fileUpload = new FileUpload();
                            fileUpload.execute(user_id, file_type, title);
                        } else {
                            Constants.noInternetDialouge(getActivity(), "No internet");
                        }
                    }
                }
            }
        });
        return v;
    }
    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            rel_feedlist.setVisibility(View.GONE);
            activityresult.setVisibility(View.VISIBLE);
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                String imPath = Getpath.getPath(getActivity(),selectedImage);
                Bitmap perfectImage=modifyOrientation(photo,imPath);
                iv_selected_image.setImageBitmap(perfectImage);
                file_type="image";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode==VIDEO_REQUEST_CODE && resultCode == RESULT_OK && null != data){
            Uri selectedVideo = data.getData();
            file_path=Getpath.getPath(getActivity(),selectedVideo);
            rel_feedlist.setVisibility(View.GONE);
            activityresult.setVisibility(View.VISIBLE);
            iv_selected_image.setVisibility(View.GONE);
            vv_selectedvideo.setVisibility(View.VISIBLE);
            try
            {
                mediaC = new MediaController(getActivity());
                String path = data.getData().toString();
                 videofile = new File(file_path);
                vv_selectedvideo.setVideoPath(path);
                vv_selectedvideo.requestFocus();
                vv_selectedvideo.setMediaController(mediaC);
                mediaC.setAnchorView(vv_selectedvideo);
                vv_selectedvideo.start();
                file_type="video";

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
        else if(requestCode==FILE_REQUEST_CODE && resultCode == RESULT_OK && null != data){
            Uri selectedfile = data.getData();
            file_path=Getpath.getPath(getActivity(),selectedfile);
            rel_feedlist.setVisibility(View.GONE);
            activityresult.setVisibility(View.VISIBLE);
            iv_selected_image.setVisibility(View.GONE);
            rel_file.setVisibility(View.VISIBLE);
            doc_file=new File(file_path);
            file_type="docs";

        }

    }
    public String getRealPathFromURI(Uri contentUri) {
        /*Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = getActivity().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
    // for avoid rotation of image
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    private void getFeeds() {
        firstVisibleItemCount=0;
        feedList feeds=new feedList();
        feeds.execute(String.valueOf(page));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /*
* GET FEED LIST ASYNTASK*/
    private class feedList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";

        @Override
        protected void onPreExecute() {
            loader_feeds.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _page = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.FEEDS;
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
                        .appendQueryParameter("page", _page);

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
                * "files": [
        {
            "user_name": "am",
            "user_id": 1,
            "id": 1,
            "title": "test",
            "file_type": "image",
            "file_name": "http://applicationworld.net/forum/files/file15104322021303583579.jpg",
            "no_of_like": 4,
            "no_of_comment": 1,
            "no_of_share": 1,
            "no_of_downloads": 1,
            "date": "11-11-2017"
        }

    ]
    "Files": {
        "finder": "all",
        "page": 1,
        "current": 10,
        "count": 13,
        "perPage": 10,
        "prevPage": false,
        "nextPage": true,
        "pageCount": 2,
        "sort": "Files.id",
        "direction": "desc",
        "limit": null,
        "sortDefault": "Files.id",
        "directionDefault": "desc",
        "scope": null
    }
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray feedListArray = res.getJSONArray("files");
                    JSONObject count_obj=res.getJSONObject("Files");
                    scroll_allow=count_obj.getString("nextPage");
                    firstVisibleItemCount=firstVisibleItemCount+8;
                    if(feedListArray.length()<=0){
                        server_message="No Feeds Found";
                        server_status=0;

                    }
                    else{
                       // fList.clear();
                        server_status=1;
                        for (int i = 0; i < feedListArray.length(); i++) {
                            JSONObject o_list_obj = feedListArray.getJSONObject(i);
                            String user_name = o_list_obj.getString("user_name");
                            String user_id = o_list_obj.getString("user_id");
                            String id = o_list_obj.getString("id");
                            for(int j=1;j<fList.size();j++){
                                if(id.contentEquals(fList.get(j).getId())){
                                    fList.remove(j);
                                }
                            }
                            String title = o_list_obj.getString("title");
                            String file_type = o_list_obj.getString("file_type");
                            String file_name = o_list_obj.getString("file_name");
                            String no_of_like = o_list_obj.getString("no_of_like");
                            String no_of_comment = o_list_obj.getString("no_of_comment");
                            String no_of_share = o_list_obj.getString("no_of_share");
                            String no_of_downloads = o_list_obj.getString("no_of_downloads");
                            String description=o_list_obj.getString("description");
                            String date = o_list_obj.getString("date");
                            list1=new Feeds(id,user_name,user_id,title,file_type,file_name,no_of_like,no_of_comment,no_of_share,
                                                    no_of_downloads,date,description);
                            fList.add(list1);
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
                fadapter = new FeedAdapter(getContext(), fList);
                lv_feeds.setAdapter(fadapter);
                fadapter.notifyDataSetChanged();
                lv_feeds.setSelection(total);
            }
            else{
                swipe_feeds.setVisibility(View.GONE);
                tv_nofeeds.setVisibility(View.VISIBLE);
            }
            loader_feeds.setVisibility(View.GONE);


        }
    }

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(activityresult, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#C62828"));

        snackbar.show();
    }
    /**
     * File Upload
     */
    private class FileUpload extends AsyncTask<String, Void, Void> {

        String TAG = "Sign Up";
        private boolean is_success = false;

        @Override
        protected void onPreExecute() {
            loader_file.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            //requestURL = Config.API_BASE_URL + Config.POST_CITIZEN_NEWS;
            requestURL = Constants.ONLINE_URL + Constants.FILE_UPLOAD;

            try {
                String _user_id = params[0];
                String _file_type = params[1];
                String _title = params[2];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("user_id", _user_id);
                multipart.addFormField("file_type", _file_type);
                multipart.addFormField("title", _title);
                multipart.addFormField("description", question);

                // after completion of image work please enable this
                if(file_type.contentEquals("image")) {
                    if (imageFile != null) {
                        multipart.addFilePart("file_name", imageFile);
                    }
                }
                else if(file_type.contentEquals("video")){
                    if (videofile != null) {
                        multipart.addFilePart("file_name", videofile);
                    }
                }
                else if(file_type.contentEquals("docs")){
                    if (doc_file != null) {
                        multipart.addFilePart("file_name", doc_file);
                    }
                }
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);

                /*
                    "status": 1,
    "message": "Successfully inserted"(
                * */

                if (res != null && res.length() > 0) {
                    JSONObject res_server = new JSONObject(res.trim());
                    JSONObject newObj=new JSONObject(String.valueOf(res_server.getJSONObject("res")));
                    server_status = newObj.optInt("status");
                    if (server_status == 1) {
                        server_response = "File Posted";

                    } else {
                        server_response = "Sorry !! Posting failed";
                    }
                }
            } catch (JSONException e) {
                server_response = "Network Error";
                e.printStackTrace();
            } catch (IOException e) {
                server_response = "Network Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            loader_file.setVisibility(View.GONE);
            if (server_status == 1) {
                activityresult.setVisibility(View.GONE);
                rel_feedlist.setVisibility(View.VISIBLE);
                getFeeds();

            }
            Toast.makeText(getActivity(),server_response,Toast.LENGTH_SHORT).show();
        }
    }
   /* private class Question extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Comments Sync";
        String server_message;
        String id,username,email_address,contact_no;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }
//user_id, file_type, title,question
        @Override
        protected Void doInBackground(String... params) {

            try {
                String _user_id = params[0];
                String _file_type = params[1];
                String _text = params[2];
                String _question = params[3];
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
                        .appendQueryParameter("file_type",file_type )
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

                *//**
                 * {
                 "res": {
                 "message": "File liked has been saved.",
                 "status": 1
                 }
                 }
                 * *//*

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
    }*/



}
