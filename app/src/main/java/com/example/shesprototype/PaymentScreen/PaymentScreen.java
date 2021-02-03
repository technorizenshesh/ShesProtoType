package com.example.shesprototype.PaymentScreen;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.shesprototype.NailedProduct.NailadProductActivity;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonDetails;
import com.example.shesprototype.StripePayment.PaymentActivityStripe;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentScreen extends AppCompatActivity implements IApiResponse {

    private RelativeLayout RR_back;
    private Button bttn_confirm;
    private TextView txt_name;
    private TextView txt_address;
    private TextView txt_distance;
    private TextView txt_date;
    private ImageView img_hair;
    private ProgressBar progressBar;

    private RelativeLayout RR_credit_card;
    private CardView card_bank;
    private CardView card_paypal;
    private CardView cadrd_cash;

    private RadioButton radio_credit;
    private RadioButton radio_bank;
    private RadioButton radio_paypall;
    private RadioButton card_cash;
    private Preference preference;

    String Date="";
    String Time="";
    String isSelected="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

        preference = new Preference(this);

        findview();

        final Intent intent=getIntent();
        if(intent !=null)
        {
             Date = intent.getStringExtra("Date").toString();
             Time = intent.getStringExtra("Time").toString();

            txt_date.setText(Date+","+Time);

        }

        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        bttn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSelected.equalsIgnoreCase("4"))
                {
                    Button bttn_cong;
                    final Dialog dialog = new Dialog(PaymentScreen.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.setContentView(R.layout.congratulation);
                    bttn_cong=dialog.findViewById(R.id.bttn_cong);

                    bttn_cong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (preference.isNetworkAvailable()) {

                                progressBar.setVisibility(View.VISIBLE);

                                add_placeorder();

                            }else {

                                Toast.makeText(PaymentScreen.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });
                }else  if(isSelected.equalsIgnoreCase("1"))
                {
                    Intent intent1=new Intent(PaymentScreen.this, PaymentActivityStripe.class);
                    startActivity(intent1);

                }else  if(isSelected.equalsIgnoreCase("2"))
                {
                    Intent intent1=new Intent(PaymentScreen.this, PaymentActivityStripe.class);
                    startActivity(intent1);

                }else  if(isSelected.equalsIgnoreCase("3"))
                {
                    Intent intent1=new Intent(PaymentScreen.this, PaymentActivityStripe.class);
                    startActivity(intent1);
                }


            }
        });

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getHomeSaloonDetails();

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void findview() {
        RR_back=(RelativeLayout) findViewById(R.id.RR_back);
        bttn_confirm=findViewById(R.id.bttn_confirm);
        txt_name=findViewById(R.id.txt_name);
        txt_address=findViewById(R.id.txt_address);
        txt_distance=findViewById(R.id.txt_distance);
        txt_date=findViewById(R.id.txt_date);
        img_hair=findViewById(R.id.img_hair);
        progressBar=findViewById(R.id.progressBar);

        RR_credit_card=findViewById(R.id.RR_credit_card);
        card_bank=findViewById(R.id.card_bank);
        card_paypal=findViewById(R.id.card_paypal);
        cadrd_cash=findViewById(R.id.cadrd_cash);

        radio_credit=findViewById(R.id.radio_credit);
        radio_bank=findViewById(R.id.radio_bank);
        radio_paypall=findViewById(R.id.radio_paypall);
        card_cash=findViewById(R.id.card_cash);

        RR_credit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSelected= "1";

                radio_credit.setChecked(true);
                radio_bank.setChecked(false);
                radio_paypall.setChecked(false);
                card_cash.setChecked(false);
            }
        });

        card_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSelected= "2";

                radio_credit.setChecked(false);
                radio_bank.setChecked(true);
                radio_paypall.setChecked(false);
                card_cash.setChecked(false);
            }
        });

        card_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSelected= "3";

                radio_credit.setChecked(false);
                radio_bank.setChecked(false);
                radio_paypall.setChecked(true);
                card_cash.setChecked(false);
            }
        });

        cadrd_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSelected= "4";

                radio_credit.setChecked(false);
                radio_bank.setChecked(false);
                radio_paypall.setChecked(false);
                card_cash.setChecked(true);

            }
        });


    }


    public void getHomeSaloonDetails(){

        String salon_id =  Preference.get(PaymentScreen.this,Preference.KEY_salon_id);

        HashMap<String, String> map = new HashMap<>();

        map.put("salon_id",salon_id);

        ApiRequest apiRequest = new ApiRequest(PaymentScreen.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_salon_detaild, Constants.USER_get_salon_detaild,map, Request.Method.POST);
    }

    public void add_placeorder(){

        String salon_id =  Preference.get(PaymentScreen.this,Preference.KEY_salon_id);
        String USER_ID =  Preference.get(PaymentScreen.this,Preference.KEY_USER_ID);

        HashMap<String, String> map = new HashMap<>();

        map.put("user_id",USER_ID);
        map.put("service_id","1");
        map.put("category_id","1");
        map.put("salon_id",salon_id);
        map.put("amount","100");
        map.put("date",Date);
        map.put("time",Time);
        map.put("payment_type","cash");

        ApiRequest apiRequest = new ApiRequest(PaymentScreen.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_add_placeorder, Constants.USER_add_placeorder,map, Request.Method.POST);


    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_salon_detaild.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {

                        SaloonDetails finalArray = new Gson().fromJson(response,new TypeToken<SaloonDetails>(){}.getType());

                        String ImageUrlSlaoon = finalArray.getResult().getCoverImage();


                        txt_name.setText(finalArray.getResult().getName());
                        txt_address.setText(finalArray.getResult().getAddress());

                        if(ImageUrlSlaoon !=null)
                        {
                            Glide.with(this).load(finalArray.getResult().getImage()).placeholder(R.drawable.hair_wash)
                                    .into(img_hair);
                        }



                    }else
                    {
                        Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if (Constants.USER_add_placeorder.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();
                    String message = jsonObject.getString("message").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        Intent intent=new Intent(PaymentScreen.this, NailadProductActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(this, "Booking "+message, Toast.LENGTH_SHORT).show();
                        
                    }else
                    {
                        Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }

}
