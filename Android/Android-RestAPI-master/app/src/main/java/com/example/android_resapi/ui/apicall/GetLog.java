package com.example.android_resapi.ui.apicall;

import android.app.Activity;
import java.util.Collections;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.android_resapi.R;
import com.example.android_resapi.httpconnection.GetRequest;
import com.example.android_resapi.ui.ChartActivity;
import com.example.android_resapi.ui.LogActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class GetLog extends GetRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;

    public GetLog(Activity activity, String urlStr) {
        super(activity);
        this.activity = activity;
        this.urlStr = urlStr;
    }

    ArrayList<Tag> output = new ArrayList<Tag>();


    @Override
    protected void onPreExecute() {
        try {

            TextView textView_Date1 = activity.findViewById(R.id.textView_date1);
            TextView textView_Time1 = activity.findViewById(R.id.textView_time1);
            TextView textView_Date2 = activity.findViewById(R.id.textView_date2);
            TextView textView_Time2 = activity.findViewById(R.id.textView_time2);

            String params = String.format("?from=%s:00&to=%s:00",textView_Date1.getText().toString()+textView_Time1.getText().toString(),
                                                            textView_Date2.getText().toString()+textView_Time2.getText().toString());

            Log.i(TAG,"urlStr="+urlStr+params);
            url = new URL(urlStr+params);

        } catch (MalformedURLException e) {
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        TextView message = activity.findViewById(R.id.message2);
        message.setText("조회중...");
    }

    @Override
    protected void onPostExecute(String jsonString) {
        TextView message = activity.findViewById(R.id.message2);
        if (jsonString == null) {
            message.setText("로그 없음");
            return;
        }
        message.setText("");
        ArrayList<Tag> arrayList = getArrayListFromJSONString(jsonString);

        //Log액티비티 에서 호출했다면 로그 띄움
        if(activity instanceof LogActivity)
        {
            ListView txtList = activity.findViewById(R.id.logList);
            // 최근 순서대로(리스트 역순으로)
            Collections.reverse(arrayList);

            final ArrayAdapter adapter = new ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1,
                    arrayList.toArray());
            txtList.setAdapter(adapter);
            txtList.setDividerHeight(10);
        }

        //chart 액티비티에서 호출했다면 차트 띄움
        if(activity instanceof ChartActivity)
        {

            LineChart chart = (LineChart)activity.findViewById(R.id.chart);
            ArrayList <Entry> soundValue = new ArrayList<>();
            ArrayList <Entry> lightValue = new ArrayList<>();

            for (Tag item : output) {
                // 각 항목에서 필요한 데이터 추출
                float timestamp = Float.parseFloat(item.Time);
                float sound = Float.parseFloat(item.Sound);
                float light = Float.parseFloat(item.Light);
                soundValue.add(new Entry(timestamp, sound));
                lightValue.add(new Entry(timestamp, light));
            }
            // LineDataSet 객체 생성
            LineDataSet set = new LineDataSet(soundValue, "Sound");
            LineDataSet set2 = new LineDataSet(lightValue, "Light");
            set.setDrawIcons(false);
            set2.setDrawIcons(false);

            set.setColor(Color.RED);
            set.setCircleColor(Color.RED);

            set2.setColor(Color.BLUE);
            set2.setCircleColor(Color.BLUE); // Light 데이터 세트의 색상을 설정합니다
            LineData data = new LineData(set, set2); // 두 데이터 세트를 모두 추가합니다.

            chart.setData(data);
            chart.invalidate();

        }
    }

    protected ArrayList<Tag> getArrayListFromJSONString(String jsonString) {
         output = new ArrayList(); //차트는 역순으로 띄우지 않기 때문에 output 리스트를 가져옴.
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");

            Log.i(TAG, "jsonString="+jsonString);

            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                Tag thing = new Tag(jsonObject.getString("kioskId"),
                                    jsonObject.getString("sound"),
                                    jsonObject.getString("light"),
                                    jsonObject.getString("buzzer"),
                                    jsonObject.getString("timestamp"),
                                    jsonObject.getString("time"));
                output.add(thing);
            }

        } catch (JSONException e) {
            //Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }

    public class Tag {
        String Sound;
        String Light;
        String Buzzer;
        String Timestamp;
        String id;
        String Time;

        public Tag(String kioskid, String sound, String light, String buzzer, String time, String realTime) {
            id = kioskid;
            Sound = sound;
            Light = light;
            Buzzer = buzzer;
            Timestamp = time;
            Time = realTime;
        }

        public String toString() {
            return String.format("{%s} [%s] sound: %s, light: %s, buzzer: %s", id, Timestamp, Sound, Light, Buzzer);
        }
    }
}

