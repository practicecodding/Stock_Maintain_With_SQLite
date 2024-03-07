package com.hamidul.stockmaintainwithsqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class SellMemo extends Fragment {


    HashMap<String,String> hashMap;
    RecyclerView listView;
    DatabaseHelper dbHelper;
    boolean isOnText = true;
    Button button;
    public static ArrayList<HashMap<String,String>> sellList = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.sell_memo, container, false);

        listView = myView.findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(getActivity());
        button = myView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnText){

                    for (int x=0; x<sellList.size(); x++){

                        hashMap = sellList.get(x);
                        String sku = hashMap.get("sku");
                        int unit = Integer.parseInt(hashMap.get("unit"));
                        dbHelper.insertPurchase(sku,unit);

                        for (HashMap item : sellList){
                            if (item.get("sku").equals(sku)){

                                int oldUnit = dbHelper.getStockOldUnit(sku);

                                int updateStockUnit = oldUnit-unit;

                                dbHelper.updateStock(sku,updateStockUnit);
                            }
                        }

                    }

                    startActivity(new Intent(getActivity(),MainActivity.class));

                }
                else {

                    Toast.makeText(getActivity(), "Complete Previous Task", Toast.LENGTH_SHORT).show();

                }

            }
        });

        listView.setAdapter(new MyAdapter());
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return myView;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.item,parent,false);

            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {

            hashMap = (HashMap) sellList.get(position);
            String sku = hashMap.get("sku");
            String unit = hashMap.get("unit");
            holder.skuName.setText(sku);

            holder.addUnit.setVisibility(View.GONE);
            holder.tvUnit.setVisibility(View.VISIBLE);
            holder.tvUnit.setText(""+unit);
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

            holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (isOnText){
                        isOnText = false;
                        holder.edUnit.setText(unit);
                        holder.okay.setVisibility(View.VISIBLE);
                        holder.edUnit.setVisibility(View.VISIBLE);
                        holder.tvUnit.setVisibility(View.GONE);
                        holder.edUnit.requestFocus();

                        hashMap = sellList.get(position);
                        String sSku = hashMap.get("sku");

                        holder.okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isOnText = true;
                                holder.okay.setVisibility(View.GONE);
                                holder.edUnit.setVisibility(View.GONE);
                                holder.tvUnit.setVisibility(View.VISIBLE);

                                String sUnit = holder.edUnit.getText().toString();

//                                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

                                if (!sUnit.isEmpty()){
                                    for (HashMap item : sellList){
                                        if (item.get("sku").equals(sSku)){
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                hashMap.replace("unit",sUnit);
                                            }
                                        }
                                    }

                                }else {
                                    sellList.remove(position);
                                }

                                listView.setAdapter(new MyAdapter());
                                listView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            }
                        });
                    }else {
                        Toast.makeText(getActivity(), "Complete Previous Task", Toast.LENGTH_SHORT).show();
                    }


                    return false;
                }
            });



        }

        @Override
        public int getItemCount() {
            return sellList.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView skuName,tvUnit;
            EditText edUnit;
            ImageView okay,addUnit;
            LinearLayout mainLayout;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
                tvUnit = itemView.findViewById(R.id.tvUnit);
                edUnit = itemView.findViewById(R.id.edUnit);
                okay = itemView.findViewById(R.id.okay);
                addUnit = itemView.findViewById(R.id.addUnit);
                mainLayout = itemView.findViewById(R.id.mainLayout);

            }
        }

    }

}