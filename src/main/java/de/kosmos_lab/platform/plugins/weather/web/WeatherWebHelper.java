package de.kosmos_lab.platform.plugins.weather.web;

import de.kosmos_lab.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.platform.web.servlets.KosmoSServlet;
import de.kosmos_lab.web.exceptions.ParameterNotFoundException;

import de.kosmos_lab.platform.plugins.weather.WeatherUtils;
import de.kosmos_lab.platform.plugins.weather.data.WeatherEntry;
import de.kosmos_lab.platform.plugins.weather.data.WeatherSpeed;
import de.kosmos_lab.platform.plugins.weather.data.WeatherTemperature;
import de.kosmos_lab.platform.plugins.weather.data.WeatherUnit;
import de.kosmos_lab.platform.plugins.weather.data.WeatherValue;
import de.kosmos_lab.web.server.WebServer;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;

import java.io.IOException;


public class WeatherWebHelper {
    public static final String StringFormat = "%s";
    public static final String DoubleFormat = "%.1f";
    public static final String IntFormat = "%d";

    public static WeatherUnit getUnit(KosmoSHttpServletRequest request) {
        //return WeatherUnit.valueOf(request.getString("units", "metric").toUpperCase());
        try {
            return WeatherUtils.searchEnum(WeatherUnit.class, request.getParameter("units", true));
        } catch (ParameterNotFoundException e) {
            //e.printStackTrace();
        }
        return WeatherUnit.METRIC;


    }

    static void sendText(KosmoSHttpServletRequest req, HttpServletResponse response, String text) throws IOException {
        response.setHeader("Content-Type", "text/plain");
        response.getWriter().print(text);
    }

    public static String get(Object value, WeatherUnit units) {
        if (value == null) {
            return null;
        }
        if (value instanceof WeatherTemperature) {
            if (units == WeatherUnit.METRIC) {
                return (String.format(DoubleFormat, ((WeatherTemperature) value).get_C()));
            }
            if (units == WeatherUnit.STANDARD) {
                return (String.format(DoubleFormat, ((WeatherTemperature) value).get_K()));
            }
            if (units == WeatherUnit.IMPERIAL) {
                return (String.format(DoubleFormat, ((WeatherTemperature) value).get_F()));

            }
        }
        if (value instanceof WeatherSpeed) {
            if (units == WeatherUnit.METRIC) {
                return (String.format(DoubleFormat, ((WeatherSpeed) value).get_kilometer_hour()));

            }
            if (units == WeatherUnit.STANDARD) {
                return (String.format(DoubleFormat, ((WeatherSpeed) value).get_meter_sec()));

            }
            if (units == WeatherUnit.IMPERIAL) {
                return (String.format(DoubleFormat, ((WeatherSpeed) value).get_miles_hour()));

            }
        }
        if (value instanceof Double) {
            return (String.format(DoubleFormat, ((Double) value)));
        }
        if (value instanceof Integer) {
            return (String.format(IntFormat, ((Integer) value)));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;

    }


    public static void sendValue(KosmoSHttpServletRequest request, HttpServletResponse response, WeatherEntry w, WeatherValue value) throws IOException, ParameterNotFoundException {
        WeatherUnit units = getUnit(request);
        String all = request.getParameter("all", false);

        if (all != null) {
            boolean doAll = false;
            if (all.length() == 0) {
                doAll = true;
            } else {
                doAll = request.getBoolean("all", true);
            }
            if (doAll) {
                JSONObject jsonObject = new JSONObject();
                for (WeatherValue v : WeatherValue.values()) {

                    if (v != null) {
                        jsonObject.put(v.name(), getValue(w, v, units));
                    }
                }
                KosmoSServlet.sendJSON(request, response, jsonObject);
                return;
            }
        }
        String svalues = request.getParameter("values", false);


        if (svalues != null) {
            String[] vals = svalues.split(",");
            if (vals.length > 0) {
                JSONObject jsonObject = new JSONObject();
                for (String v : vals) {
                    value = WeatherUtils.searchEnum(WeatherValue.class, v);
                    if (value != null) {
                        jsonObject.put(value.name(), getValue(w, value, units));
                    }
                }
                KosmoSServlet.sendJSON(request, response, jsonObject);
                return;
            }
        }
        String svalue = request.getParameter("value", false);
        if (value == null) {
            value = WeatherUtils.searchEnum(WeatherValue.class, svalue);
        }
        if (value == null) {
            response.sendError(WebServer.STATUS_NOT_FOUND, String.format("could not find value %s - please check the name"));
            return;
        }
        String v = getValue(w, value, units);
        if (v != null) {
            sendText(request, response, v);

            return;
        }
        response.sendError(WebServer.STATUS_NOT_FOUND, "could not find the data you were looking for");
        return;
    }

    public static String getValue(WeatherEntry w, WeatherValue value, WeatherUnit units) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case temp:
                return get(w.getTemperature(), units);
            case tempMax:
                return get(w.getTempMax(), units);
            case tempMin:
                return get(w.getTempMin(), units);
            case feelsLike:
                return get(w.getFeelsLike(), units);
            case pressure:
                return get(w.getPressure(), units);
            case humidity:
                return get(w.getHumidity(), units);
            case weatherCode:
                return get(w.getWeatherCode(), units);
            case weatherIcon:
                return get(w.getWeatherIcon(), units);
            case cloudiness:
                return get(w.getCloudiness(), units);
            case rain1h:
                return get(w.getRain1h(), units);
            case rain3h:
                return get(w.getRain3h(), units);
            case snow1h:
                return get(w.getSnow1h(), units);
            case snow3h:
                return get(w.getSnow3h(), units);
            case windDegree:
                return get(w.getWindDegree(), units);
            case windSpeed:
                return get(w.getWindSpeed(), units);
            case windGust:
                return get(w.getWindGust(), units);
            case visibility:
                return get(w.getVisibility(), units);
        }
        return null;
    }

    public static String getParameters(KosmoSHttpServletRequest request) {
        try {

            String lon = request.getString("lon");
            String lat = request.getString("lat");
            if (lat != null && lon != null) {
                return String.format("lat=%s&lon=%s", lat, lon);
            }

        } catch (ParameterNotFoundException ex) {

        }
        try {
            String query = request.getString("q");
            if (query != null) {
                return String.format("q=%s", UrlEncoded.encodeString(query));
            }


        } catch (ParameterNotFoundException ex) {

        }

        return null;
    }
}
