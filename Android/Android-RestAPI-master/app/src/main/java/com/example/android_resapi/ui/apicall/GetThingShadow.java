package com.example.android_resapi.ui.apicall;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.android_resapi.R;
import com.example.android_resapi.httpconnection.GetRequest;

public class GetThingShadow extends GetRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;
    public GetThingShadow(Activity activity, String urlStr) {
        super(activity);
        this.urlStr = urlStr;
    }

    @Override
    protected void onPreExecute() {
        try {
            Log.e(TAG, urlStr);
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            activity.finish();
        }
    }

    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;
        Map<String, String> state = getStateFromJSONString(jsonString);
        TextView reported_soundTV = activity.findViewById(R.id.reported_sound);
        TextView reported_lightTV = activity.findViewById(R.id.reported_light);
        TextView reported_buzzerTV = activity.findViewById(R.id.reported_buzzer);
        reported_soundTV.setText(state.get("reported_sound"));
        reported_lightTV.setText(state.get("reported_light"));
        reported_buzzerTV.setText(state.get("reported_buzzer"));

        TextView desired_buzzerTV = activity.findViewById(R.id.desired_buzzer);
        desired_buzzerTV.setText(state.get("desired_buzzer"));
    }

    protected Map<String, String> getStateFromJSONString(String jsonString) {
        Map<String, String> output = new HashMap<>();
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");
            Log.i(TAG, "jsonString="+jsonString);
            JSONObject root = new JSONObject(jsonString);
            JSONObject state = root.getJSONObject("state");
            JSONObject reported = state.getJSONObject("reported");
            String soundValue = reported.getString("sound");
            String lightValue = reported.getString("light");
            String buzzerValue = reported.getString("buzzer");
            output.put("reported_sound", soundValue);
            output.put("reported_light", lightValue);
            output.put("reported_buzzer",buzzerValue);

            JSONObject desired = state.getJSONObject("desired");
//            String desired_soundValue = desired.getString("sound");
//            String desired_lightValue = desired.getString("light");
            String desired_buzzerValue = desired.getString("buzzer");
//            output.put("desired_sound", desired_soundValue);
//            output.put("desired_light", desired_lightValue);
            output.put("desired_buzzer",desired_buzzerValue);

        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }
}
