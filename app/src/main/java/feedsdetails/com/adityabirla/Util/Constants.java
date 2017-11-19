package feedsdetails.com.adityabirla.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import feedsdetails.com.adityabirla.R;

/**
 * Created by Amaresh on 11/7/17.
 */

public class Constants {

    public static String ONLINE_URL="http://applicationworld.net/forum/";
    public  static String LOGIN="users/loginCheck.json";
    public  static String LIKE="likes/add.json";
    public  static String SHARE="shares/add.json";
    public  static String FEEDS="files/fileList.json";
    public  static String DOWNLOADS="downloads/add.json";
    public  static String SHAREDPREFERENCE_KEY="hindalco";
    public  static String USER_ID="user_id";
    public  static String FILE_UPLOAD="files/add.json";
    public  static String COMMENTS="comments/add.json";
    public  static String COMMENT_LIST="comments/commentList";

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public static void noInternetDialouge(Context _context,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Oops !");
        builder.setMessage(Message);
        ImageView showImage = new ImageView(_context);
        Resources res = _context.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_warning_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, _context.getResources().getColor(R.color.colorPrimaryDark));
        showImage.setImageDrawable(drawable);
        builder.setView(showImage);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
                //finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
