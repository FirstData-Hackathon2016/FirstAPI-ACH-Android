package com.firstdata.ach.connectpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firstdata.rashmi.rashmidemo.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button payRegulerBttn;
    private Button paywithMyBankBttn;
    private AlertDialog alertDialog;
    AlertDialog.Builder customBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payRegulerBttn = (Button) findViewById(R.id.payRegulerBttn);
        paywithMyBankBttn = (Button) findViewById(R.id.payPWMBBttn);

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


        payRegulerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, RegulerActivity.class);
                startActivity(intent);

            }

        });

    }

}
