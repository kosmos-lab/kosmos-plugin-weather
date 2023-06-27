package de.kosmos_lab.platform.plugins.weather.openweathermap;


import de.kosmos_lab.platform.client.HTTPClient;
import de.kosmos_lab.platform.plugins.weather.WeatherProvider;
import de.kosmos_lab.platform.plugins.weather.data.WeatherCurrent;
import de.kosmos_lab.platform.plugins.weather.data.WeatherEntry;
import de.kosmos_lab.platform.plugins.weather.data.WeatherForecast;
import de.kosmos_lab.platform.plugins.weather.data.WeatherSpeed;
import de.kosmos_lab.platform.plugins.weather.data.WeatherTemperature;
import org.eclipse.jetty.http.HttpMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;

public class OpenWeatherMapProvider implements WeatherProvider {

    private final String apikey;
    ConcurrentHashMap<String, WeatherCurrent> currentCache = new ConcurrentHashMap<String, WeatherCurrent>();
    ConcurrentHashMap<String, WeatherForecast> forecastCache = new ConcurrentHashMap<String, WeatherForecast>();


    public OpenWeatherMapProvider(String key) {
        this.apikey = key;
    }

    public static WeatherEntry updateFromJSON(JSONObject json) {
        WeatherEntry entry = new WeatherEntry();
        updateFromJSON(entry, json);
        return entry;
    }

    public static void updateFromJSON(WeatherEntry data, JSONObject json) {

        JSONObject jsonObject = json.optJSONObject("main");
        if (jsonObject != null) {
            if (jsonObject.has("temp")) {
                data.setTemperature(new WeatherTemperature(jsonObject.getDouble("temp")));
            } else {
                data.setTemperature(null);
            }
            if (jsonObject.has("feels_like")) {
                data.setFeelsLike(new WeatherTemperature(jsonObject.getDouble("feels_like")));
            } else {
                data.setFeelsLike(null);
            }
            if (jsonObject.has("temp_min")) {
                data.setTempMin(new WeatherTemperature(jsonObject.getDouble("temp_min")));
            } else {
                data.setTempMin(null);
            }
            if (jsonObject.has("temp_max")) {
                data.setTempMax(new WeatherTemperature(jsonObject.getDouble("temp_max")));
            } else {
                data.setTempMax(null);
            }
            if (jsonObject.has("pressure")) {
                data.setPressure(jsonObject.getDouble("pressure"));
            } else {
                data.setPressure(null);
            }
            if (jsonObject.has("humidity")) {
                data.setHumidity(jsonObject.getDouble("humidity"));
            } else {
                data.setHumidity(null);
            }
        }

        JSONArray jsonArray = json.optJSONArray("weather");
        if (jsonArray != null) {
            JSONObject weather = jsonArray.optJSONObject(0);
            if (weather != null) {
                if (weather.has("id")) {
                    data.setWeatherCode(weather.getInt("id"));
                } else {
                    data.setWeatherCode(null);
                }
                if (weather.has("icon")) {
                    data.setWeatherIcon(weather.getString("icon"));
                } else {
                    data.setWeatherIcon(null);
                }
            }
        }
        jsonObject = json.optJSONObject("wind");
        if (jsonObject != null) {
            if (jsonObject.has("speed")) {
                data.setWindSpeed(new WeatherSpeed(jsonObject.getDouble("speed")));
            } else {
                data.setWindSpeed(null);
            }
            if (jsonObject.has("deg")) {
                data.setWindDegree(jsonObject.getInt("deg"));

            } else {
                data.setWindDegree(null);
            }
            if (jsonObject.has("gust")) {
                data.setWindGust(new WeatherSpeed(jsonObject.getDouble("gust")));
            } else {
                data.setWindGust(null);
            }
        }
        jsonObject = json.optJSONObject("rain");
        if (jsonObject != null) {
            if (jsonObject.has("1h")) {
                data.setRain1h(jsonObject.getDouble("1h"));
            } else {
                data.setRain1h(null);
            }
            if (jsonObject.has("3h")) {
                data.setRain3h(jsonObject.getDouble("3h"));
            } else {
                data.setRain3h(null);
            }
        }
        jsonObject = json.optJSONObject("snow");
        if (jsonObject != null) {
            if (jsonObject.has("1h")) {
                data.setSnow1h(jsonObject.getDouble("1h"));
            } else {
                data.setSnow1h(null);
            }
            if (jsonObject.has("3h")) {
                data.setSnow3h(jsonObject.getDouble("3h"));
            } else {
                data.setSnow3h(null);
            }
        }
        jsonObject = json.optJSONObject("clouds");
        if (jsonObject != null) {
            if (jsonObject.has("all")) {
                data.setCloudiness(jsonObject.getDouble("all"));
            } else {
                data.setCloudiness(null);
            }

        }
        if (json.has("visibility")) {
            data.setVisibility(json.getInt("visibility"));
        } else {
            data.setVisibility(null);
        }
    }

    @Override
    public @CheckForNull WeatherCurrent getCurrentWeather(@Nonnull String parameters) {
        WeatherCurrent current = currentCache.get(parameters);
        if (current != null) {
            current.check();

        } else {
            current = new WeatherCurrent(this, parameters);
            currentCache.put(parameters, current);
        }
        return current;


    }

    @Override
    public @CheckForNull WeatherForecast getForecast(@Nonnull String parameters) {
        WeatherForecast forecast = forecastCache.get(parameters);
        if (forecast != null) {
            forecast.check();

        } else {
            forecast = new WeatherForecast(this, parameters);
            forecastCache.put(parameters, forecast);
        }
        return forecast;


    }

    @Override
    public void update(@Nonnull WeatherForecast forecast) {
        JSONObject fullJson = getForecastJSON(forecast.getParameter());
        if (fullJson != null) {


            forecast.clear();

            JSONArray list = fullJson.optJSONArray("list");
            if (list != null) {
                for (int i = 0; i < list.length(); i++) {
                    forecast.add(OpenWeatherMapProvider.updateFromJSON(list.getJSONObject(i)));
                }
            }
            forecast.updated = System.currentTimeMillis();
        }
    }

    @Override
    public void update(@Nonnull WeatherCurrent weatherCurrent) {
        JSONObject json = getCurrentWeatherJSON(weatherCurrent.getParameter());
        OpenWeatherMapProvider.updateFromJSON(weatherCurrent.getData(), json);
        weatherCurrent.updated = System.currentTimeMillis();

    }

    @CheckForNull
    JSONObject getCurrentWeatherJSON(String parameter) {

        try {
            HTTPClient c = new HTTPClient();
            return c.fetchJSONObject(String.format("https://api.openweathermap.org/data/2.5/weather?appid=%s&units=standard&%s", this.apikey, parameter), HttpMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @CheckForNull
    JSONObject getForecastJSON(String parameter) {

        try {
            HTTPClient c = new HTTPClient();
            return c.fetchJSONObject(String.format("https://api.openweathermap.org/data/2.5/forecast?appid=%s&units=standard&%s", this.apikey, parameter), HttpMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
