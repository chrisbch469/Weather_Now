package chris_boich.weathernow.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.OnClick;
import chris_boich.weathernow.R;
import chris_boich.weathernow.weather.Current;
import chris_boich.weathernow.weather.Day;
import chris_boich.weathernow.weather.Forecast;
import chris_boich.weathernow.weather.Hour;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Forecast mForecast;

    private TextView mTemperatureTextView, mTimeTextView, mLocationTextView, mHumidityValue, mPrecipValue, mSummaryTextView, mWindSpeedValue;
    private ImageView mIconImageView, mRefreshImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTemperatureTextView = (TextView) findViewById(R.id.degreeTextView);
        mTimeTextView = (TextView) findViewById(R.id.timeTextView);
        mLocationTextView = (TextView) findViewById(R.id.locationTextView);
        mHumidityValue = (TextView) findViewById(R.id.humidityValue);
        mPrecipValue = (TextView) findViewById(R.id.precipValue);
        mIconImageView = (ImageView) findViewById(R.id.iconImageView);
        mSummaryTextView = (TextView) findViewById(R.id.summaryTextView);
        mWindSpeedValue = (TextView) findViewById(R.id.windSpeedValue);
        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        final double longitude = -75.1444;
        final double latitude = 39.7664;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);

    }
    private void getForecast(double latitude, double longitude) {
        String apiKey = "a3ca6f2ce6c3f40c614c4698e81468de";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        //okhttp code
        if (isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            //asynchronous call enqueue
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception Caught: ", e);
                    }
                }
            });
        } else {
            alertUserAboutNetworkError();

        }
    }



    private void toggleRefresh() {
        if(mProgressBar.getVisibility()== View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureTextView.setText(current.getTemperature()+ "");
        mTimeTextView.setText("As of " + current.getFormattedTime());
        mPrecipValue.setText(current.getPrecipChance() + " %");
        mHumidityValue.setText(current.getHumidity() + "");
        mSummaryTextView.setText(current.getSummary());
        mWindSpeedValue.setText(current.getWindSpeed() + "mph");

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));

        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTimeZone(timezone);

            hours[i] = hour;
        }
        return hours;

    }

    private Day[] getDailyForecast(String jsonData) throws  JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for(int i = 0; i<data.length();i++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setTimeZone(timezone);
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));

            days[i] = day;

        }
        return days;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");


        Current currentweather = new Current();
        currentweather.setHumidity(currently.getDouble("humidity"));
        currentweather.setTime(currently.getLong("time"));
        currentweather.setPrecipChance(currently.getDouble("precipProbability"));
        currentweather.setTemperature(currently.getDouble("temperature"));
        currentweather.setSummary(currently.getString("summary"));
        currentweather.setIcon(currently.getString("icon"));
        currentweather.setTimeZone(timezone);
        currentweather.setWindSpeed(currently.getDouble("windSpeed"));

        Log.d(TAG, currentweather.getFormattedTime());

        return currentweather;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkinfo != null && networkinfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
    private void alertUserAboutNetworkError(){
        AlertDialogFragmentNetworkIssue networkDialog = new AlertDialogFragmentNetworkIssue();
        networkDialog.show(getFragmentManager(), "network_error_dialog");
    }

@OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
    Intent intent = new Intent(this, DailyForecastActivity.class);
    startActivity(intent);
}

}
