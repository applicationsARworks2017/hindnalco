package feedsdetails.com.adityabirla.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import feedsdetails.com.adityabirla.R;

public class Login_Activity extends AppCompatActivity {
    LinearLayout lin_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        lin_signin=(LinearLayout)findViewById(R.id.lin_signin);
        lin_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Activity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
