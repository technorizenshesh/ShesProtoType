package com.example.shesprototype.ProductFilterScreen;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterProductActivity extends AppCompatActivity implements IApiResponse {

    RelativeLayout RR_back;
    private Spinner Spn_Category;
    private Preference preference;
    private ProgressBar progressBar;
    ArrayList<ChoseCategoryModel> Category=new ArrayList<>();
    String categoryId ="";
    public FilterProductActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_product);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

        findView();
        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getCategory();
    }

    private void findView() {
        progressBar=findViewById(R.id.progressBar);
        Spn_Category=findViewById(R.id.Spn_Category);
        RR_back=findViewById(R.id.RR_back);
    }

    public void getCategory(){

        HashMap<String, String> map = new HashMap<>();

        ApiRequest apiRequest = new ApiRequest(FilterProductActivity.this,this);

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

                        getHomeModel finalArray = new Gson().fromJson(response,new TypeToken<getHomeModel>(){}.getType());

                        Category = (ArrayList<ChoseCategoryModel>) finalArray.getResult();

                        Category(Category,Spn_Category);

                    }else
                    {
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


    private void Category(final ArrayList<ChoseCategoryModel> Category, Spinner SpinnerNew) {

        CategoryAdapter customAdapter1=new CategoryAdapter(FilterProductActivity.this ,Category);
        SpinnerNew.setAdapter(customAdapter1);

        SpinnerNew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                categoryId = Category.get(position).getId().toString();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

    }
}
