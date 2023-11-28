package com.trodev.wifibillmanageruser;

import static android.icu.number.NumberFormatter.UnitWidth.FULL_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.help5g.uddoktapaysdk.UddoktaPay;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener;

import java.util.HashMap;
import java.util.Map;

public class PayBillActivity extends AppCompatActivity implements SSLCTransactionResponseListener {


    // Constants for payment
    private static final String API_KEY = "982d381360a69d419689740d9f2e26ce36fb7a50";
    private static final String CHECKOUT_URL = "https://sandbox.uddoktapay.com/api/checkout-v2";
    private static final String VERIFY_PAYMENT_URL = "https://sandbox.uddoktapay.com/api/verify-payment";
    private static final String REDIRECT_URL = "https://uddoktapay.com";
    private static final String CANCEL_URL = "https://uddoktapay.com";

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
    TextInputEditText nameEt, emailEt, amountEt;
    LinearLayout webLayout, uiLayout;
    WebView payWebView;
    TextView resultTv;

    private SSLCommerzInitialization sslCommerzInitialization;
    private SSLCAdditionalInitializer additionalInitialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);

        resultTv = findViewById(R.id.resultTv);
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        amountEt = findViewById(R.id.amountEt);
        webLayout = findViewById(R.id.webLayout);
        uiLayout = findViewById(R.id.uiLayout);
        payWebView = findViewById(R.id.payWebView);
        payBtn = findViewById(R.id.payBtn);

        String fullname = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();


/*        payBtn.setOnClickListener(new View.OnClickListener() {
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
        });*/

/*
        final SSLCommerzInitialization sslCommerzInitialization = new SSLCommerzInitialization ("trode65664972f07c1","trode65664972f07c1@ssl", amount, SSLCCurrencyType.BDT,"123456789098765", "yourProductType", SSLCSdkType.TESTBOX);

        final SSLCCustomerInfoInitializer customerInfoInitializer = new SSLCCustomerInfoInitializer("customer name", "customer email",
                "address", "dhaka", "1214", "Bangladesh", "phoneNumber");
*/


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = amountEt.getText().toString().trim();

                if (amount.isEmpty()) {
                    amountEt.setError("Error");

                } else {

                    sslSetUp(amount);
                }

            }
        });

    }

    private void sslSetUp(String amount) {
        sslCommerzInitialization = new SSLCommerzInitialization(
                "trode65664972f07c1",
                "trode65664972f07c1@ssl",
                Double.parseDouble(amount),
                SSLCCurrencyType.BDT,
                amount,
                "eshop",
                SSLCSdkType.TESTBOX
        );

        additionalInitialization = new SSLCAdditionalInitializer();
        additionalInitialization.setValueA("Value Option 1");
        additionalInitialization.setValueB("Value Option 2");
        additionalInitialization.setValueC("Value Option 3");
        additionalInitialization.setValueD("Value Option 4");

        IntegrateSSLCommerz.getInstance(PayBillActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .addAdditionalInitializer(additionalInitialization)
                .buildApiCall(PayBillActivity.this);
    }


    public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {
        Toast.makeText(getApplicationContext(), "Payment success", Toast.LENGTH_SHORT).show();
        Log.i("DONE", "Payment Done");
    }


    public void transactionFail(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    public void closed(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}