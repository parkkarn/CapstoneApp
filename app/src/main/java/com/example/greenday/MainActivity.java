package com.example.greenday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.example.greenday.ui.dashboard.DashboardViewModel;
import com.example.greenday.ui.home.HomeViewModel;
import com.example.greenday.ui.notifications.NotificationsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.greenday.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private HomeViewModel homeViewModel;
    private DashboardViewModel dashboardViewModel;
    private NotificationsViewModel notificationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        // FetchDataFromServer 실행
        new FetchDataFromServer().execute();
    }

    private class FetchDataFromServer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // 이 부분은 백그라운드 스레드에서 실행됩니다.
            try {
                // 서버에서 데이터 가져오기
                String urlStr = "http://ALB-2065391585.ap-northeast-2.elb.amazonaws.com:8080/request_data";
                URL url = new URL(urlStr);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    Thread.sleep(1000);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // 이 부분은 UI 스레드에서 실행됩니다.
            List<String> co2Array = new ArrayList<>();
            List<String> humidityArray = new ArrayList<>();
            List<String> postTimeArray = new ArrayList<>();
            List<String> temperatureArray = new ArrayList<>();
            if (result != null) {
                // JSON 데이터를 ViewModel에 설정
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // 각 컬럼에 대한 데이터 추출
                        int co2 = jsonObject.getInt("CO2");
                        int humidity = jsonObject.getInt("humi");
                        String postTime = jsonObject.getString("posttime");
                        int temperature = jsonObject.getInt("temp");

                        // 데이터를 각 컬럼에 해당하는 배열에 추가
                        co2Array.add(String.valueOf(co2));
                        humidityArray.add(String.valueOf(humidity));
                        postTimeArray.add(postTime);
                        temperatureArray.add(String.valueOf(temperature));
                    }

                    // 결과 출력
                    System.out.println("CO2 Array: " + co2Array);
                    System.out.println("Humidity Array: " + humidityArray);
                    System.out.println("Post Time Array: " + postTimeArray);
                    System.out.println("Temperature Array: " + temperatureArray);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                int humi_sum = 0;
                int temp_sum = 0;
                int CO2_sum = 0;
                for(int i=0;i<humidityArray.size();i++){
                    humi_sum += Integer.parseInt(humidityArray.get(i));
                    temp_sum += Integer.parseInt(temperatureArray.get(i));
                    CO2_sum += Integer.parseInt(co2Array.get(i));
                }
                homeViewModel.setHumidityArray(humidityArray);
                homeViewModel.setPostTimeArray(postTimeArray);
                homeViewModel.setText("MAX : " + Collections.max(humidityArray) +"%\nMIN : " +
                        Collections.min(humidityArray) + "%\nAVG : " + (int)((double)humi_sum/humidityArray.size()) + "%");


                dashboardViewModel.setTempArray(temperatureArray);
                dashboardViewModel.setPostTimeArray(postTimeArray);
                dashboardViewModel.setText("MAX : " + Collections.max(temperatureArray) +"°C\nMIN : " +
                        Collections.min(temperatureArray) + "°C\nAVG : " + (int)((double)temp_sum/temperatureArray.size()) +"°C");

                notificationsViewModel.setCo2Array(co2Array);
                notificationsViewModel.setPostTimeArray(postTimeArray);
                notificationsViewModel.setText("MAX : " + Collections.max(co2Array) +"ppm\nMIN : " +
                        Collections.min(co2Array) + "ppm\nAVG : " + (int)((double)CO2_sum/co2Array.size())+"ppm");
            } else {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

}