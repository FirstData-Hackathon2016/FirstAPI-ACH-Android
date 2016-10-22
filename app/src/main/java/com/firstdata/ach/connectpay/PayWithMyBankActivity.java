package com.firstdata.ach.connectpay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firstdata.ach.connectpay.interfaces.EnrollmentCallback;
import com.firstdata.ach.connectpay.model.ConnectPay;
import com.firstdata.ach.connectpay.model.EnrollmentRequest;
import com.firstdata.ach.connectpay.model.EstablishRequest;
import com.firstdata.ach.connectpay.model.TransactionRequest;
import com.firstdata.ach.connectpay.model.TransactionResponse;
import com.firstdata.ach.connectpay.model.ValidateRequest;
import com.firstdata.rashmi.rashmidemo.R;

import rx.Observer;
import rx.functions.Action0;


public class PayWithMyBankActivity extends ActionBarActivity {
    final Context context = this;
    private String jsonResponse;
    String accessId = null;
    String returnUrl = null;
    String merchantId = null;
    String description = null;
    String currency = null;
    String amount = null;

    private ProgressDialog progressDialog;
    private EstablishRequest establishRequest;
    private FirstApi firstApi = null;
    ValidateRequest validateRequest = null;
    AlertDialog.Builder customBuilder;

    boolean flag = false;
    private AlertDialog alertDialog;
    EnrollmentCallback enrollmentCallback;
    String enrollmentId;
    EditText editText;


    /**
     * Method calls establish
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Activity self = this;

        firstApi = new FirstApi();
        alertDialog = new AlertDialog.Builder(this).setCancelable(false).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paywithmybank);
        firstApi.performEnrollment((ViewGroup) findViewById(R.id.paywithmybankLayout), self, new EnrollmentCallback() {
            @Override
            public void onSuccess(final EnrollmentRequest enrollmentRequest) {
               String enrollmentId = enrollmentRequest.getEnrollmentId();
                Intent intent = new Intent();
                intent.putExtra("MyData", enrollmentId);
                setResult(RESULT_OK, intent);
                finish();

            }

            @Override
            public void onFailure(String exception) {
                Log.e("Error in callback -> ", exception);
            }
        });
    }


    /**
     * This method triggers transaction flow based on enrollment id and amount enterd by user
     *
     * @param enrollmentId
     */
    public void performTransaction(final String enrollmentId,final String userText,final Activity act) {
        FirstApi firstApi = new FirstApi();
        final ProgressDialog dialog = new ProgressDialog(act);

        TransactionRequest transactionRequestObj = populateTransaction(enrollmentId, userText);
        firstApi.createPrimaryTransaction(transactionRequestObj).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                dialog.setMessage("Please Wait processing transaction call");
                dialog.setTitle("Transaction is in process");
                dialog.show();

            }
        }).subscribe(new Observer<TransactionResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error in transaction: ", e.getMessage());
            }

            @Override
            public void onNext(final TransactionResponse transactionResponseObj) {


                dialog.dismiss();
                AlertDialog.Builder builder = getAlert(act);
                builder.setMessage("Transaction is completed with transaction id " + transactionResponseObj.getTransactionId() + " Would you like to refund ?");
                final AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                performRefund(enrollmentId, transactionResponseObj.getTransactionId(), transactionResponseObj.getAmount(),act);

                            }

                        });
                alert.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                finish();

                            }

                        });

            }


        });
    }

    private AlertDialog.Builder getAlert(Activity act) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(act);
        customBuilder.setCancelable(true);
        customBuilder.setTitle("FirstAPI Response");
        customBuilder.setInverseBackgroundForced(true);
        customBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

            }
        });
        customBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        return customBuilder;

    }

    private AlertDialog.Builder getAlertwithOK(Activity act) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(act);
        customBuilder.setCancelable(true);
        customBuilder.setTitle("FirstAPI Response");
        customBuilder.setInverseBackgroundForced(true);
        customBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

            }
        });
        return customBuilder;

    }

    /**
     * Method triggers refund transaction call with enrollment Id,transaction id  and amount entered by user
     *
     * @param enrollmentId
     * @param transactionId
     * @param amount
     */
    public void performRefund(final String enrollmentId, String transactionId, String amount,final Activity act) {
        FirstApi firstApi = new FirstApi();
        final ProgressDialog dialog = new ProgressDialog(act);

        TransactionRequest transactionRequestObj = populateTransaction(enrollmentId, amount);
        transactionRequestObj.setTransactionType("refund");
        firstApi.createSecondaryTransaction(transactionId, transactionRequestObj).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                dialog.setMessage("Please Wait refund transaction call");
                dialog.setTitle("Refund is in process");
                dialog.show();
                //progressDialog = ProgressDialog.show(act, "Refund is in process", "Please Wait refund transaction call", true, false);
            }
        }).subscribe(new Observer<TransactionResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(TransactionResponse transactionResponseObj) {
                dialog.dismiss();
                AlertDialog.Builder builder2 = getAlertwithOK(act);
                builder2.setMessage("Refund is completed for amount " + transactionResponseObj.getAmount());
                final AlertDialog alert2 = builder2.create();
                alert2.show();
                alert2.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                alert2.dismiss();
                            }

                        });

            }

        });
    }

    /**
     * Populates transaction object based on enrollment id and amount entered by user
     *
     * @param enrollmentId
     * @param amount
     * @return
     */
    protected TransactionRequest populateTransaction(String enrollmentId, String amount) {

        TransactionRequest transactionRequestObj = new TransactionRequest();

        transactionRequestObj.setTransactionType("purchase");
        transactionRequestObj.setPaymentMethod("connect_pay");
        if (amount != null && !amount.isEmpty()) {
            transactionRequestObj.setAmount(amount);
        } else {
            transactionRequestObj.setAmount("2700");
        }
        transactionRequestObj.setCurrency("USD");
        ConnectPay cp = new ConnectPay();
        cp.setConnectPayNumber(enrollmentId);
        transactionRequestObj.setConnectPay(cp);
        return transactionRequestObj;

    }


    /**
     * Populates validate object based on PWMB call response
     *
     * @param transactionId
     * @param requestSignature
     * @param merchantReference
     * @param panel
     * @param status
     * @param transactionType
     * @return
     */
    private ValidateRequest populate(final String transactionId, final String requestSignature, final String merchantReference,
                                     final String panel,
                                     final String status, final String transactionType) {
        ValidateRequest req = new ValidateRequest();
        req.setTransactionId(transactionId);
        req.setCallType("pwmb.getAll");
        req.setRequestSignature(requestSignature);
        req.setTransactionType(transactionType);
        req.setMerchantReference(merchantReference);
        req.setStatus(status);
        req.setReturnUrl("msg://return");
        req.setPanel(panel);
        req.setVerified("true");
        req.setPaymentType("6");
        req.setType("1");
        return req;
    }


}