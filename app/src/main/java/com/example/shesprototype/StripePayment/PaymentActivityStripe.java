package com.example.shesprototype.StripePayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardholderNameEditText;
import com.braintreepayments.cardform.view.ExpirationDateEditText;
import com.example.shesprototype.NailedProduct.NailadProductActivity;
import com.example.shesprototype.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import me.yokeyword.swipebackfragment.SwipeBackActivity;

public class PaymentActivityStripe extends SwipeBackActivity
{

    TextView text_toolbar;
    private CardInputWidget mCardInputWidget;
    private ErrorDialogHandler mErrorDialogHandler;
    private ProgressDialogController mProgressDialogController;
    Button pay_btn;
    Card card;
    ProgressDialog pDialog;
    ImageView img_back;
    String media_id;
    String paidAmt;
    Float amt;
    EditText fullname_etxt;
    EditText email_etxt;
    String fullName;

    String email;
    String address;

    RelativeLayout RR_back;

   private EditText edt_cardNumber;
    private EditText edt_exprDate_month;
    private EditText edt_exprDateTwo_year;
    private EditText edt_CVV;

    String Str_Poistion="";
    EditText edt_cardName;

    CardholderNameEditText et_card_holder_name;
    EditText et_card_number1;
    ExpirationDateEditText et_card_expiry_date;
    EditText edt_CVV_number;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_stripe_new);

       // Float amt=  Preference.getFloat(PaymentActivityStripe.this,Preference.KEY_amount);
    /*    String cardNumber=Preference.get(PaymentActivityStripe.this,Preference.KEY_CardNumberNew);
        String cardCardName=Preference.get(PaymentActivityStripe.this,Preference.KEY_CardHolderName);
        String cardExpiry=Preference.get(PaymentActivityStripe.this,Preference.KEY_ExpiryDate);

        String monthExp=cardExpiry.substring(0,2);
        String monthyer=cardExpiry.substring(2,4);

        Toast.makeText(this, ""+cardExpiry, Toast.LENGTH_SHORT).show();
*/
        //Toast.makeText(this, monthExp+"/"+monthyer, Toast.LENGTH_SHORT).show();

        //getPaymentMethod();

        et_card_holder_name = (CardholderNameEditText) findViewById(R.id.et_card_holder_name);
        et_card_number1 = (EditText) findViewById(R.id.et_card_number1);
        et_card_expiry_date = (ExpirationDateEditText) findViewById(R.id.et_card_expiry_date);
        edt_CVV_number = (EditText) findViewById(R.id.edt_CVV_number);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        pay_btn = (Button) findViewById(R.id.pay_btn);
        fullname_etxt = (EditText) findViewById(R.id.fullname_etxt1);
        email_etxt = (EditText) findViewById(R.id.email_etxt);
        RR_back = (RelativeLayout) findViewById(R.id.RR_back);
        edt_cardNumber = (EditText) findViewById(R.id.edt_cardNumber);
        edt_exprDate_month = (EditText) findViewById(R.id.edt_exprDate_month);
        edt_exprDateTwo_year = (EditText) findViewById(R.id.edt_exprDateTwo_year);
        edt_CVV = (EditText) findViewById(R.id.edt_CVV);
        edt_cardName = (EditText) findViewById(R.id.edt_cardName);

        //Str_Poistion= Preference.get(PaymentActivityStripe.this,Preference.KEY_Address);

        edt_cardNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==16) {

                    edt_exprDate_month.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.length() ==16) {

                    edt_cardNumber.requestFocus();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        edt_exprDate_month.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==2) {

                    edt_exprDateTwo_year.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.length() ==2) {

                    edt_exprDate_month.requestFocus();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        edt_exprDateTwo_year.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==2) {

                    edt_CVV.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.length() ==2) {

                    edt_exprDateTwo_year.requestFocus();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String value1 = String.format("%1$,.2f",amt);

        pay_btn.setText("Checkout $"+value1);

        final Card cardToSave = mCardInputWidget.getCard();
        mErrorDialogHandler = new ErrorDialogHandler(getSupportFragmentManager());
        mProgressDialogController =
                new ProgressDialogController(getSupportFragmentManager());
        pDialog = new ProgressDialog(PaymentActivityStripe.this);
        pDialog.setMessage(PaymentActivityStripe.this.getResources().getString(R.string.loading));

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  CardHolderName=et_card_holder_name.getText().toString();
                String  card_number1=et_card_number1.getText().toString();
                String  card_Month=et_card_expiry_date.getMonth().toString();
                String  card_year=et_card_expiry_date.getYear().toString();
                String  CVV_number=edt_CVV_number.getText().toString();

                if(CardHolderName.equalsIgnoreCase(""))
                {
                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Holder Name", Toast.LENGTH_SHORT).show();

                }else if(card_number1.equalsIgnoreCase(""))
                {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Card Number.", Toast.LENGTH_SHORT).show();


                }else if(card_Month.equalsIgnoreCase(""))
                {
                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Expiry Month.", Toast.LENGTH_SHORT).show();

                }else if(card_year.equalsIgnoreCase(""))
                {
                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Expiry Year.", Toast.LENGTH_SHORT).show();

                }else if(CVV_number.equalsIgnoreCase(""))
                {
                    Toast.makeText(PaymentActivityStripe.this, "Please Enter CVV Number.", Toast.LENGTH_SHORT).show();

                }else
                {
                    int month = Integer.parseInt(card_Month);
                    int Year = Integer.parseInt(card_year);

                    card = new Card(card_number1, month, Year, CVV_number);
                    CreateToken(card);
                    progressBar.setVisibility(View.VISIBLE);
                   // pDialog.show();
                   // pDialog.setCancelable(false);

                }



             /*  String  CardHolderName=et_card_holder_name.getText().toString();
               String  card_number1=et_card_number1.getText().toString();
               String  card_Month=et_card_expiry_date.getMonth().toString();
               String  card_year=et_card_expiry_date.getYear().toString();
               String  CVV_number=edt_CVV_number.getText().toString();
                if (CardHolderName.equalsIgnoreCase("")) {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Holder Name", Toast.LENGTH_SHORT).show();

                }else if(card_number1.equalsIgnoreCase("")) {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Card Number.", Toast.LENGTH_SHORT).show();

                } else if(card_Month.equalsIgnoreCase("")) {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Expiry Month.", Toast.LENGTH_SHORT).show();

                }  else if(card_year.equalsIgnoreCase("")) {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter Expiry Year.", Toast.LENGTH_SHORT).show();

                }else if(CVV_number.equalsIgnoreCase("")) {

                    Toast.makeText(PaymentActivityStripe.this, "Please Enter CVV Number.", Toast.LENGTH_SHORT).show();

                } else {

                   *//* String cvv = mCardInputWidget.getCard().getCVC();
                    int exp = mCardInputWidget.getCard().getExpMonth();
                    int exp_year = mCardInputWidget.getCard().getExpYear();
                    String card_num = mCardInputWidget.getCard().getNumber();
                    *//*
                    int month = Integer.parseInt(card_Month);
                    int year = Integer.parseInt(card_year);

                    card = new Card(card_number1, month, year, CVV_number);
                    CreateToken(card);
                    pDialog.show();
                    pDialog.setCancelable(false);
                }*/
            }
        });


    }

    private void CreateToken(Card card) {

      Stripe stripe = new Stripe(PaymentActivityStripe.this, "pk_test_51HOcnlIVXTZWwf8e5u07bjCszEj4KVFVbsYe6QVQTp95WKoxlmDNDS80WBeslUho8P7dQM1bahIsMhvja9clTjoP00NOGGmNa4" +
                "" +
                "" +
                "");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                      //  Toast.makeText(PaymentActivityStripe.this, "Success "+token.getId(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(PaymentActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();
                        // Show localized error message
                        Log.e("token_Stripe",token.getId());

                        Toast.makeText(PaymentActivityStripe.this, "Payment SuccessFully..", Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(PaymentActivityStripe.this, NailadProductActivity.class);
                        startActivity(intent);
                    // pDialog.cancel();
                        progressBar.setVisibility(View.GONE);
                       // Login(token);
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                      //  pDialog.cancel();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PaymentActivityStripe.this, error.getLocalizedMessage(), Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

  /*  private void Login(Token token) {
        String userid = Preference.get(PaymentActivityStripe.this, Preference.KEY_USER_ID);
        paidAmt =Preference.get(PaymentActivityStripe.this,Preference.KEY_amount);
        address=Preference.get(PaymentActivityStripe.this,Preference.key_PlaceUser_address);

        int type = Preference.getInt(PaymentActivityStripe.this, Preference.TYPE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", "157");
        map.put("stripeToken",String.valueOf(token.getId()));
        map.put("media_id", "22");
        map.put("type", "type");
        map.put("paymentAmt", "4000");
        map.put("name", "Harshit");
        map.put("address", "Indore");
        map.put("email", "harshit89@gmail.com");
        map.put("description", "Indore");
        ApiRequest apiRequest = new ApiRequest(PaymentActivityStripe.this, this);
        apiRequest.postRequest(Preference.PAY_FOR_CHANNEL, Preference.PAY_FOR_CHANNEL, map, Request.Method.POST);
    }*/




    private void Validation() {

        fullName = fullname_etxt.getText().toString();
        email = email_etxt.getText().toString();

        if (mCardInputWidget.getCard() == null) {
            mErrorDialogHandler.showError("Invalid Card Data");
            return;
        } else if (mCardInputWidget.getCard() != null || mCardInputWidget.getCard().validateCard()) {
            String cvv = mCardInputWidget.getCard().getCVC();
            int exp = mCardInputWidget.getCard().getExpMonth();
            int exp_year = mCardInputWidget.getCard().getExpYear();
            String card_num = mCardInputWidget.getCard().getNumber();
            card = new Card(card_num, exp, exp_year, cvv);

        } else if (fullName.equalsIgnoreCase("")) {
            Toast.makeText(PaymentActivityStripe.this, "Please enter name.", Toast.LENGTH_SHORT).show();

        } else if (!isValidEmail(email)) {
            Toast.makeText(PaymentActivityStripe.this, "Please enter correct email address.", Toast.LENGTH_SHORT).show();

        } else {
            CreateToken(card);
            pDialog.show();
            pDialog.setCancelable(false);
        }


    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }




/*    private void Login(Token token) {
        String userid = Preference.get(PaymentActivityStripe.this, Preference.KEY_USER_ID);
        paidAmt =Preference.get(PaymentActivityStripe.this,Preference.KEY_amount);
        address=Preference.get(PaymentActivityStripe.this,Preference.key_PlaceUser_address);

        int type = Preference.getInt(PaymentActivityStripe.this, Preference.TYPE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", userid);
        map.put("stripeToken", String.valueOf(token.getId()));
        // map.put("media_id", media_id);
        map.put("type", String.valueOf(type));
        map.put("paymentAmt", paidAmt);
        map.put("name", fullName);
        map.put("address", address);
        map.put("email", email);
        ApiRequest apiRequest = new ApiRequest(PaymentActivityStripe.this, this);
        apiRequest.postRequest(Preference.PAY_FOR_CHANNEL, Preference.PAY_FOR_CHANNEL, map, Request.Method.POST);
    }*/

}
