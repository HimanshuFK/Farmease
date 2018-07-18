package com.farmease.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.farmease.app.HomeActivity;
import com.farmease.app.R;
import com.farmease.app.activity.ForgetPasswordActivity;
import com.farmease.app.activity.OtpActivity;
import com.farmease.app.bean.BeanLogin;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    @BindView(R.id.login_button)
    LoginButton fbBtn;
    @BindView(R.id.signin_google)
    SignInButton signin_google;
    @BindView(R.id.edtxt_email)
    EditText edtEmail;
    @BindView(R.id.edtxt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.txt_forgetpassword)
    TextView txtForgetPass;
    @BindView(R.id.txt_skip)
    TextView txtSkip;
    @BindView(R.id.layout_fb)
    FrameLayout layoutFb;
    @BindView(R.id.layout_gmail)
    FrameLayout layoutGmail;
    @BindView(R.id.img_showPassword)
    ImageView imgShowPassword;
    private Unbinder unbinder;
    private boolean showPass = true;
    private CustomProgressBar progressBar;
    private GoogleApiClient googleApiClient;
    private CallbackManager callbackManager;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        fbBtn.setReadPermissions("email");
        signin_google.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
        fbBtn.setOnClickListener(this);
        imgShowPassword.setOnClickListener(this);
        layoutFb.setOnClickListener(this);
        layoutGmail.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        txtForgetPass.setOnClickListener(this);
        progressBar = CustomProgressBar.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                if (!Utility.isValidEmail(email)) {
                    edtEmail.setError("Invalid email");
                } else if (password.length() == 0) {
                    edtPassword.setError("Password Required");
                } else {
                    if (Utility.isInternetConnected(LoginActivity.this)){
                        userSignIn(email, password);
                    }else {
                        AppToast.showToast(LoginActivity.this,"No Internet Found",Toast.LENGTH_SHORT);
                    }

                }

            }
        });

    }

    //for google signin
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Utility.savePref(LoginActivity.this, Constants.username, personName);
            Utility.savePref(LoginActivity.this, Constants.useremail, email);

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            if (Utility.isInternetConnected(LoginActivity.this)){
                userGmailSignin(email);
            }else {
                AppToast.showToast(LoginActivity.this,"No Internet Found",Toast.LENGTH_SHORT);
            }
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // BeanLogin returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void userGmailSignin(final String email) {
        progressBar.showProgress(LoginActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanLogin> call = service.userGmail(email);

        call.enqueue(new Callback<BeanLogin>() {
            @Override
            public void onResponse(Call<BeanLogin> call, Response<BeanLogin> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    Log.e("token", response.body().getResult().getToken());
                    Utility.savePref(LoginActivity.this, Constants.token, response.body().getResult().getToken());
                    Utility.savePref(LoginActivity.this, Constants.userId, response.body().getResult().getId());
                    Utility.savePref(LoginActivity.this, Constants.login_type, "gmail");
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Utility.saveBooleanDataTosharedPrefences(LoginActivity.this, Constants.LOGIN, true);
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(LoginActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanLogin> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void userSignIn(final String email, final String password) {

        progressBar.showProgress(LoginActivity.this);


        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanLogin> call = service.userLogin(email, password);

        call.enqueue(new Callback<BeanLogin>() {
            @Override
            public void onResponse(Call<BeanLogin> call, Response<BeanLogin> response) {
                progressBar.hideProgress();
                Log.e("values", "" + email + password);
                if (response.isSuccessful()) {

                    Log.e("token", response.body().getResult().getToken());
                    Utility.savePref(LoginActivity.this, Constants.login_type, "manual");
                    Utility.savePref(LoginActivity.this, Constants.userId, response.body().getResult().getId());
                    Utility.savePref(LoginActivity.this, Constants.token, response.body().getResult().getToken());
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Utility.saveBooleanDataTosharedPrefences(LoginActivity.this, Constants.LOGIN, true);
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(LoginActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanLogin> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //fb login

    private void userFBSignin(final String fbId) {

        progressBar.showProgress(LoginActivity.this);

        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Call<BeanLogin> call = service.userFB(fbId,"Android",android_id);

        call.enqueue(new Callback<BeanLogin>() {
            @Override
            public void onResponse(Call<BeanLogin> call, Response<BeanLogin> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    Log.e("token", response.body().getResult().getToken());
                    Utility.savePref(LoginActivity.this, Constants.login_type, "fb");
                    Utility.savePref(LoginActivity.this, Constants.userId, response.body().getResult().getId());
                    Utility.savePref(LoginActivity.this, Constants.token, response.body().getResult().getToken());
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Utility.saveBooleanDataTosharedPrefences(LoginActivity.this, Constants.LOGIN, true);
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(LoginActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanLogin> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void facebookLogin() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity Response ", response.toString());
                                try {
                                    String id = object.optString("id");
                                    String fName = object.optString("first_name");
                                    String lName = object.optString("last_name");
                                    String email = object.optString("email");
                                    JSONObject picture = object.getJSONObject("picture").getJSONObject("data");
                                    String pictureUrl = picture.getString("url");
                                    String name = fName + " " + lName;
                                    Utility.savePref(LoginActivity.this,Constants.userimg,pictureUrl);
                                    if (Utility.isInternetConnected(LoginActivity.this)){
                                        userFBSignin(id);
                                    }else {
                                        AppToast.showToast(LoginActivity.this,"No Internet Found",Toast.LENGTH_SHORT);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_gmail:
                signIn();
                break;
            case R.id.layout_fb:
               // LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                facebookLogin();
                break;
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.txt_forgetpassword:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.txt_skip:
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                break;
            case R.id.img_showPassword:
                int start,end;
                if (showPass) {
                    start=edtPassword.getSelectionStart();
                    end=edtPassword.getSelectionEnd();
                    edtPassword.setTransformationMethod(null);
                    edtPassword.setSelection(start,end);
                    showPass=false;
                }else {
                    start=edtPassword.getSelectionStart();
                    end=edtPassword.getSelectionEnd();
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    showPass=true;
                    edtPassword.setSelection(start,end);
                }
                break;

        }
    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
