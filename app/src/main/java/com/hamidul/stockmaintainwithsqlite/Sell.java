package com.hamidul.stockmaintainwithsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class Sell extends AppCompatActivity {

    RecyclerView listView;
    DatabaseHelper dbHelper;
    ArrayList<HashMap<String,String>> arrayList;
    HashMap<String, String> hashMap;
    LinearLayout textView;
    boolean isOnText = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);
        textView = findViewById(R.id.textView);

        getFinalStock();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnText && SellMemo.sellList.size()>=1){
                    ViewDetails.PURCHASE = false;
                    startActivity(new Intent(Sell.this, ViewDetails.class));
                }
                else {
                    Toast.makeText(Sell.this, "Input Some Item", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void getFinalStock(){

        isOnText = true;

        SellMemo.sellList = new ArrayList<>();

        Cursor cursor = dbHelper.getAllStockData();

        if (cursor!=null && cursor.getCount()>0){

            arrayList = new ArrayList();

            while (cursor.moveToNext()){

                String sku = cursor.getString(1);
                int unit = cursor.getInt(2);
                double time = cursor.getDouble(3);

                hashMap = new HashMap();
                hashMap.put("sku",sku);
                hashMap.put("unit", String.valueOf(unit));
                hashMap.put("time", String.valueOf(time));
                arrayList.add(hashMap);

            }

            listView.setAdapter(new MyAdapter());
            listView.setLayoutManager(new LinearLayoutManager(Sell.this));

        }


    }


    public class MyAdapter extends RecyclerView.Adapter<Sell.MyAdapter.myViewHolder>{


        @NonNull
        @Override
        public Sell.MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.item,parent,false);

            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull Sell.MyAdapter.myViewHolder holder, int position) {

            hashMap = (HashMap<String, String>) arrayList.get(position);
            String sku = hashMap.get("sku");

            holder.addUnit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isOnText){
                        holder.okay.setVisibility(View.VISIBLE);
                        holder.edUnit.setVisibility(View.VISIBLE);
                        holder.addUnit.setVisibility(View.GONE);
                        holder.edUnit.requestFocus();
                        isOnText = false;

                    }else {
                        Toast.makeText(Sell.this, "Complete Previous Task", Toast.LENGTH_SHORT).show();
                    }

                }

            });

            holder.edUnit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string = s.toString();

                    if (!string.isEmpty() && string.startsWith("0")){

                        s.delete(0,1);

                    }

                }
            });


            holder.okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOnText = true;
                    holder.okay.setVisibility(View.GONE);
                    holder.edUnit.setVisibility(View.GONE);
                    holder.addUnit.setVisibility(View.GONE);

                    String unit = holder.edUnit.getText().toString();

                    if (!unit.isEmpty()){
                        holder.tvUnit.setVisibility(View.VISIBLE);
                        holder.tvUnit.setText(unit);
                        hashMap = new HashMap<>();
//                        hashMap.put("id", String.valueOf(position));
                        hashMap.put("sku",sku);
                        hashMap.put("unit",unit);
                        SellMemo.sellList.add(hashMap);
                    }else {
                        holder.addUnit.setVisibility(View.VISIBLE);
                    }

                }
            });

            holder.skuName.setText(sku);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            public TextView skuName,tvUnit;
            EditText edUnit;
            ImageView addUnit,okay;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
                edUnit = itemView.findViewById(R.id.edUnit);
                tvUnit = itemView.findViewById(R.id.tvUnit);
                addUnit = itemView.findViewById(R.id.addUnit);
                okay = itemView.findViewById(R.id.okay);

            }
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getFinalStock();
    }



}