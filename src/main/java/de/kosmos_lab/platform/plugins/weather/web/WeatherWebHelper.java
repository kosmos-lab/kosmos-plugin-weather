package de.kosmos_lab.platform.plugins.weather.web;

import de.dfki.baall.helper.webserver.exceptions.ParameterNotFoundException;
import de.kosmos_lab.kosmos.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.platform.plugins.weather.WeatherUtils;
import de.kosmos_lab.platform.plugins.weather.data.WeatherEntry;
import de.kosmos_lab.platform.plugins.weather.data.WeatherSpeed;
import de.kosmos_lab.platform.plugins.weather.data.WeatherTemperature;
import de.kosmos_lab.platform.plugins.weather.data.WeatherUnit;
import de.kosmos_lab.platform.plugins.weather.data.WeatherValue;
import jakarta.servlet.http.HttpServletResponse;

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

    public static void send(KosmoSHttpServletRequest request, HttpServletResponse response, Object value, WeatherUnit units) throws IOException {
        if (value == null) {
            response.setStatus(204);

            return;
        }
        if (value instanceof WeatherTemperature) {
            if (units == WeatherUnit.METRIC) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherTemperature) value).get_C()));
                return;
            }
            if (units == WeatherUnit.STANDARD) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherTemperature) value).get_K()));
                return;
            }
            if (units == WeatherUnit.IMPERIAL) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherTemperature) value).get_F()));
                return;
            }
        }
        if (value instanceof WeatherSpeed) {
            if (units == WeatherUnit.METRIC) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherSpeed) value).get_kilometer_hour()));
                return;
            }
            if (units == WeatherUnit.STANDARD) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherSpeed) value).get_meter_sec()));
                return;
            }
            if (units == WeatherUnit.IMPERIAL) {
                sendText(request, response, String.format(DoubleFormat, ((WeatherSpeed) value).get_miles_hour()));
                return;
            }
        }
        if (value instanceof Double) {
            sendText(request, response, String.format(DoubleFormat, ((Double) value)));
            return;

        }
        if (value instanceof Integer) {
            sendText(request, response, String.format(IntFormat, ((Integer) value)));
            return;
        }
        if (value instanceof String) {
            sendText(request, response, (String)value);
            return;
        }
        response.setStatus(500);

    }

    public static void sendValue(KosmoSHttpServletRequest request, HttpServletResponse response, WeatherEntry w, WeatherValue value) throws IOException, ParameterNotFoundException {
        if (value == null) {
            value = WeatherUtils.searchEnum(WeatherValue.class, request.getParameter("value", true));
        }
        if ( value == null ) {

            response.sendError(500,"could not find value");
            return;
        }
        WeatherUnit units = getUnit(request);
        switch (value) {
            case temp:
                send(request, response, w.getTemperature(), units);
                return;

            case temp_max:

                /*if (w.getTempMax() != null) {
                    if (units == WeatherUnit.METRIC) {
                        sendText(request, response, String.format(DoubleFormat, w.getTempMax().get_C()));
                        return;
                    }
                    if (units == WeatherUnit.IMPERIAL) {
                        sendText(request, response, String.format(DoubleFormat, w.getTempMax().get_F()));
                        return;
                    }
                    sendText(request, response, String.format(DoubleFormat, w.getTempMax().get_K()));
                    return;

                }
                response.setStatus(204);*/
                send(request, response, w.getTempMax(), units);
                return;
            case temp_min:
                send(request, response, w.getTempMin(), units);

                return;

            case feelsLike:
                send(request, response, w.getFeelsLike(), units);

                return;

            case pressure:
                send(request, response, w.getPressure(), units);

                return;
            case humidity:
                send(request, response, w.getHumidity(), units);

                return;
            case weatherCode:
                send(request, response, w.getWeatherCode(), units);

                return;
            case weatherIcon:
                send(request, response, w.getWeatherIcon(), units);

                return;
            case cloudiness:
                send(request, response, w.getCloudiness(), units);

                return;
            case rain1h:
                send(request, response, w.getRain1h(), units);

                return;

            case rain3h:
                send(request, response, w.getRain3h(), units);

                return;
            case snow1h:
                send(request, response, w.getSnow1h(), units);

                return;
            case snow3h:
                send(request, response, w.getSnow3h(), units);

                return;
            case windDegree:
                send(request, response, w.getWindDegree(), units);

                return;
            case windSpeed:
                send(request, response, w.getWindSpeed(), units);

                return;
            case windGust:
                send(request, response, w.getWindGust(), units);

                return;
            case visibility:
                send(request, response, w.getVisibility(), units);

                return;
        }
    }

    public static String getParameters(KosmoSHttpServletRequest request) {

        try {
            String query = request.getString("q");
            if (query != null) {
                return String.format("q=%s", query);
            }


        } catch (ParameterNotFoundException ex) {

        }
        try {

            String lon = request.getString("lon");
            String lat = request.getString("lat");
            if (lat != null && lon != null) {
                return String.format("lat=%s&lon=%s", lat, lon);
            }

        } catch (ParameterNotFoundException ex) {

        }
        return null;
    }
}
