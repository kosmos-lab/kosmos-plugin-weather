package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherProvider;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public abstract class WeatherData {
    final WeatherProvider provider;
    public String parameter = null;
    public long updated;
    long maxAge = 300000;

    public WeatherData(@Nonnull WeatherProvider provider, @CheckForNull String parameter) {
        this.parameter = parameter;
        this.provider = provider;
        update();

    }

    protected abstract void update();

    public void check() {
        long delta = System.currentTimeMillis() - updated;
        if (delta > maxAge) {
            update();
        }
    }

    @CheckForNull
    public String getParameter() {
        return this.parameter;
    }


}



