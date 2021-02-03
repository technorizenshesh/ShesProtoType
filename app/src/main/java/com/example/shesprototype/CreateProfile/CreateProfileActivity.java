package com.example.shesprototype.CreateProfile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.LoginScreen.SignUpModel;
import com.example.shesprototype.MainLoginScreen.MainLoginActivity;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements IApiResponse {

    private Spinner spinnerCountry;
    private int code[] ={+1};
    private int flags[]= {R.mipmap.flag};
    private Button bttn_Sedn;

    RelativeLayout RR_back;

    private ProgressBar progressBar;
    private Preference preference;
    private String android_id ="";

    private EditText edt_name;
    private EditText edt_email;
    private EditText edt_mobile;
    private EditText edt_address;
    private EditText edt_selected_area;
    private EditText edt_passsword;
    private EditText edt_Cpasssword;
    private RelativeLayout RR_profile;

    private Bitmap bitmap;
    private CircleImageView user_profile;
    private ImageView img;
    private Boolean isProfileIage  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

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


        CountrySpinnerAdapter customAdapter=new CountrySpinnerAdapter(CreateProfileActivity.this,flags,code);
        spinnerCountry.setAdapter(customAdapter);
        spinnerCountry.setEnabled(false);

        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        bttn_Sedn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(CreateProfileActivity.this, SelectedAreaActivity.class);
                startActivity(intent);*/
               validation();

            }
        });
        RR_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Dexter.withActivity(CreateProfileActivity.this)
                        .withPermissions(Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {

                                    //Toast.makeText(CreateProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    showPictureDialog(1,0);

                                } else {
                                    showSettingDialogue();
                                }
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
    }

    private void findview() {

        edt_name=findViewById(R.id.edt_name);
         edt_email=findViewById(R.id.edt_email);
         edt_mobile=findViewById(R.id.edt_mobile);
         edt_address=findViewById(R.id.edt_address);
         edt_passsword=findViewById(R.id.edt_passsword);
         edt_Cpasssword=findViewById(R.id.edt_Cpasssword);
        user_profile=findViewById(R.id.user_profile);
        img=findViewById(R.id.img);

        spinnerCountry=findViewById(R.id.spinnerCountry);
        RR_back=findViewById(R.id.RR_back);
        edt_selected_area=findViewById(R.id.edt_selected_area);
        bttn_Sedn=findViewById(R.id.bttn_Sedn);
        progressBar=findViewById(R.id.progressBar);
        RR_profile=findViewById(R.id.RR_profile);
    }


    private void validation() {

        String UserName=edt_name.getText().toString();
        String email=edt_email.getText().toString();
        String mobile=edt_mobile.getText().toString();
        String address=edt_address.getText().toString();
        String selected_area=edt_selected_area.getText().toString();
        String password=edt_passsword.getText().toString();
        String confrmPassword=edt_Cpasssword.getText().toString();


        if(UserName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter FullName.", Toast.LENGTH_SHORT).show();

        }else if(!isValidEmail(email))
        {
            Toast.makeText(this, "Please Enter email.", Toast.LENGTH_SHORT).show();

        }else if(mobile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter mobile.", Toast.LENGTH_SHORT).show();

        }else if(address.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter address.", Toast.LENGTH_SHORT).show();

        }else if(selected_area.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter City.", Toast.LENGTH_SHORT).show();

        }else if(password.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter password.", Toast.LENGTH_SHORT).show();


        }else if(confrmPassword.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show();

        }else if(!confrmPassword.equalsIgnoreCase(password))
        {
            Toast.makeText(this, "Don't match Password.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (preference.isNetworkAvailable()) {

                bttn_Sedn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                signUpMethod(UserName,email,mobile,address,selected_area,password);

            }else {
                Toast.makeText(this,  R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void signUpMethod(String user_name,String email,String mobile,String address,String city,String password){

        HashMap<String, String> map = new HashMap<>();

        map.put("user_name",user_name);
        map.put("email",email);
        map.put("mobile",mobile);
        map.put("address",address);
        map.put("city",city);
        map.put("password",password);
        map.put("image","abc");
        map.put("lat","25.00");
        map.put("lon","25.00");
        map.put("register_id",android_id);


        ApiRequest apiRequest = new ApiRequest(CreateProfileActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_SIGNUP, Constants.USER_SIGNUP,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_SIGNUP.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);
            bttn_Sedn.setEnabled(true);

            if (response !=null) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();
                    String result = jsonObject.getString("message").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        SignUpModel finalArray = new Gson().fromJson(response,new TypeToken<SignUpModel>(){}.getType());

                        Toast.makeText(this, finalArray.getMessage(), Toast.LENGTH_SHORT).show();

                        preference.save(CreateProfileActivity.this,Preference.KEY_USER_ID,finalArray.getResult().getId());

                        Intent intent=new Intent(CreateProfileActivity.this, MainLoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else
                    {
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
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
        bttn_Sedn.setEnabled(true);
        Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfileActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showPictureDialog(final int i,final int j) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(i);
                                break;
                            case 1:
                                takePhotoFromCamera(j);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary(int i) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, i);
    }

    private void takePhotoFromCamera(int j) {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(cameraIntent, j);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();

                    try {
                        //   isImage=true;
                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImage);

                        user_profile.setImageBitmap(bitmap);
                        img.setVisibility(View.GONE);
                        File f = new File(String.valueOf(selectedImage));
                        String imageName = f.getName();
                        isProfileIage =true;

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e); }

                }
                break;
            case 0:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                     isProfileIage =true;
                    File f = new File(String.valueOf(selectedImage));
                    String imageName = f.getName();
                    img.setVisibility(View.GONE);
                    user_profile.setImageBitmap(bitmap);

                }
                break;
        }
    }
}
