
package feedsdetails.com.adityabirla.Activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import feedsdetails.com.adityabirla.Fragment.FeedList;
import feedsdetails.com.adityabirla.Fragment.Profile;
import feedsdetails.com.adityabirla.Fragment.Venue;
import feedsdetails.com.adityabirla.R;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }
    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.mylist:
                frag=new FeedList();
                item.setCheckable(true);
                break;
            case R.id.location:
                frag=new Venue();
                item.setCheckable(true);
                break;

            case R.id.profile:
                frag=new Profile();
                item.setCheckable(true);
                break;
            default:
                frag=new FeedList();
                item.setCheckable(true);

        }
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        /*for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }*/

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }
    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if(text.toString().contentEquals("Venue")){
                actionBar.setTitle("Hindalco Ind. Ltd., Hirakud");

            }
            else {
                actionBar.setTitle(text);
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onBackPressed() {


        if (back_pressed + 2000 > System.currentTimeMillis()) {
            //super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit from the application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        back_pressed = System.currentTimeMillis();

    }


}
