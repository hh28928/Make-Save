package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hammadhanif.cs_477_final_project.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class Paymentactivity extends AppCompatActivity {


    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_ID);
    Button paynow;
    EditText payment_value;
    String amount;

    @Override
    public void onDestroy() {
        this.stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);

        paynow = (Button) findViewById(R.id.button_pay);
        payment_value = (EditText) findViewById(R.id.enteramount);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        this.startService(intent);

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment_value.getText().toString().length()==0)
                {
                    Toast.makeText(Paymentactivity.this,"Enter an amount please",Toast.LENGTH_SHORT).show();
                    return;
                }
                payment();

            }
        });
    }



    private void payment() {
        amount=payment_value.getText().toString();
        PayPalPayment payPalPayment =
                new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                        "USD","Pay for service",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PAYPAL_REQUEST_CODE)
        {
            if(resultCode==-1)
            {
                PaymentConfirmation confirmation =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null)
                {
                    try{
                        String details = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails",details)
                                .putExtra("PaymentAmount",amount));


                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            else if(resultCode== AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();

            }


        }
        else if(requestCode==PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(this,"INVALID",Toast.LENGTH_SHORT).show();
        }
    }
}
