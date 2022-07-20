package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherProvider;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class WeatherCurrent extends WeatherData {
    protected WeatherEntry data;

    public WeatherCurrent(@Nonnull WeatherProvider provider,@CheckForNull String parameter) {
        super(provider, parameter);
    }
    @SuppressFBWarnings("EI_EXPOSE_REP")
    @Nonnull public WeatherEntry getData() {
        if ( this.data == null) {
            this.data = new WeatherEntry();
        }
        return this.data;
    }
    @Override
    protected void update() {
        provider.update(this);
    }
}

