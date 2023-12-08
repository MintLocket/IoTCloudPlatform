package com.example.android_resapi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android_resapi.R;
import com.example.android_resapi.ui.apicall.GetThingShadow;
import com.example.android_resapi.ui.apicall.UpdateShadow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceActivity extends AppCompatActivity {
    String urlStr;
    String tmpUrlStr;
    final static String TAG = "AndroidAPITest";
    Timer timer;
    Button startGetBtn;
    Button stopGetBtn;
    Button kiosk_A_btn;
    Button kiosk_B_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent intent = getIntent();
        urlStr = intent.getStringExtra("thingShadowURL"); //INTENT 객체로 String형 url 받아옴.
        tmpUrlStr = urlStr; //tempUrl에 메인에서 받아온거 할당(재사용 위함)

        kiosk_A_btn = findViewById(R.id.kiosk_A_btn);
        kiosk_A_btn.setEnabled(true);
        kiosk_B_btn = findViewById(R.id.kiosk_B_btn);
        kiosk_B_btn.setEnabled(true);

        kiosk_A_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlStr = tmpUrlStr; //a를 누르고 b를 눌렀을 경우, 쿼리문이 계속 쌓이는 것을 방지하기 위해 초기화시켜줌.
                urlStr = urlStr + "MyMKRWiFi1010";
            }
        });

        kiosk_B_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlStr = tmpUrlStr;//a를 누르고 b를 눌렀을 경우, 쿼리문이 계속 쌓이는 것을 방지하기 위해 초기화시켜줌.
                urlStr = urlStr + "Kiosk_B";
            }
        });

        startGetBtn = findViewById(R.id.startGetBtn);
        startGetBtn.setEnabled(true);
        startGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //여기서 ThingShadow에 url 넣어줌 -> 이 전에 A, B 나눠주는 작업 해야댐.
                        new GetThingShadow(DeviceActivity.this, urlStr).execute();
                    }
                },
                        0,2000);

                startGetBtn.setEnabled(false);
                stopGetBtn.setEnabled(true);
            }
        });

        stopGetBtn = findViewById(R.id.stopGetBtn);
        stopGetBtn.setEnabled(false);
        stopGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null)
                    timer.cancel();
                clearTextView();
                startGetBtn.setEnabled(true);
                stopGetBtn.setEnabled(false);
            }
        });

        Button updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_buzzer = findViewById(R.id.edit_buzzer);

                JSONObject payload = new JSONObject();

                try {
                    JSONArray jsonArray = new JSONArray();
                    String buzzer_input = edit_buzzer.getText().toString();
                    if (buzzer_input != null && !buzzer_input.equals("")) {
                        JSONObject tag1 = new JSONObject();
                        tag1.put("tagName", "buzzer");
                        tag1.put("tagValue", buzzer_input);

                        jsonArray.put(tag1);
                    }
                    if (jsonArray.length() > 0)
                        payload.put("tags", jsonArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
                Log.i(TAG,"payload="+payload);
                if (payload.length() >0 )
                    new UpdateShadow(DeviceActivity.this,urlStr).execute(payload);
                else
                    Toast.makeText(DeviceActivity.this,"변경할 상태 정보 입력이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void clearTextView() {
        TextView reported_buzzerTV = findViewById(R.id.reported_buzzer);
        TextView reported_soundTV = findViewById(R.id.reported_sound);
        TextView reported_lightTV = findViewById(R.id.reported_light);
        reported_buzzerTV.setText("");
        reported_soundTV.setText("");
        reported_lightTV.setText("");

        TextView desired_buzzerTV = findViewById(R.id.desired_buzzer);
        desired_buzzerTV.setText("");
    }

}


