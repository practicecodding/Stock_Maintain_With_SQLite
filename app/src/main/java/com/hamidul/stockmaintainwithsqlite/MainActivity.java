package com.hamidul.stockmaintainwithsqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<HashMap<String,String>> arrayList;
    HashMap hashMap;
    ListView listView;
    LinearLayout textView;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);

        getFinalStock();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog dialog = new Dialog(MainActivity.this);
//
//                dialog.setContentView(R.layout.insert_data);
//
//                EditText edSku = dialog.findViewById(R.id.edSku);
//                EditText edUnit = dialog.findViewById(R.id.edUnit);
//                Button button = dialog.findViewById(R.id.button);
//
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
//                dialog.show();
//
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        String sku = edSku.getText().toString();
//                        String sUnit = edUnit.getText().toString();
//                        int unit = Integer.parseInt(sUnit);
//
//                        dbHelper.insertStock(sku,unit);
//
//                        getFinalStock();
//
//                    }
//                });



                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("What You Want")
                        .setPositiveButton("purchase", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, Purchase.class));
                            }
                        })
                        .setNegativeButton("Sell", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, Sell.class));
                            }
                        })
                        .show();


            }
        });


        //==========================================================================================




    }

    private void getFinalStock(){

        Cursor cursor = dbHelper.getAllStockData();

        if (cursor!=null && cursor.getCount()>0){

            arrayList = new ArrayList();

            while (cursor.moveToNext()){

                String sku = cursor.getString(1);
                int unit = cursor.getInt(2);

                hashMap = new HashMap();
                hashMap.put("sku",sku);
                hashMap.put("unit",unit);
                arrayList.add(hashMap);

            }

            listView.setAdapter(new MyAdapter());

        }

    }

    public class MyAdapter extends BaseAdapter{
        LayoutInflater layoutInflater;
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.stock_item,parent,false);

            TextView skuName = myView.findViewById(R.id.skuName);
            TextView skuUnit = myView.findViewById(R.id.skuUnit);

            hashMap = (HashMap) arrayList.get(position);
            String sku = (String) hashMap.get("sku");
            int unit = (int) hashMap.get("unit");

            skuName.setText(sku);
            skuUnit.setText(""+unit);

            return myView;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}