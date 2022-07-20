package de.kosmos_lab.platform.plugins.weather.data;

import org.json.JSONObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class WeatherEntry {
    public WeatherTemperature temperature;
    public WeatherTemperature tempMax;

    @CheckForNull
    public WeatherTemperature getTemperature() {
        return temperature;
    }

    public void setTemperature(@Nullable WeatherTemperature temperature) {
        this.temperature = temperature;
    }

    @CheckForNull public WeatherTemperature getTempMax() {
        return tempMax;
    }

    public void setTempMax(@Nullable WeatherTemperature tempMax) {
        this.tempMax = tempMax;
    }

    @CheckForNull public WeatherTemperature getTempMin() {
        return tempMin;
    }

    public void setTempMin(@Nullable WeatherTemperature tempMin) {
        this.tempMin = tempMin;
    }

    @CheckForNull public WeatherTemperature getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(@Nullable WeatherTemperature feelsLike) {
        this.feelsLike = feelsLike;
    }

    @CheckForNull public Double getPressure() {
        return pressure;
    }

    public void setPressure(@Nullable Double pressure) {
        this.pressure = pressure;
    }

    @CheckForNull public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(@Nullable Double humidity) {
        this.humidity = humidity;
    }

    @CheckForNull public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(@Nullable Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    @CheckForNull public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(@Nullable String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    @CheckForNull public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(@Nullable Integer visibility) {
        this.visibility = visibility;
    }

    @CheckForNull public WeatherSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(@Nullable WeatherSpeed windSpeed) {
        this.windSpeed = windSpeed;
    }

    @CheckForNull  public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(@Nullable Integer windDegree) {
        this.windDegree = windDegree;
    }

    @CheckForNull public WeatherSpeed getWindGust() {
        return windGust;
    }

    public void setWindGust(@Nullable WeatherSpeed windGust) {
        this.windGust = windGust;
    }

    @CheckForNull public Double getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(@Nullable Double cloudiness) {
        this.cloudiness = cloudiness;
    }

    @CheckForNull public Double getRain1h() {
        return rain1h;
    }

    public void setRain1h(@Nullable Double rain1h) {
        this.rain1h = rain1h;
    }

    @CheckForNull public Double getRain3h() {
        return rain3h;
    }

    public void setRain3h(@Nullable Double rain3h) {
        this.rain3h = rain3h;
    }

    @CheckForNull public Double getSnow1h() {
        return snow1h;
    }

    public void setSnow1h(@Nullable Double snow1h) {
        this.snow1h = snow1h;
    }

    @CheckForNull public Double getSnow3h() {
        return snow3h;
    }

    public void setSnow3h(@Nullable Double snow3h) {
        this.snow3h = snow3h;
    }

    public WeatherTemperature tempMin;
    public WeatherTemperature feelsLike;
    public Double pressure;
    public Double humidity;
    public Integer weatherCode;
    public String weatherIcon;

    public Integer visibility;
    public WeatherSpeed windSpeed;
    public Integer windDegree;
    public WeatherSpeed windGust;
    public Double cloudiness;
    public Double rain1h;
    public Double rain3h;
    public Double snow1h;
    public Double snow3h;
    
    
    public JSONObject toJSON() {
        
        JSONObject json = new JSONObject();
        
        if ( this.temperature != null ) {
            json.put("temp_c",this.temperature.get_C());
            json.put("temp_k",this.temperature.get_K());
            json.put("temp_f",this.temperature.get_F());
        }
        if ( this.tempMax != null ) {
            json.put("temp_max_c",this.tempMax.get_C());
            json.put("temp_max_k",this.tempMax.get_K());
            json.put("temp_max_f",this.tempMax.get_F());
        }
        if ( this.tempMin != null ) {
            json.put("temp_min_c",this.tempMin.get_C());
            json.put("temp_min_k",this.tempMin.get_K());
            json.put("temp_min_f",this.tempMin.get_F());
        }
        if ( this.pressure != null ) {
            json.put("pressure",this.pressure);
            
        }
        if ( this.humidity != null ) {
            json.put("humidity",this.humidity);
            
        }
        if ( this.weatherCode != null) {
            json.put("weatherCode",this.weatherCode);
        }
        if ( this.cloudiness != null)  {
            json.put("cloudiness",this.cloudiness);
        }
        if ( this.rain1h != null ) {
            json.put("rain1h",this.rain1h);
        }
        if ( this.rain3h != null ) {
            json.put("rain3h",this.rain3h);
        }
        if ( this.snow1h != null ) {
            json.put("snow1h",this.snow1h);
        }
        if ( this.snow3h != null ) {
            json.put("snow3h",this.snow3h);
        }
        if ( this.visibility != null ) {
            json.put("visibility",this.visibility);
        }
        if ( this.weatherIcon != null ) {
            json.put("weatherIcon",this.weatherIcon);
        }
        if ( this.windDegree != null ) {
            json.put("windDegree",this.windDegree);
        }
        if ( this.windGust != null ) {
            json.put("windGust",this.windGust);
        }
        if ( this.windSpeed != null ) {
            json.put("windSpeed",this.windSpeed);
        }
        return json;
    }
}
