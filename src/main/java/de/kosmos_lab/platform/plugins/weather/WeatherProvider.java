package de.kosmos_lab.platform.plugins.weather;

import de.kosmos_lab.platform.plugins.weather.data.WeatherCurrent;
import de.kosmos_lab.platform.plugins.weather.data.WeatherForecast;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface WeatherProvider {
    @CheckForNull
    WeatherCurrent getCurrentWeather(@Nonnull String parameters);

    @CheckForNull WeatherForecast getForecast(@Nonnull String parameters);

    void update(@Nonnull WeatherForecast forecast);
    void update(@Nonnull WeatherCurrent weatherCurrent);
}
