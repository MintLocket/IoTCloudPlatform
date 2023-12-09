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

        listThingsURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices";
        thingShadowURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices/";
        getLogsURL = "https://b04a2ls0h4.execute-api.ap-southeast-2.amazonaws.com/prod/devices/";

        //사용자가 url을 직접 입력하지 않고도, 버튼 이벤트 발생시에 액티비티로 넘어갈 때 인텐트로 url을 넘겨주도록 수정함.
        //디바이스 조회 버튼 클릭 이벤트 정의 
        Button listThingsBtn = findViewById(R.id.listThingsBtn);
        listThingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListThingsActivity.class);
                intent.putExtra("listThingsURL", listThingsURL);
                startActivity(intent);
            }
        });

        //디바이스 최신 상태 조회 버튼 클릭 이벤트 정의
        Button thingShadowBtn = findViewById(R.id.thingShadowBtn);
        thingShadowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                intent.putExtra("thingShadowURL", thingShadowURL);
                startActivity(intent);
            }
        });

        //디바이스 로그 조회 버튼 클릭 이벤트 조회
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
