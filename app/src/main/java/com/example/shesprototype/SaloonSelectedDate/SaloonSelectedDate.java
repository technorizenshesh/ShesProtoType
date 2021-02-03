package com.example.shesprototype.SaloonSelectedDate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.PaymentScreen.PaymentScreen;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SaloonSelectedDate extends AppCompatActivity implements IApiResponse {

    private RecyclerView recycler_available_slot;
    private AvailableSlot mAdapter;
    private ArrayList<String> modelList = new ArrayList<>();
    private ArrayList<String> modelListTime = new ArrayList<>();

    Button btn_booking;
    private RelativeLayout RR_date;
    private TextView txt_date;
    private int mYear, mMonth,mDay;

    String MondayOpen="";
    String MondayClose="";

    String TuesdayOpen="";
    String TuesdayClose="";

    String WednesDayOpen="";
    String WednesDayClose="";

    String ThursdayOpen="";
    String ThursdayClose="";

    String FridayOpen="";
    String FridayClose="";

    String SaturdayOpen="";
    String SaturdayClose="";

    String SundayOpen="";
    String SundayClose="";

    private Preference preference;
    private ProgressBar progressBar;
    private String android_id ="";
    private RelativeLayout RR_time;

    String frmdate11="";
    boolean isDate=false;
    String salon_Userid="";

    public static String Time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_selected_date);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

       /* Date c = Calendar.getInstance().getTime();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", c);*/


        preference = new Preference(SaloonSelectedDate.this);

        findview();

        salon_Userid =  Preference.get(SaloonSelectedDate.this,Preference.KEY_salon_User_id);

       // getTime(9,7);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isDate)
                {
                    Toast.makeText(SaloonSelectedDate.this, "Please Select Date..", Toast.LENGTH_SHORT).show();

                }else if(Time.equalsIgnoreCase(""))
                {
                    Toast.makeText(SaloonSelectedDate.this, "Please Select Time", Toast.LENGTH_SHORT).show();

                }else
                {

                    Intent intent=new Intent(SaloonSelectedDate.this, PaymentScreen.class);
                    intent.putExtra("Time",Time);
                    intent.putExtra("Date",frmdate11);
                    startActivity(intent);
                }
            }
        });

        RR_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SaloonSelectedDate.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                              //  RR_time.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
                              String frmdate1 = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                               String DayFull = getFullDayName(frmdate1);

                                frmdate11 = getDate(frmdate1);

                                txt_date.setText(frmdate11);

                                isDate =true;

                                RR_time.setVisibility(View.VISIBLE);

                              if (preference.isNetworkAvailable()) {

                                    progressBar.setVisibility(View.VISIBLE);

                                    getHomeSaloon(DayFull,salon_Userid);

                                }else {

                                    Toast.makeText(SaloonSelectedDate.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();

            }
        });


/*
       if (preference.isNetworkAvailable()) {

           progressBar.setVisibility(View.VISIBLE);

            getHomeSaloon(dayOfTheWeek,salon_Userid);

        }else {

            Toast.makeText(SaloonSelectedDate.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }*/

        //setAdapter();


    }


    private void getTime(int Open,int Close) {

        for(int i=Open;i>Close;i--)
        {
            modelListTime.add(String.valueOf(i));
        }

    }

    private void findview() {
        recycler_available_slot=findViewById(R.id.recycler_available_slot);
        btn_booking=findViewById(R.id.btn_booking);
        RR_date=findViewById(R.id.RR_date);
        txt_date=findViewById(R.id.txt_date);
        progressBar=findViewById(R.id.progressBar);
       RR_time=findViewById(R.id.RR_time);
    }

    private void setAdapter(ArrayList<String> modelList) {

        /*modelList.add(new SaloonSpecialistModel("9:30Am"));
        modelList.add(new SaloonSpecialistModel("9:30Am"));
        modelList.add(new SaloonSpecialistModel("9:30Am"));
        modelList.add(new SaloonSpecialistModel("9:30Am"));
        modelList.add(new SaloonSpecialistModel("9:30Am"));*/

        mAdapter = new AvailableSlot(SaloonSelectedDate.this, this.modelList);
        recycler_available_slot.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaloonSelectedDate.this);
        recycler_available_slot.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_available_slot.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new AvailableSlot.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String model) {

            }
        });
    }

    public static String getFullDayName(String startDat) {

        SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(startDat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEEE");
        String newDateStr = postFormater.format(dateObj);
        return newDateStr;

    }

    public static String getFullDayNameNew(String startDat) {

        SimpleDateFormat curFormater = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(startDat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEEE");
        String newDateStr = postFormater.format(dateObj);
        return newDateStr;

    }


    private String getDate(String Date) {

        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy");
        String newDateStr = postFormater.format(dateObj);
        return newDateStr;
    }




    public void getHomeSaloon(String day,String salon_Userid){

        HashMap<String, String> map = new HashMap<>();

        map.put("user_id",salon_Userid);

        map.put("day",day);

        ApiRequest apiRequest = new ApiRequest(SaloonSelectedDate.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.get_user_time_slot, Constants.get_user_time_slot,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.get_user_time_slot.equalsIgnoreCase(tag_json_obj)){

            if (response !=null)  {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {

                        TimeSlotDataModel finalArray = new Gson().fromJson(response,new TypeToken<TimeSlotDataModel>(){}.getType());

                        modelList = (ArrayList<String>) finalArray.getResult().getTimeSlote();

                        setAdapter(modelList);

                    }else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SaloonSelectedDate.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(SaloonSelectedDate.this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


}
