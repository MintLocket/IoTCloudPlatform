package com.example.android_resapi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_resapi.R;
import com.example.android_resapi.ui.apicall.GetLog;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    String listThingsURL, thingShadowURL, getLogsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //사용자가 url 주입하지 않고도 버튼 클릭 이벤트 발생시 화면 이동하면서 인텐트로 url을 넘겨주게끔 수정함
        listThingsURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices";
        thingShadowURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices/";
        getLogsURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices/";

        //디바이스 조회 버튼 클릭 이벤트 정의
        Button listThingsBtn = findViewById(R.id.listThingsBtn);
        listThingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListThingsActivity.class);
          릭 이벤트 정의
        Button listLogsBtn = findViewById(R.id.listLogsBtn);
        listLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                intent.putExtra("getLogsURL", getLogsURL);
                startActivity(intent);
            }
        });

        //차트 보기 버튼 클릭 이벤트 정의
        Button chartBtn = findViewById(R.id.listChart);
        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("getLogsURL", getLogsURL);
                startActivity(intent);
            }
        });
    }
}


