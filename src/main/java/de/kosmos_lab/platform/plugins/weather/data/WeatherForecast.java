package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherProvider;
import org.json.JSONArray;

import java.util.LinkedList;

public class WeatherForecast extends WeatherData {
    protected LinkedList<WeatherEntry> forecasts;

    public WeatherForecast(WeatherProvider provider, String parameter) {
        super(provider, parameter);
    }

    @Override
    protected void update() {
        provider.update(this);
    }

    public WeatherEntry getForecast(int slot) throws IndexOutOfBoundsException{
        slot = slot - 1;
        return this.forecasts.get(slot);

    }

    public JSONArray toJSONArray() {
        JSONArray arr = new JSONArray();

        for (WeatherEntry entry : forecasts) {
            arr.put(entry.toJSON());
        }

        return arr;
    }

    public void clear() {
        if (this.forecasts == null) {
            this.forecasts = new LinkedList<>();
        } else {
            this.forecasts.clear();
        }
    }

    public void add(WeatherEntry entry) {
        forecasts.add(entry);
    }
}

