package com.example.shesprototype.SaloonShopDetailsScreen;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.shesprototype.GPSTracker;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.Abouts.AboutsFragment;
import com.example.shesprototype.SaloonShopDetailsScreen.Gallary.GallaryFragment;
import com.example.shesprototype.SaloonShopDetailsScreen.Review.ReviewFragment;
import com.example.shesprototype.SaloonShopDetailsScreen.Services.ServicesFragment;
import com.example.shesprototype.SaloonShopScreen.ApiModel.Gallery;
import com.example.shesprototype.SaloonShopScreen.ApiModel.UsersDetails;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaloonShopDetailsActivity extends AppCompatActivity implements IApiResponse {

    RecyclerView recycler_view_saloon_specialist;
    private SaloonSpecialistRecyclerViewAdapter mAdapter;

    private ArrayList<UsersDetails> modelList = new ArrayList<>();
    public static ArrayList<Gallery> modelList_gallery = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txt_open;
    private ImageView Saloon_img;
    private TextView saloon_name;
    private TextView txt_view_rating;
    private TextView txt_address;

    private Preference preference;
    private ProgressBar progressBar;
    private String android_id ="";


    GPSTracker gpsTracker;
    String latitude ="";
    String longitude ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_shop_details);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

        //android device Id
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preference = new Preference(this);

        findview();

        txt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(SaloonShopDetailsActivity.this, SaloonshopGender.class);
                startActivity(intent);*/
            }
        });

        String salon_id =  Preference.get(SaloonShopDetailsActivity.this,Preference.KEY_salon_id);

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getHomeSaloon(salon_id);

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        gpsTracker=new GPSTracker(this);

        if(gpsTracker.canGetLocation()){

            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());

        }else{
            gpsTracker.showSettingsAlert();
        }

    }
    private void findview() {
        recycler_view_saloon_specialist=findViewById(R.id.recycler_view_saloon_specialist);
        txt_open=findViewById(R.id.txt_open);
        progressBar=findViewById(R.id.progressBar);
        saloon_name=findViewById(R.id.saloon_name);
        Saloon_img=findViewById(R.id.Saloon_img);
        txt_view_rating=findViewById(R.id.txt_view_rating);
        txt_address=findViewById(R.id.txt_address);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new AboutsFragment(), "About");
        adapter.addFrag(new ServicesFragment(), "Services");
        adapter.addFrag(new GallaryFragment(), "Gallary");
        adapter.addFrag(new ReviewFragment(), "Review");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

   private void setAdapter(ArrayList<UsersDetails> modelList) {

/*        this.modelList.add(new SaloonSpecialistModel("Deniel",R.drawable.haircut_img,"Manager"));
        this.modelList.add(new SaloonSpecialistModel("Kieran",R.drawable.saloon_img,"Sr.Barber"));
        this.modelList.add(new SaloonSpecialistModel("Helan",R.drawable.haircut_img,"Makup artist"));
        this.modelList.add(new SaloonSpecialistModel("Ben",R.drawable.haircut_img,"Makup artist"));
        this.modelList.add(new SaloonSpecialistModel("Ben",R.drawable.haircut_img,"Hair style"));*/

        mAdapter = new SaloonSpecialistRecyclerViewAdapter(SaloonShopDetailsActivity.this, this.modelList);
        recycler_view_saloon_specialist.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaloonShopDetailsActivity.this);
        recycler_view_saloon_specialist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recycler_view_saloon_specialist.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SaloonSpecialistRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, UsersDetails model) {

            }
        });
    }

    public void getHomeSaloon(String salon_id){

        HashMap<String, String> map = new HashMap<>();

        map.put("salon_id",salon_id);

        ApiRequest apiRequest = new ApiRequest(SaloonShopDetailsActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_salon_detaild, Constants.USER_get_salon_detaild,map, Request.Method.POST);
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
                        String NameSaloon = finalArray.getResult().getName();
                        String rating = finalArray.getResult().getRating();
                        String Address = finalArray.getResult().getAddress();


                        Preference.save(SaloonShopDetailsActivity.this,Preference.key_saloon_image,ImageUrlSlaoon);
                        Preference.save(SaloonShopDetailsActivity.this,Preference.key_NameSaloon,NameSaloon);
                        Preference.save(SaloonShopDetailsActivity.this,Preference.key_Saloonrating,rating);
                        Preference.save(SaloonShopDetailsActivity.this,Preference.key_SaloonAddress,Address);


                        Glide.with(this).load(ImageUrlSlaoon).placeholder(R.drawable.hair_wash)
                                .into(Saloon_img);

                        saloon_name.setText(NameSaloon);
                        txt_view_rating.setText(rating);
                        txt_address.setText(Address);

                       // modelList = (ArrayList<UsersDetails>) finalArray.getResult().get(0).getSalonSpecialities();
                      //  modelList_gallery = (ArrayList<Gallery>) finalArray.getResult().get(0).getGallery();

                     //   setAdapter(modelList);

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
