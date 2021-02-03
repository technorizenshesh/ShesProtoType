package com.example.shesprototype.MainLoginScreen;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.CreateProfile.CountrySpinnerAdapter;
import com.example.shesprototype.ForgetPassword.ForgetPasswordActivity;
import com.example.shesprototype.LoginScreen.SignUpModel;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, IApiResponse {

    private Spinner spinnerCountry;
    private int code[] ={+1};
    private int flags[]= {R.mipmap.flag,R.drawable.usa_flag,R.drawable.china,R.drawable.indonesia};
    Button btn_Login;

    private RelativeLayout RR_google_login;
    private Preference preference;
    private ProgressBar progressBar;
    String android_id ="";

    private EditText edt_mobile;
    private EditText edt_password;
    private TextView txt_forgot_password;
    private ImageView img_password;

    //GoogleSignIn
    private SignInButton signInButton;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 1;
    private GoogleSignInApi mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    FirebaseAuth.AuthStateListener mAuthLitner;
    TextView txt_edit;
    RelativeLayout RR_FaceBook_login;


    //FaceBook
    CallbackManager mCallbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.mehroon));
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }

        //android device Id
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preference = new Preference(this);


        findview();

        CountrySpinnerAdapter customAdapter=new CountrySpinnerAdapter(MainLoginActivity.this,flags,code);
        spinnerCountry.setAdapter(customAdapter);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainLoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });

        img_password.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {

                    case MotionEvent.ACTION_UP:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                }
                return true;
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


        RR_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        RR_FaceBook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

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
                Toast.makeText(MainLoginActivity.this, "btnCancel", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onCancel");
                // ...
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainLoginActivity.this, "Btnerrror", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });



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

                                Toast.makeText(MainLoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(MainLoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void validation() {

        String mobile=edt_mobile.getText().toString();
        String password=edt_password.getText().toString();

        if(mobile.equalsIgnoreCase(""))
        {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Please Enter Mobile Number.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }else if(password.equalsIgnoreCase(""))
        {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Please Enter password.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }else
        {
            if (preference.isNetworkAvailable()) {

                btn_Login.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                loginMethod(mobile,password);

            }else {

                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void findview() {
        loginButton=findViewById(R.id.loginButton);
        spinnerCountry=(Spinner)findViewById(R.id.spinnerCountry);
        btn_Login=(Button)findViewById(R.id.btn_Login);
        edt_mobile=findViewById(R.id.edt_mobile);
        edt_password=findViewById(R.id.edt_password);
        progressBar=findViewById(R.id.progressBar);
        txt_forgot_password=findViewById(R.id.txt_forgot_password);
        img_password=findViewById(R.id.img_password);
        RR_google_login=findViewById(R.id.RR_google_login);
        RR_FaceBook_login=findViewById(R.id.RR_FaceBook_login);

    }



    public void loginMethod(String mobile,String password){

        HashMap<String, String> map = new HashMap<>();

        map.put("mobile",mobile);
        map.put("password",password);
        map.put("register_id",android_id);

        ApiRequest apiRequest = new ApiRequest(MainLoginActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_LOGIN, Constants.USER_LOGIN,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_LOGIN.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            btn_Login.setEnabled(true);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();
                    String result = jsonObject.getString("message").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        SignUpModel finalArray = new Gson().fromJson(response,new TypeToken<SignUpModel>(){}.getType());

                        Toast.makeText(this, finalArray.getMessage(), Toast.LENGTH_SHORT).show();

                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_ID,finalArray.getResult().getId());
                        Preference.save(MainLoginActivity.this,Preference.KEY_Address,finalArray.getResult().getAddress());
                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_name,finalArray.getResult().getUserName());
                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_email,finalArray.getResult().getEmail());

                        Preference.save(MainLoginActivity.this,Preference.KEY_image,finalArray.getResult().getImage());
                        Preference.save(MainLoginActivity.this,Preference.KEY_mobile,finalArray.getResult().getMobile());

                        Intent intent=new Intent(MainLoginActivity.this, NailadProductActivity.class);
                        startActivity(intent);
                        finish();

                    }else
                    {
                        btn_Login.setEnabled(true);
                        Toast.makeText(this, "Your have entered wrong email & password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    btn_Login.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }else  if (Constants.USER_social_login.equalsIgnoreCase(tag_json_obj)){

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

                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_ID,finalArray.getResult().getId());
                        Preference.save(MainLoginActivity.this,Preference.KEY_Address,finalArray.getResult().getAddress());
                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_name,finalArray.getResult().getUserName());
                        Preference.save(MainLoginActivity.this,Preference.KEY_USER_email,finalArray.getResult().getEmail());

                        Preference.save(MainLoginActivity.this,Preference.KEY_image,finalArray.getResult().getImage());
                        Preference.save(MainLoginActivity.this,Preference.KEY_mobile,finalArray.getResult().getMobile());


                        Intent intent=new Intent(MainLoginActivity.this, NailadProductActivity.class);
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
        btn_Login.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
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

        ApiRequest apiRequest = new ApiRequest(MainLoginActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_social_login, Constants.USER_social_login,map, Request.Method.POST);
    }

    //Google Login
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

        } else {

            Toast.makeText( this, "Login Unsuccessful", Toast.LENGTH_SHORT ).show();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
