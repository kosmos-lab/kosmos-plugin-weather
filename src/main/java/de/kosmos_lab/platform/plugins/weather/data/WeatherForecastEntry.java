package de.kosmos_lab.platform.plugins.weather.data;

import org.json.JSONObject;

import javax.annotation.CheckForNull;

public class WeatherForecastEntry  {
    public WeatherTemperature feelsLike;
    public long updated;


    public WeatherTemperature temperature;
    public WeatherTemperature tempMax;
    public WeatherTemperature tempMin;
    public Double pressure;
    public Double humidity;


    @CheckForNull
    public WeatherTemperature getTemperature() {
        return temperature;
    }

    @CheckForNull
    public WeatherTemperature getFeelsLike() {
        return feelsLike;
    }

    @CheckForNull
    public WeatherTemperature getTempMax() {
        return feelsLike;
    }
    @CheckForNull
    public WeatherTemperature getTempMin() {
        return tempMin;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }



    public JSONObject toJSON() {

        JSONObject json = new JSONObject();

        if ( this.temperature != null ) {
            json.put("temp_c",temperature.get_C());
            json.put("temp_k",temperature.get_K());
            json.put("temp_f",temperature.get_F());
        }
        if ( this.tempMax != null ) {
            json.put("temp_max_c",tempMax.get_C());
            json.put("temp_max_k",tempMax.get_K());
            json.put("temp_max_f",tempMax.get_F());
        }
        if ( this.tempMin != null ) {
            json.put("temp_min_c",tempMin.get_C());
            json.put("temp_min_k",tempMin.get_K());
            json.put("temp_min_f",tempMin.get_F());
        }
        if ( this.pressure != null ) {
            json.put("pressure",pressure);

        }
        if ( this.humidity != null ) {
            json.put("humidity",humidity);

        }
        return json;
    }


}

