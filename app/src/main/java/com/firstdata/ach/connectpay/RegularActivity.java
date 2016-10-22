package com.firstdata.ach.connectpay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firstdata.ach.connectpay.model.BAARequest;
import com.firstdata.ach.connectpay.model.ConnectPay;
import com.firstdata.ach.connectpay.model.EnrollmentRequest;
import com.firstdata.ach.connectpay.model.EstablishRequest;
import com.firstdata.ach.connectpay.model.TokenResponse;
import com.firstdata.ach.connectpay.model.TransactionRequest;
import com.firstdata.ach.connectpay.model.TransactionResponse;
import com.firstdata.rashmi.rashmidemo.R;

import rx.Observer;
import rx.functions.Action0;


public class RegularActivity extends AppCompatActivity {

    private Button payRegularBttn;
    private Button payWithMyBankBttn;
    private EstablishRequest establishRequest;
    private TransactionRequest transactionRequestObj;
    private EnrollmentRequest enrollmentRequest;
    private BAARequest baaRequest;
    private AlertDialog alertDialog;
    private Observer<Object> transactionResponseObjObserver;
    private Observer<TokenResponse> tokenResponseObserver;
    private ProgressDialog progressDialog;
    private FirstApi firstApi = null;

    String transactionId = "";
    AlertDialog.Builder customBuilder;
    private EditText fName, lName, accountNumber, routingNumber;
    private Button next;


    /**
     * Method creates reguler enrolment view also call enrollment flow with the input provided by user
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);
        alertDialog = new AlertDialog.Builder(this).setCancelable(false).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        customBuilder = new AlertDialog.Builder(this);
        firstApi = new FirstApi();

        fName = (EditText) findViewById(R.id.fName);

        lName = (EditText) findViewById(R.id.lName);

        accountNumber = (EditText) findViewById(R.id.accountNumber);

        routingNumber = (EditText) findViewById(R.id.routingNumber);
        next = (Button) findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrollmentRequest enrollmentRequest = populateEnrollmentRequest(fName.getText().toString(), lName.getText().toString(),
                        accountNumber.getText().toString(), routingNumber.getText().toString());
                callRegularEnrollment(enrollmentRequest);

            }
        });
    }

    /**
     * Method triggers enrollment flow with Enrollment Object Also calls micro validate based on Auth answer given by user
     *
     * @param enrollmentRequest
     */
    public void callRegularEnrollment(EnrollmentRequest enrollmentRequest) {
        final EditText editTextName = new EditText(this);
        final LinearLayout linearLayout = new LinearLayout(this);

        firstApi.enrollRegular(enrollmentRequest).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                progressDialog = ProgressDialog.show(RegularActivity.this, "Processing Enrollment call", "Please Wait processing Enrollment Call", true, false);
            }
        }).subscribe(new Observer<TokenResponse>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error", "Enrollment call is failed");
            }

            @Override
            public void onNext(TokenResponse tokenResponse) {
                final String enrollmentId = tokenResponse.getEnrollmentId();
                progressDialog.dismiss();
                Log.d("Response from val", "Tran id " + tokenResponse.getEnrollmentId());
                editTextName.setHint("Enter Auth Answer");
                editTextName.setFocusable(true);
                editTextName.setClickable(true);
                editTextName.setFocusableInTouchMode(true);
                editTextName.setSelectAllOnFocus(true);
                editTextName.setSingleLine(true);
                editTextName.setImeOptions(EditorInfo.IME_ACTION_NEXT);

                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editTextName);

                customBuilder.setCancelable(true);
                customBuilder.setTitle("FirstAPI Response");
                customBuilder.setMessage("You are enrolled with : " + tokenResponse.getEnrollmentId() + System.getProperty("line.separator")+"Validation Pending..");
                customBuilder.setView(linearLayout);
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
                final AlertDialog alert = customBuilder.create();
                alert.show();
                alert.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String userText = editTextName.getText().toString();
                                if (userText != null && userText.trim().length() == 0) {
                                    Toast.makeText(RegularActivity.this, "** Please enter a valid Auth Answer", Toast.LENGTH_SHORT).show();

                                } else {
                                    alert.dismiss();
                                    int authAnswer = Integer.parseInt(userText);
                                    BAARequest baaRequest = populateMicroValidate(enrollmentId, authAnswer);
                                    firstApi.callMicroValidate(baaRequest).doOnSubscribe(new Action0() {
                                        @Override
                                        public void call() {
                                            progressDialog = ProgressDialog.show(RegularActivity.this, "Micro Validation is in process", "Please Wait processing micro validation call", true, false);
                                        }
                                    }).subscribe(new Observer<TokenResponse>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onNext(TokenResponse tokenResponse) {
                                            progressDialog.dismiss();
                                            String enrollmentId = tokenResponse.getEnrollmentId();
                                            Intent intent = new Intent();
                                            intent.putExtra("MySecData", enrollmentId);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                }

                            }
                        });
            }


        });
    }


    public void callTransaction(String enrollmentId) {
        final EditText editTextName = new EditText(this);
        final LinearLayout linearLayout = new LinearLayout(this);
        performTransaction(editTextName, linearLayout, enrollmentId);
    }

    /**
     * Method triggers transaction call with enrollment Id and amount entered by user
     *
     * @param editTextName
     * @param linearLayout
     * @param enrollmentId
     */
    public void performTransaction(final EditText editTextName, LinearLayout linearLayout, final String enrollmentId) {
        customBuilder = new AlertDialog.Builder(this);
        editTextName.setHint("Enter Amount...");
        editTextName.setFocusable(true);
        editTextName.setClickable(true);
        editTextName.setFocusableInTouchMode(true);
        editTextName.setSelectAllOnFocus(true);
        editTextName.setSingleLine(true);
        editTextName.setImeOptions(EditorInfo.IME_ACTION_NEXT);


        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(editTextName);


        customBuilder.setCancelable(true);
        customBuilder.setTitle("FirstAPI Response");
        customBuilder.setMessage("You are enrolled with :" + enrollmentId);
        customBuilder.setView(linearLayout);
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
        final AlertDialog alert = customBuilder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userText = editTextName.getText().toString();
                        if (userText != null && userText.trim().length() == 0) {
                            Toast.makeText(RegularActivity.this, "** Please enter a valid Amount", Toast.LENGTH_SHORT).show();

                        } else {
                            alert.dismiss();
                            TransactionRequest transactionRequestObj = populateTransaction(enrollmentId, userText);
                            firstApi.createPrimaryTransaction(transactionRequestObj).doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    progressDialog = ProgressDialog.show(RegularActivity.this, "Transaction is in process", "Please Wait processing transaction call", true, false);
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
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = getAlert();
                                    builder.setMessage("Transaction is completed with transaction id "+ transactionResponseObj.getTransactionId()+" Would you like to refund ?");
                                    final AlertDialog alert = builder.create();
                                    alert.show();
                                    alert.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                                            new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    alert.dismiss();
                                                    performRefund(enrollmentId, transactionResponseObj.getTransactionId(), transactionResponseObj.getAmount());

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

                    }
                });


    }

    /**
     * Method triggers refund transaction call with enrollment Id,transaction id  and amount entered by user
     *
     * @param enrollmentId
     * @param transactionId
     * @param amount
     */
    public void performRefund(final String enrollmentId, String transactionId, String amount) {
        customBuilder = new AlertDialog.Builder(this);
        TransactionRequest transactionRequestObj = populateTransaction(enrollmentId, amount);
        transactionRequestObj.setTransactionType("refund");
        firstApi.createSecondaryTransaction(transactionId, transactionRequestObj).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                progressDialog = ProgressDialog.show(RegularActivity.this, "Refund is in process", "Please Wait refund transaction call", true, false);
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
                progressDialog.dismiss();
                progressDialog.dismiss();
                AlertDialog.Builder builder2 = getAlertwithOK();
                builder2.setMessage("Refund is completed for amount "+ transactionResponseObj.getAmount());
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

    private AlertDialog.Builder getAlert() {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(this);
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

    private AlertDialog.Builder getAlertwithOK() {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(this);
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
     * Populate Transaction object based enrollment id and amount entered by user
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
     * Populate Micro validate object with enrollment id and authanswer entered by user
     *
     * @param enrollmentId
     * @param authAnswer
     * @return
     */
    public BAARequest populateMicroValidate(String enrollmentId, int authAnswer) {
        baaRequest = new BAARequest();
        baaRequest.setEnrollmentId(enrollmentId);
        baaRequest.setAuthenticationAnswer(authAnswer);
        return baaRequest;
    }

    /**
     * Populate Enrollment Object with user input
     *
     * @param firstName
     * @param lastName
     * @param accountNumber
     * @param routingNumber
     * @return
     */
    public EnrollmentRequest populateEnrollmentRequest(String firstName, String lastName, String accountNumber, String routingNumber) {
        enrollmentRequest = new EnrollmentRequest();

        enrollmentRequest.setFirstName(firstName);
        enrollmentRequest.setLastName(lastName);

        EnrollmentRequest.EnrollmentApp enrollmentApp = new EnrollmentRequest.EnrollmentApp();
        enrollmentApp.setApplication("PayeezyACH");
        enrollmentApp.setDevice("DeviceXYZ123");
        enrollmentApp.setImei("IMEI76856745");
        enrollmentApp.setApplicationId("76ed6b08-224d-4f2e-9771-28cb5c9f26bd");
        enrollmentApp.setDeviceId("DeviceID65657");
        enrollmentApp.setIpAddress("192.168.1.1");
        enrollmentApp.setTrueIpAddress("192.168.1.1");
        enrollmentApp.setOrganizationId("FirtsDataInternalUAID9999");
        enrollmentRequest.setEnrollmentApplication(enrollmentApp);

        EnrollmentRequest.EnrollmentUser user = new EnrollmentRequest.EnrollmentUser();
        user.setRoutingNumber(routingNumber);
        user.setAccountNumber(accountNumber);
        enrollmentRequest.setEnrollmentUser(user);

        EnrollmentRequest.Address address = new EnrollmentRequest.Address();
        address.setAddressLine1("7979 Westheimer");
        address.setState("TX");
        address.setCity("Houston");
        address.setCountry("0840");
        address.setEmail("jsmith@email.com");
        address.setZip("77063");

        EnrollmentRequest.Address.Phone phone = new EnrollmentRequest.Address.Phone();
        phone.setType("MOBILE");
        phone.setNumber("9999955555");
        address.setPhone(phone);

        enrollmentRequest.setAddress(address);
        return enrollmentRequest;
    }

}


