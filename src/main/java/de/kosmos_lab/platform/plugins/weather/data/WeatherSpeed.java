package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherUtils;

public class WeatherSpeed {
    public double data;

    public WeatherSpeed(double data_in_ms) {
        this.data = data_in_ms;
    }

    public WeatherSpeed fromValue(Double data_in_ms) {
        if (data_in_ms != null) {
            return new WeatherSpeed(data_in_ms);

        }
        return null;
    }


    public double get_miles_hour() {
        return WeatherUtils.MeterPerSecondToMilesPerHour(data);
    }

    public double get_meter_sec() {
        return (data);
    }

    public double get_kilometer_hour() {
        return WeatherUtils.MeterPerSecondToKmPerHour(data);
    }
}
