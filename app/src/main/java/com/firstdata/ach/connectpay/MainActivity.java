package com.firstdata.ach.connectpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firstdata.rashmi.rashmidemo.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button payRegularBttn;
    private Button paywithMyBankBttn;
    private Button btnSubmit;
    private AlertDialog alertDialog;
    AlertDialog.Builder customBuilder;
    EditText editText;
    EditText amountText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.enrollmentTXT);


        //final EditText editTextName = new EditText(this);
        amountText = (EditText)findViewById(R.id.amountTXT);

        final LinearLayout linearLayout = new LinearLayout(this);

        payRegularBttn = (Button) findViewById(R.id.payRegularBttn);
        paywithMyBankBttn = (Button) findViewById(R.id.payPWMBBttn);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);


        customBuilder = new AlertDialog.Builder(this);
        alertDialog = new AlertDialog.Builder(this).setCancelable(false).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();

        final MainActivity self = this;




        paywithMyBankBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Map<String, String> data = new HashMap<>();
                Intent intent = new Intent(MainActivity.this, PayWithMyBankActivity.class);
                intent.putExtra("paywithmybank:establish", (Serializable) data);
                startActivityForResult(intent, 100);

            }

        });


        payRegularBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, RegularActivity.class);
                startActivityForResult(intent,100);

            }

        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String userText = editText.getText().toString();
                String amount = amountText.getText().toString();
                if (userText.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "** Please enter a valid Enrollment Id", Toast.LENGTH_SHORT).show();

                }
                if (amount.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "** Please enter a valid amount", Toast.LENGTH_SHORT).show();

                }
                PayWithMyBankActivity act = new PayWithMyBankActivity();
                act.performTransaction(editText.getText().toString(),amountText.getText().toString(),self);

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String myStr = "";
        if(resultCode == RESULT_OK) {
            if(data.getStringExtra("MyData") != null){
                myStr=data.getStringExtra("MyData");
            }
            if(data.getStringExtra("MySecData") != null){
                myStr=data.getStringExtra("MySecData");
            }

            editText.setText(myStr);
        }
    }

}


