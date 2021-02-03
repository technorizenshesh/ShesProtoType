package com.example.shesprototype.LoginScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.CreateProfile.CreateProfileActivity;
import com.example.shesprototype.GPSTracker;
import com.example.shesprototype.MainLoginScreen.MainLoginActivity;
import com.example.shesprototype.NailedProduct.NailadProductActivity;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,IApiResponse {

    private TextView txt_signUp;
    private TextView txt_login;
    private RelativeLayout RR_google_login;
    private RelativeLayout RR_FaceBook_login;

    //GoogleSignIn
    private SignInButton signInButton;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 1;
    private GoogleSignInApi mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    FirebaseAuth.AuthStateListener mAuthLitner;
    TextView txt_edit;

    private Preference preference;
    private ProgressBar progressBar;
    String android_id ="";

    GPSTracker gpsTracker;
    String latitude ="";
    String longitude ="";

    //FaceBook
    CallbackManager mCallbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        findView();

        RR_FaceBook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });


        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this, CreateProfileActivity.class);
                startActivity(intent);
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this, MainLoginActivity.class);
                startActivity(intent);
            }
        });

        RR_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);

            }
        });

        //GoogleSignIn
        mAuth = FirebaseAuth.getInstance();
        signInButton = (SignInButton) findViewById( R.id.btn_google );
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        } );

        //FaceBook Login
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "btnCancel", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onCancel");
                // ...
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Btnerrror", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });


        gpsTracker=new GPSTracker(this);

        if(gpsTracker.canGetLocation()){

            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());

        }else{
            gpsTracker.showSettingsAlert();
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //   Toast.makeText(Activity_LoginOption.this, ""+token, Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            String UsernAME=user.getDisplayName();
                            String email=user.getEmail();
                            String SocialId=user.getUid();
                            Uri Url=user.getPhotoUrl();

                            if (preference.isNetworkAvailable()) {

                                progressBar.setVisibility(View.VISIBLE);

                                SocialLogin(UsernAME,"123456789",email,SocialId, String.valueOf(Url));

                            }else {

                                Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void findView() {
        loginButton=findViewById(R.id.loginButton);
        progressBar=findViewById(R.id.progressBar);
        txt_signUp=findViewById(R.id.txt_signUp);
        txt_login=findViewById(R.id.txt_login);
        RR_google_login=findViewById(R.id.RR_google_login);
        RR_FaceBook_login=findViewById(R.id.RR_FaceBook_login);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            handleSignInResult( result );
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            String UsernAME=account.getDisplayName();
            String email=account.getEmail();
            String SocialId=account.getId();
            Uri Url=account.getPhotoUrl();

            if (preference.isNetworkAvailable()) {

                signInButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                SocialLogin(UsernAME,"123456789",email,SocialId, String.valueOf(Url));

            }else {

                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }


           /* Intent intent=new Intent(LoginActivity.this,MainLoginActivity.class);
            startActivity(intent);*/


        } else {

            Toast.makeText( this, "Login Unsuccessful", Toast.LENGTH_SHORT ).show();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void SocialLogin(String user_name,String mobile,String email,String social_id,String image){

        HashMap<String, String> map = new HashMap<>();

        map.put("user_name",user_name);
        map.put("mobile",mobile);
        map.put("email",email);
        map.put("social_id",social_id);
        map.put("lat","25.00");
        map.put("lon","25.00");
        map.put("image","xyz");
        map.put("register_id",android_id);

        ApiRequest apiRequest = new ApiRequest(LoginActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_social_login, Constants.USER_social_login,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_social_login.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            signInButton.setEnabled(true);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();
                    String result = jsonObject.getString("message").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        SignUpModel finalArray = new Gson().fromJson(response,new TypeToken<SignUpModel>(){}.getType());

                        Toast.makeText(this, finalArray.getMessage(), Toast.LENGTH_SHORT).show();

                        Preference.save(LoginActivity.this,Preference.KEY_USER_ID,finalArray.getResult().getId());
                        Preference.save(LoginActivity.this,Preference.KEY_Address,finalArray.getResult().getAddress());
                        Preference.save(LoginActivity.this,Preference.KEY_USER_name,finalArray.getResult().getUserName());
                        Preference.save(LoginActivity.this,Preference.KEY_USER_email,finalArray.getResult().getEmail());

                        Preference.save(LoginActivity.this,Preference.KEY_image,finalArray.getResult().getImage());
                        Preference.save(LoginActivity.this,Preference.KEY_mobile,finalArray.getResult().getMobile());


                        Intent intent=new Intent(LoginActivity.this, NailadProductActivity.class);
                        startActivity(intent);
                        finish();

                    }else
                    {
                        signInButton.setEnabled(true);
                        Toast.makeText(this, "Your have entered wrong email & password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    signInButton.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        signInButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


}
