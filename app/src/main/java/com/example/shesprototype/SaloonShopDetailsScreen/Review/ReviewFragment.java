package com.example.shesprototype.SaloonShopDetailsScreen.Review;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.LoginScreen.SignUpModel;
import com.example.shesprototype.NailedProduct.NailadProductActivity;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ReviewFragment extends Fragment implements IApiResponse {

    View rootView;
    private ProgressBar progressBar;
    TextView txt_write;
    RelativeLayout RR_all_review;
    Button btn_save;

    boolean isReview=false;

    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.review_fragment, container, false);

        progressBar=rootView.findViewById(R.id.progressBar);
        txt_write=rootView.findViewById(R.id.txt_write);
        RR_all_review=rootView.findViewById(R.id.RR_all_review);
        btn_save=rootView.findViewById(R.id.btn_save);

        txt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isReview)
                {
                    RR_all_review.setVisibility(View.GONE);
                    isReview=false;

                }else
                {
                    RR_all_review.setVisibility(View.VISIBLE);
                    isReview=true;
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RR_all_review.setVisibility(View.GONE);

            }
        });
        return  rootView;
    }

    public void SocialLogin(){

        HashMap<String, String> map = new HashMap<>();

        map.put("user_name","");
        map.put("mobile","");

        ApiRequest apiRequest = new ApiRequest(getActivity(),this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_social_login, Constants.USER_social_login,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_social_login.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            //signInButton.setEnabled(true);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();
                    String result = jsonObject.getString("message").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        SignUpModel finalArray = new Gson().fromJson(response,new TypeToken<SignUpModel>(){}.getType());

                        Toast.makeText(getActivity(), finalArray.getMessage(), Toast.LENGTH_SHORT).show();

                        Preference.save(getActivity(),Preference.KEY_USER_ID,finalArray.getResult().getId());

                        Intent intent=new Intent(getActivity(), NailadProductActivity.class);
                        startActivity(intent);

                    }else
                    {
                       // signInButton.setEnabled(true);
                        Toast.makeText(getActivity(), "Your have entered wrong email & password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //signInButton.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
     //   signInButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }

}
