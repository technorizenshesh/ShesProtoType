package com.example.shesprototype.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shesprototype.Preference;
import com.example.shesprototype.R;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout RR_back;
    TextView txt_name;
    TextView txt_email;
    TextView txt_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String address = Preference.get(ProfileActivity.this,Preference.KEY_Address);
        String UserName = Preference.get(ProfileActivity.this,Preference.KEY_USER_name);
        String Email =Preference.get(ProfileActivity.this,Preference.KEY_USER_email);
        String Image = Preference.get(ProfileActivity.this,Preference.KEY_image);
        String Mobile = Preference.get(ProfileActivity.this,Preference.KEY_mobile);


        RR_back=findViewById(R.id.RR_back);
        txt_name=findViewById(R.id.txt_name);
        txt_email=findViewById(R.id.txt_email);
        txt_mobile=findViewById(R.id.txt_mobile);


        txt_name.setText(UserName);
        txt_email.setText(Email);
        txt_mobile.setText(Mobile);

        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}