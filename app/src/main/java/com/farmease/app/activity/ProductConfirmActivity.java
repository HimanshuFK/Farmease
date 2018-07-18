package com.farmease.app.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.bean.BeanBookedSlot;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.login.LoginActivity;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductConfirmActivity extends AppCompatActivity implements View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edtxt_date)
    EditText edtDate;
    @BindView(R.id.layout_hours)
    LinearLayout layoutHours;
    @BindView(R.id.layout_days)
    LinearLayout layoutDays;
    @BindView(R.id.edtxt_fromDate)
    EditText edtFromdate;
    @BindView(R.id.edtxt_fromTime)
    EditText edtFromTime;
    @BindView(R.id.edtxt_toDate)
    EditText edtToDate;
    @BindView(R.id.edtxt_toTime)
    EditText edtToTime;
    @BindView(R.id.radio_yes)
    RadioButton radioYesT;
    @BindView(R.id.radio_no)
    RadioButton radioNoT;
    @BindView(R.id.radio_yesOperator)
    RadioButton radioYesO;
    @BindView(R.id.radio_noOperator)
    RadioButton radioNoO;
    @BindView(R.id.btn_addtoCart)
    Button btnAddCart;
    @BindView(R.id.txt_chkAvailability)
    TextView txtCheckAvailable;
    @BindView(R.id.txt_tractorPriceValue)
    TextView txtTractorPrice;
    @BindView(R.id.txt_opertorPriceValue)
    TextView txtOperatorPrice;
    @BindView(R.id.txt_grandTotalValue)
    TextView txtGrandTotal;
    @BindView(R.id.txt_totalPriceValue)
    TextView txtTotalPrice;
    @BindView(R.id.txt_noPriceDetails)
    TextView txtNoPriceDetail;
    @BindView(R.id.layout_priceCalculation)
    RelativeLayout layoutAllPrice;
    private CustomProgressBar progressBar;
    private Unbinder unbinder;
    private Date date1, date2;
    private String tractor = "0", operator = "0";
    private int fromHour;
    private List<Integer> list;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    private ArrayList<Calendar> mDataList;
    private float grandAmount, tractorPrice, operatorPrice, perHourRate, totalHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_confirm);
        unbinder = ButterKnife.bind(this);
        progressBar = CustomProgressBar.getInstance();
        edtDate.setOnClickListener(this);
        edtFromdate.setOnClickListener(this);
        edtFromTime.setOnClickListener(this);
        edtToDate.setOnClickListener(this);
        edtToTime.setOnClickListener(this);
        radioYesT.setOnClickListener(this);
        radioNoT.setOnClickListener(this);
        radioYesO.setOnClickListener(this);
        radioNoO.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        radioNoT.setChecked(true);
        radioNoO.setChecked(true);
        txtCheckAvailable.setOnClickListener(this);
        perHourRate = getIntent().getExtras().getFloat("perHourRate");
        tractorPrice = getIntent().getExtras().getFloat("tractor_cost");
        operatorPrice = getIntent().getExtras().getFloat("operator_cost");
        checkBookedSlot();

    }

    public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtxt_date:
                openDatePicker(edtDate);
                break;
            case R.id.edtxt_fromTime:
                if (edtDate.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select date first", Toast.LENGTH_SHORT);
                } else {
                    openTimePicker(edtFromTime, 1);
                }

                break;
            case R.id.edtxt_toTime:
                if (edtDate.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select date first", Toast.LENGTH_SHORT);
                } else if (edtFromTime.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select from time first", Toast.LENGTH_SHORT);
                } else {
                    openTimePicker(edtToTime, 2);

                }
                break;
            case R.id.radio_yes:
                tractor = "1";
                radioYesT.setSelected(true);
                radioYesT.setChecked(true);
                radioNoT.setChecked(false);
                radioNoT.setSelected(false);
                if (edtToTime.getText().toString().length() != 0) {
                    priceCalculation(edtFromTime.getText().toString(), edtToTime.getText().toString());
                }
                break;
            case R.id.radio_no:
                tractor = "0";
                radioYesT.setSelected(false);
                radioNoT.setSelected(true);
                radioYesT.setChecked(false);
                radioNoT.setChecked(true);
                if (edtToTime.getText().toString().length() != 0) {
                    priceCalculation(edtFromTime.getText().toString(), edtToTime.getText().toString());
                }
                break;
            case R.id.radio_yesOperator:
                operator = "1";
                radioYesO.setSelected(true);
                radioYesO.setChecked(true);
                radioNoO.setChecked(false);
                radioNoO.setSelected(false);
                if (edtToTime.getText().toString().length() != 0) {
                    priceCalculation(edtFromTime.getText().toString(), edtToTime.getText().toString());
                }
                break;
            case R.id.radio_noOperator:
                operator = "0";
                radioYesO.setSelected(false);
                radioYesO.setChecked(false);
                radioNoO.setChecked(true);
                radioNoO.setSelected(true);
                if (edtToTime.getText().toString().length() != 0) {
                    priceCalculation(edtFromTime.getText().toString(), edtToTime.getText().toString());
                }
                break;
            case R.id.btn_addtoCart:
                if (edtDate.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select date first", Toast.LENGTH_SHORT);
                } else if (edtFromTime.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select from time first", Toast.LENGTH_SHORT);
                } else if (edtToTime.getText().toString().length() == 0) {
                    AppToast.showToast(ProductConfirmActivity.this, "Select to time first", Toast.LENGTH_SHORT);
                } else {
                    addCart(totalHours);
                }

                break;
            case R.id.txt_chkAvailability:

                bookedSlot();
                //openDatePicker(edtDate);
                break;
        }
    }

    private void bookedSlot() {
        final Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(ProductConfirmActivity.this,
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.setMinDate(now);
        }
        Calendar[] days = new Calendar[list.size()];

        for (int i=0;i<list.size();i++){
            int j;
            j=list.get(i)-now.get(Calendar.DAY_OF_MONTH);
            Log.e("val",j+",Booked"+list.get(i)+",Today"+now.get(Calendar.DAY_OF_MONTH));
            Calendar date=Calendar.getInstance();
            date.add(Calendar.DATE,j);
            days[i]=date;
        }
        dpd.setDisabledDays(days);
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    private void checkAvailability() {

        progressBar.showProgress(ProductConfirmActivity.this);

        final String date = edtDate.getText().toString().trim();

        final int id = getIntent().getExtras().getInt("id");
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.checkAvailablity(id, date);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    // Toast.makeText(ProductConfirmActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Date is not Availabled", Toast.LENGTH_LONG).show();
                        edtDate.setText("");
                    }
                  /*  RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductConfirmActivity.this);
                    errorHandler.responseOnError(statusCode);*/
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkBookedSlot() {

        progressBar.showProgress(ProductConfirmActivity.this);

        final int id = getIntent().getExtras().getInt("id");
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanBookedSlot> call = service.bookedSlot("product/bookedSlots/" + id);

        call.enqueue(new Callback<BeanBookedSlot>() {
            @Override
            public void onResponse(Call<BeanBookedSlot> call, Response<BeanBookedSlot> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    // Toast.makeText(ProductConfirmActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   ArrayList<BeanBookedSlot.Slots> mData=response.body().getResult();
                    list=new ArrayList<>();
                   for (int i=0;i<mData.size();i++){

                       Calendar c=Calendar.getInstance();
                       int m=c.get(Calendar.MONTH);
                       String dtStart = mData.get(i).getCreated_at();
                       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
                       SimpleDateFormat output = new SimpleDateFormat("dd", Locale.ENGLISH);
                       SimpleDateFormat month = new SimpleDateFormat("MM", Locale.ENGLISH);
                       try {
                           Date date = format.parse(dtStart);

                           if (m==Integer.valueOf(month.format(date))){
                               list.add(Integer.valueOf(output.format(date)));
                           }
                          /* else {
                               int sub=Integer.valueOf(month.format(date))-m;
                               list.add(Integer.valueOf(output.format(date))+30*sub);
                           }*/


                       } catch (ParseException e) {
                           e.printStackTrace();
                       }

                   }

                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductConfirmActivity.this);
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanBookedSlot> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCart(final float totalHours) {

        progressBar.showProgress(ProductConfirmActivity.this);

        //priceCalculation();
        String date = edtDate.getText().toString().trim();
        String toHour = edtToTime.getText().toString();
        String fromHour = edtFromTime.getText().toString();
        String grandAmount = txtGrandTotal.getText().toString().replace("$","");
        float perHourRate = getIntent().getExtras().getFloat("perHourRate");
        int id = getIntent().getExtras().getInt("id");
        String token= Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId= Utility.getPref(ProductConfirmActivity.this, Constants.userId,null);
        Log.e("token",token);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.cartAdd(id, totalHours, perHourRate, toHour, fromHour, operator, tractor, grandAmount, date,userId,token);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    Toast.makeText(ProductConfirmActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ProductConfirmActivity.this,CartActivity.class));
                    finish();
                } else {
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductConfirmActivity.this);
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private float priceCalculation(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");

        Log.e("dates", time1 + "," + time2);
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long difference = date2.getTime() - date1.getTime();

            Log.e("differ", "" + difference);
            // int days = (int) (difference / (1000 * 60 * 60 * 24));
            float minutes = (int) ((difference / (1000 * 60)) % 60);
            float hours = (int) ((difference / (1000 * 60 * 60)));
            // hours = (hours < 0 ? -hours : hours);
            totalHours = hours + minutes / 60;
            Log.e("totalhour", "" + totalHours);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        float totalPrice = perHourRate * totalHours;
        txtTotalPrice.setText("$ " + totalPrice);
        if (tractor.equalsIgnoreCase("1")) {
            totalPrice = totalPrice + tractorPrice;
            txtTractorPrice.setText("$ " + tractorPrice);
        } else {
            txtTractorPrice.setText("$ 0");
        }
        if (operator.equalsIgnoreCase("1")) {
            totalPrice = totalPrice + operatorPrice;
            txtOperatorPrice.setText("$ " + operatorPrice);
        } else {
            txtOperatorPrice.setText("$ 0");
        }
        grandAmount = totalPrice;
        txtGrandTotal.setText("$ " + grandAmount);
        layoutAllPrice.setVisibility(View.VISIBLE);
        txtNoPriceDetail.setVisibility(View.GONE);
        return grandAmount;
    }

    public void openDatePicker(final EditText edtxt) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(ProductConfirmActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        view.setMinDate(System.currentTimeMillis() - 1000);
                        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtxt.setText(date);
                        checkAvailability();


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void openTimePicker(final EditText edtxt, final int value) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ProductConfirmActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM;
                if (selectedHour < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                if (value == 1) {
                    fromHour = selectedHour;
                }
                if (selectedMinute < 10) {
                    if (value == 2) {
                        if (fromHour < selectedHour) {
                            edtxt.setText(selectedHour + " : 0" + selectedMinute + " " + AM_PM);
                        } else {
                            AppToast.showToast(ProductConfirmActivity.this, "Not Possible", Toast.LENGTH_SHORT);
                        }
                    } else {
                        edtxt.setText(selectedHour + " : 0" + selectedMinute + " " + AM_PM);
                    }

                } else {
                    if (value == 2) {
                        if (fromHour < selectedHour) {
                            edtxt.setText(selectedHour + " : " + selectedMinute + " " + AM_PM);
                        } else {
                            AppToast.showToast(ProductConfirmActivity.this, "Not Possible", Toast.LENGTH_SHORT);
                        }
                    } else {
                        edtxt.setText(selectedHour + " : " + selectedMinute + " " + AM_PM);
                    }
                }
                if (edtToTime.getText().toString().length() != 0) {
                    priceCalculation(edtFromTime.getText().toString(), edtToTime.getText().toString());
                }
            }
        }, hour, minute, true);//Yes 24 hour time


        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public float differenceTime(String time1, String time2) {
        float differenc = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Log.e("dates", time1 + "," + time2);
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long difference = date2.getTime() - date1.getTime();

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
                    / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            differenc = Float.valueOf(hours);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return differenc;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        edtDate.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
    }
}
