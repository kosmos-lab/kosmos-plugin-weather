package de.kosmos_lab.platform.plugins.weather.data;

import de.kosmos_lab.platform.plugins.weather.WeatherProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.CheckForNull;
import java.util.LinkedList;

public class WeatherForecast extends WeatherData {
    protected LinkedList<WeatherEntry> forecasts;
    @Override
    protected void update() {
        provider.update(this);
    }

    public WeatherForecast(WeatherProvider provider, String parameter) {
        super(provider, parameter);
    }







    public WeatherEntry getForecast(int slot) {
        slot = slot - 1;
        try {
            return this.forecasts.get(slot);
        } catch (IndexOutOfBoundsException i) {
            i.printStackTrace();
        }
        return null;
    }

    public JSONArray toJSONArray() {
        JSONArray arr = new JSONArray();

        for (WeatherEntry entry : forecasts) {
            arr.put(entry.toJSON());
        }

        return arr;
    }

    public void clear() {
        if ( this.forecasts == null ) {
            this.forecasts = new LinkedList<>();
        }
        else {
            this.forecasts.clear();
        }
    }

    public void add(WeatherEntry entry) {
        forecasts.add(entry);
    }
}

