package chris_boich.weathernow.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import chris_boich.weathernow.R;

/**
 * Created by ctboi_000 on 5/12/2015.
 */
public class Current {

    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;
    private double mWindSpeed;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon(){
        return mIcon;
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;

        if(mIcon.equals("clear-day")){
            iconId = R.drawable.clear_day;
        }
        else if(mIcon.equals("clear-night")){
            iconId = R.drawable.clear_night;
        }
        else if(mIcon.equals("fog")){
            iconId = R.drawable.fog;
        }
        else if(mIcon.equals("cloudy")){
            iconId = R.drawable.cloudy;
        }
        else if(mIcon.equals("cloudy-night")){
            iconId = R.drawable.cloudy_night;
        }
        else if(mIcon.equals("partly-cloudy-day")){
            iconId = R.drawable.partly_cloudy;
        }
        else if(mIcon.equals("partly-cloudy-night")){
            iconId = R.drawable.partly_cloudy;
        }
        else if(mIcon.equals("rain")){
            iconId = R.drawable.rain;
        }
        else if(mIcon.equals("sleet")){
            iconId = R.drawable.sleet;
        }
        else if(mIcon.equals("snow")){
            iconId = R.drawable.snow;
        }
        else if(mIcon.equals("sunny")){
            iconId = R.drawable.sunny;
        }
        else if(mIcon.equals("wind")){
            iconId = R.drawable.wind;
        }
        return iconId;
    }
    public void setIcon(String icon){
        mIcon = icon;
    }

    public long getTime(){
        return mTime;
    }
    public void setTime(long time){
        mTime = time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getTemperature(){
        return (int)Math.round(mTemperature);
    }
    public void setTemperature(double temperature){
        mTemperature = temperature;
    }

    public double getHumidity(){
        return mHumidity;
    }
    public void setHumidity(double humidity){
        mHumidity = humidity;
    }

    public int getPrecipChance(){
        return (int)Math.round(mPrecipChance * 100);
    }
    public void setPrecipChance(double precipChance){
        mPrecipChance = precipChance;
    }

    public String getSummary(){
        return mSummary;
    }

    public void setSummary(String summary){
        mSummary = summary;
    }


    public int getWindSpeed() {
        return (int)Math.round(mWindSpeed);
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }
}
