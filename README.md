##Working with Connect Pay android SDK

###Steps

Add the ConnectPay-Android.aar to your project

Connect pay SDK supports two type of enrollment flows

- Reguler flow
- Pay with my bank flow



##Initializing the SDK

```
-  FirstApi firstApi = FirstApi(String apiKey, String secret, String token);

```
Once you initialized the sdk, below operations can be called

## Working with Pay with my bank flow

```
- public void performEnrollment(final ViewGroup view, final Activity act,final EnrollmentCallback enrollmentCallback)

Input parameters - ViewGroup(Where you want to append web view coming from Pay with my bank)
				 - Activity (This is useful to create instanse of PayWithMyBank class)
				 - EnrollmentCallback (Interface which is getting callback for sucess and error)

-  public Observable<TransactionResponse> createPrimaryTransaction(TransactionRequest transactionRequest)
Input parameter - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it


-  public Observable<TransactionResponse> createSecondaryTransaction(String transactionId, TransactionRequest transactionRequest)
Input parameters - transactionId which is getting return from createPrimaryTransaction operation
				 - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it

```

## Working with Pay with my bank flow

```
- public void performEnrollment(final ViewGroup view, final Activity act,final EnrollmentCallback enrollmentCallback)

Input parameters - ViewGroup(Where you want to append web view coming from Pay with my bank)
				 - Activity (This is useful to create instanse of PayWithMyBank class)
				 - EnrollmentCallback (Interface which is getting callback for sucess and error)

-  public Observable<TransactionResponse> createPrimaryTransaction(TransactionRequest transactionRequest)
Input parameter - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it


-  public Observable<TransactionResponse> createSecondaryTransaction(String transactionId, TransactionRequest transactionRequest)
Input parameters - transactionId which is getting return from createPrimaryTransaction operation
				 - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it

```


## Working with reguler enrollment flow

```
- public Observable<TokenResponse> enrollReguler(EnrollmentRequest enrollmentRequest);
Input parameter - Populate EnrollmentRequest object with properties like address,enrollmentUser and pass it

- public Observable<TokenResponse> callMicroValidate(BAARequest baaRequest)
Input parameter - Populate BAARequest object with properties like authenticationAnswer,enrollmentId and pass it

-  public Observable<TransactionResponse> createPrimaryTransaction(TransactionRequest transactionRequest)
Input parameter - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it


-  public Observable<TransactionResponse> createSecondaryTransaction(String transactionId, TransactionRequest transactionRequest)
Input parameters - transactionId which is getting return from createPrimaryTransaction operation
				 - Populate TransactionRequest object with properties like transactionType,paymentMethod and pass it

```


