package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherUtils;

public class WeatherTemperature {
    public double data;

    public WeatherTemperature(double data) {
        this.data = data;
    }

    public WeatherTemperature fromValue(Double data) {
        if (data != null) {
            return new WeatherTemperature(data);

        }
        return null;
    }

    public double get_C() {
        return WeatherUtils.KelvinToCelsius(data);
    }

    public double get_F() {
        return WeatherUtils.KelvinToFahrenheit(data);
    }

    public double get_K() {
        return (data);
    }
}
