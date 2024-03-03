package com.trodev.wifibillmanageruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.help5g.uddoktapaysdk.UddoktaPay;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.trodev.wifibillmanageruser.models.BillModels;
import com.trodev.wifibillmanageruser.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayBillActivity extends AppCompatActivity {
    private static final String API_KEY = "5078f2cffe40017a8e7648b5ec98c763035f44b8";
    private static final String CHECKOUT_URL = "https://payment.trodev.com/api/checkout-v2";
    private static final String VERIFY_PAYMENT_URL = "https://payment.trodev.com/api/verify-payment";
    private static final String REDIRECT_URL = "https://www.trodev.com";
    private static final String CANCEL_URL = "https://www.trodev.com";

    // Instance variables to store payment information
    private String storedFullName;
    private String storedEmail;
    private String storedAmount;
    private String storedInvoiceId;
    private String storedPaymentMethod;
    private String storedSenderNumber;
    private String storedTransactionId;
    private String storedDate;
    private String storedFee;
    private String storedChargedAmount;

    private String storedMetaKey1;
    private String storedMetaValue1;

    private String storedMetaKey2;
    private String storedMetaValue2;

    private String storedMetaKey3;
    private String storedMetaValue3;

    private MaterialButton payBtn;
    TextInputEditText nameEt, emailEt, amountEt, uidEt, mobileET;
    LinearLayout webLayout, uiLayout;
    WebView payWebView;
    TextView resultTv;

    DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    BillModels userProfile;
    AutoCompleteTextView autoCompleteTextView;
    String b_name, b_email, b_price, b_mobile, b_month, b_packages, b_user_id;
    String biller_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);

        /*textview*/
        resultTv = findViewById(R.id.resultTv);

        /*edit text*/
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        amountEt = findViewById(R.id.amountEt);
        mobileET = findViewById(R.id.mobileET);
        uidEt = findViewById(R.id.uidEt);

        /*web view & ui layout*/
        webLayout = findViewById(R.id.webLayout);
        uiLayout = findViewById(R.id.uiLayout);
        payWebView = findViewById(R.id.payWebView);

        /*button*/
        payBtn = findViewById(R.id.payBtn);

        /*Package adapter view*/

        /* String[] type = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.drop_down_list, type
        );

        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PayBillActivity.this, autoCompleteTextView.getText().toString() + " select", Toast.LENGTH_SHORT).show();
            }
        });
*/
        /*####################################################################################################################################*/
        /*####################################################################################################################################*/
        /*get admin info from admin user database*/
        /*database location and get user uid*/
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        /*show user profile data*/
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {

                    String u_name = userProfile.uname;
                    String u_email = userProfile.email;
                    String u_price = userProfile.prices;
                    String u_id = userProfile.user_token;
                    String u_mobile = userProfile.num;

                    // uidEt.setText(u_id);
                    nameEt.setText(u_name);
                    emailEt.setText(u_email);
                    amountEt.setText(u_price);
                    uidEt.setText(u_id);
                    mobileET.setText(u_mobile);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PayBillActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uiLayout.setVisibility(View.GONE);
                webLayout.setVisibility(View.VISIBLE);

                String fullname = nameEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String amount = amountEt.getText().toString().trim();

                // Set your metadata values in the map
                Map<String, String> metadataMap = new HashMap<>();
                metadataMap.put("CustomMetaData1", "Meta Value 1");
                metadataMap.put("CustomMetaData2", "Meta Value 2");
                metadataMap.put("CustomMetaData3", "Meta Value 3");

                UddoktaPay.PaymentCallback paymentCallback = new UddoktaPay.PaymentCallback() {
                    @Override
                    public void onPaymentStatus(String status, String fullName, String email, String amount, String invoiceId,
                                                String paymentMethod, String senderNumber, String transactionId,
                                                String date, Map<String, String> metadataValues, String fee, String chargeAmount) {

                        // Callback method triggered when the payment status is received from the payment gateway.
                        // It provides information about the payment transaction.
                        storedFullName = nameEt.getText().toString().trim();
                        storedEmail = emailEt.getText().toString().trim();
                        storedAmount = amountEt.getText().toString().trim();
                        storedInvoiceId = invoiceId;
                        storedPaymentMethod = paymentMethod;
                        storedSenderNumber = senderNumber;
                        storedTransactionId = transactionId;
                        storedDate = date;
                        storedFee = fee;
                        storedChargedAmount = chargeAmount;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Clear previous metadata values to avoid duplication
                                storedMetaKey1 = null;
                                storedMetaValue1 = null;
                                storedMetaKey2 = null;
                                storedMetaValue2 = null;
                                storedMetaKey3 = null;
                                storedMetaValue3 = null;

                                // Iterate through the metadata map and store the key-value pairs
                                for (Map.Entry<String, String> entry : metadataValues.entrySet()) {
                                    String metadataKey = entry.getKey();
                                    String metadataValue = entry.getValue();

                                    if ("CustomMetaData1".equals(metadataKey)) {
                                        storedMetaKey1 = metadataKey;
                                        storedMetaValue1 = metadataValue;
                                    } else if ("CustomMetaData2".equals(metadataKey)) {
                                        storedMetaKey2 = metadataKey;
                                        storedMetaValue2 = metadataValue;
                                    } else if ("CustomMetaData3".equals(metadataKey)) {
                                        storedMetaKey3 = metadataKey;
                                        storedMetaValue3 = metadataValue;
                                    }
                                }

                                // Update UI based on payment status
                                if ("COMPLETED".equals(status)) {
                                    // Handle payment completed case
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    save_db();
                                    resultTv.setText("Payment Complete" + "\n" + "Name:  " + storedFullName + "\n" + "Amount: " + storedAmount);

                                } else if ("PENDING".equals(status)) {
                                    // Handle payment pending case
                                    // Handle payment completed case
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    resultTv.setText("Payment Pending" + "\n" + "Name:  " + storedFullName + "\n" + "Amount: " + storedAmount);

                                } else if ("ERROR".equals(status)) {
                                    // Handle payment error case
                                    // Handle payment completed case
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    resultTv.setText("Payment Error" + "\n" + "Name:  " + storedFullName + "\n" + "Amount: " + storedAmount);
                                }
                            }
                        });
                    }
                };

                UddoktaPay uddoktapay = new UddoktaPay(payWebView, paymentCallback);
                uddoktapay.loadPaymentForm(API_KEY, fullname, email, amount, CHECKOUT_URL, VERIFY_PAYMENT_URL, REDIRECT_URL, CANCEL_URL, metadataMap);

            }
        });
    }

    private void save_db() {

        b_name = nameEt.getText().toString().trim();
        b_email = emailEt.getText().toString().trim();
        b_price = amountEt.getText().toString().trim();
        b_mobile = mobileET.getText().toString().trim();
        b_user_id = uidEt.getText().toString().trim();

        // b_packages = packagesET.getText().toString().trim();
        // b_user_id = user_token_Et.getText().toString().trim();
        b_month = autoCompleteTextView.getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference("user_bill");

        String transaction_id = storedTransactionId;

        if (!(!b_name.isEmpty() || !b_email.isEmpty() || !b_price.isEmpty() || !b_mobile.isEmpty() || !b_packages.isEmpty() || !b_user_id.isEmpty())) {
            Toast.makeText(this, "please fill all field", Toast.LENGTH_SHORT).show();
        } else if (b_packages.isEmpty()) {
            // packagesET.setError("Please select the month");
        } else {

            // Its a time and date code show our user and admin
            // amra date and time init kore database e save korchi
            Calendar calForYear = Calendar.getInstance();
            SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
            String year = currentYear.format(calForYear.getTime());

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            String time = currentTime.format(calForTime.getTime());


            String Key = reference.push().getKey();

            if (Key != null) {

                BillModels billModels = new BillModels(b_name, b_user_id, b_packages, b_mobile, b_month, b_email, b_price, date, time, year, transaction_id, FirebaseAuth.getInstance().getCurrentUser().getUid());

                // these data save on new uid and also user id
                reference.child(Key).setValue(billModels);

                // these data save on user id
                // reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(billModels);

                Toast.makeText(this, "save successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "save un-successful", Toast.LENGTH_SHORT).show();
            }

        }
    }

}