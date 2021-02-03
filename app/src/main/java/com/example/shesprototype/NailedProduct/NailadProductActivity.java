package com.example.shesprototype.NailedProduct;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.BookingHistory.BookingHistoryActivity;
import com.example.shesprototype.MainLoginScreen.MainLoginActivity;
import com.example.shesprototype.Preference;
import com.example.shesprototype.ProductFilterScreen.FilterProductActivity;
import com.example.shesprototype.Profile.ProfileActivity;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopScreen.SaloonShopActivity;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NailadProductActivity extends AppCompatActivity implements IApiResponse {

    private RecyclerView recycler_view_ProductList;
    private NailedRecyclerViewAdapter  mAdapter;
    private ArrayList<getHomeDataModel> modelList = new ArrayList<>();
    private RelativeLayout RR_filter;
    private RelativeLayout RR_nav;
    private LinearLayout LL_home;
    private LinearLayout ll_logout;
    private LinearLayout ll_profile;
    private TextView txt_empty;
    private TextView txt_name;
    private TextView txt_email;

    private Preference preference;
    private ProgressBar progressBar;
    private LinearLayout ll_Booking_history;
    String android_id ="";

    public static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

        findview();

        String Name = Preference.get(NailadProductActivity.this,Preference.KEY_USER_name);
        String Email =  Preference.get(NailadProductActivity.this,Preference.KEY_USER_email);
        //android device Id
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preference = new Preference(this);

        txt_email.setText(Email);
        txt_name.setText(Name);

        RR_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NailadProductActivity.this, FilterProductActivity.class);
                startActivity(intent);
            }
        });

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getHome();

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        RR_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    private void findview() {
        recycler_view_ProductList=(RecyclerView) findViewById(R.id.recycler_view_ProductList);
        RR_filter=(RelativeLayout) findViewById(R.id.RR_filter);
        progressBar=findViewById(R.id.progressBar);
        txt_empty=findViewById(R.id.txt_empty);
        drawer = findViewById(R.id.drawer);
        RR_nav = findViewById(R.id.RR_nav);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        LL_home = findViewById(R.id.LL_home);
        ll_profile = findViewById(R.id.ll_profile);
        ll_logout = findViewById(R.id.ll_logout);
        ll_Booking_history = findViewById(R.id.ll_Booking_history);

        LL_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
                }
            }
        });

        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
                }

                Preference.clearPreference(NailadProductActivity.this);

                Intent intent=new Intent(NailadProductActivity.this, MainLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ll_Booking_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
                }

                Intent intent=new Intent(NailadProductActivity.this, BookingHistoryActivity.class);
                startActivity(intent);

            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
                }
              Intent intent=new Intent(NailadProductActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    private void setAdapter(ArrayList<getHomeDataModel> modelList) {

      /*  modelList.add(new NailedProductListModel("HairCut",R.drawable.haircut_new));
        modelList.add(new NailedProductListModel("Manicure",R.drawable.manicure_new));
        modelList.add(new NailedProductListModel("Manicure",R.drawable.manicure_new));
        modelList.add(new NailedProductListModel("Eyelashes",R.drawable.eyelashes_new));
        modelList.add(new NailedProductListModel("Beard trimm",R.drawable.beard_trimm_new));
        modelList.add(new NailedProductListModel("Pedicure",R.drawable.pedicure_new));
        modelList.add(new NailedProductListModel("Pedicure",R.drawable.pedicure_new));
        modelList.add(new NailedProductListModel("Eyelashes",R.drawable.eyelashes_new));*/

        mAdapter = new NailedRecyclerViewAdapter(NailadProductActivity.this, this.modelList);
        recycler_view_ProductList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NailadProductActivity.this);
        recycler_view_ProductList.setLayoutManager(new GridLayoutManager(NailadProductActivity.this, 2));
        recycler_view_ProductList.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new NailedRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, getHomeDataModel model) {

                Preference.save(NailadProductActivity.this,Preference.KEY_CategoryId,model.getId());

                Intent intent=new Intent(NailadProductActivity.this, SaloonShopActivity.class);
                startActivity(intent);

            }
        });

    }

    public void getHome(){

        HashMap<String, String> map = new HashMap<>();

        ApiRequest apiRequest = new ApiRequest(NailadProductActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_category, Constants.USER_get_category,map, Request.Method.GET);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_category.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        txt_empty.setVisibility(View.GONE);

                        getHomeModel finalArray = new Gson().fromJson(response,new TypeToken<getHomeModel>(){}.getType());

                        modelList = (ArrayList<getHomeDataModel>) finalArray.getResult();

                        setAdapter(modelList);

                    }else
                    {
                        txt_empty.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Your have entered wrong email & password", Toast.LENGTH_SHORT).show();
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
