package com.farmease.app.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.widget.EditText;
import android.widget.Toast;

import com.farmease.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationSearchActivity extends AppCompatActivity implements PlaceautocompleteAdapter.PlaceautoCompleteInterface{

    @BindView(R.id.autocomplete_txt)
    EditText txt_search;

    @BindView(R.id.recyclerview_search)
    RecyclerView recyclerView;
    private Unbinder unbinder;
    private PlaceautocompleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        unbinder= ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        adapter=new PlaceautocompleteAdapter(LocationSearchActivity.this,R.layout.list_item,null);
        adapter.getFilter();

        LinearLayoutManager verticalLayoutmanager
                = new LinearLayoutManager(LocationSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutmanager);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    //mClear.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        Log.e("work","yes");
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    //mClear.setVisibility(View.GONE);
                }


                if (!s.toString().equals("")) {
                    adapter.getFilter().filter(s.toString());
                    // Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();
                } else  {
                    // Toast.makeText(mContext, "No BeanLogin Found", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), Constant.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onPlaceClick(ArrayList<PlaceautocompleteAdapter.PlaceAuto> mResultList, int position) {
        Toast.makeText(this, mResultList.get(position).description, Toast.LENGTH_SHORT).show();
    }
}