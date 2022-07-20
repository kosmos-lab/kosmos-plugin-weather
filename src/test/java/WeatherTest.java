import de.kosmos_lab.platform.plugins.weather.WeatherUtils;
import de.kosmos_lab.platform.plugins.weather.data.WeatherEntry;
import de.kosmos_lab.platform.plugins.weather.data.WeatherSpeed;
import de.kosmos_lab.platform.plugins.weather.data.WeatherTemperature;
import de.kosmos_lab.platform.plugins.weather.data.WeatherUnit;
import de.kosmos_lab.platform.plugins.weather.openweathermap.OpenWeatherMapProvider;
import de.kosmos_lab.utils.JSONChecker;
import de.kosmos_lab.utils.JSONFunctions;
import de.kosmos_lab.utils.exceptions.CompareException;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


public class WeatherTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("WeatherTest");

    public static void checkTemperature(WeatherTemperature temperature, WeatherUnit unit, double valueInUnit) {
        Double K = null;
        Double C = null;
        Double F = null;
        switch (unit) {
            case STANDARD:
                C = WeatherUtils.KelvinToCelsius(valueInUnit);
                F = WeatherUtils.KelvinToFahrenheit(valueInUnit);
                K = valueInUnit;
                break;
            case METRIC:
                C = valueInUnit;
                F = WeatherUtils.CelsiusToFahrenheit(valueInUnit);
                K = WeatherUtils.CelsiusToKelvin(valueInUnit);
                break;
            case IMPERIAL:
                F = valueInUnit;
                C = WeatherUtils.FahrenheitToCelsius(valueInUnit);
                K = WeatherUtils.FahrenheitToKelvin(valueInUnit);
                break;
            default:
                Assert.fail(String.format("Could not find conversion rates for %s", unit.name()));
        }
        Assert.assertEquals(temperature.get_C(), C, 0.01, "Temperature in Celsius not as expected");
        Assert.assertEquals(temperature.get_F(), F, 0.01, "Temperature in Fahrenheit not as expected");
        Assert.assertEquals(temperature.get_K(), K, 0.01, "Temperature in Kelvin not as expected");
    }

    public static void checkSpeed(WeatherSpeed speed, WeatherUnit unit, double valueInUnit) {
        Double ms = null;
        Double miles_h = null;
        Double km_h = null;
        switch (unit) {
            case STANDARD:
                ms = valueInUnit;
                km_h = WeatherUtils.MeterPerSecondToKmPerHour(valueInUnit);
                miles_h = WeatherUtils.MeterPerSecondToMilesPerHour(valueInUnit);
                break;
            case METRIC:
                ms = WeatherUtils.KmPerHourToMeterPerSecond(valueInUnit);
                km_h = valueInUnit;
                miles_h = WeatherUtils.KmPerHourToMilesPerHour(valueInUnit);
                break;
            case IMPERIAL:
                ms = WeatherUtils.MilesPerHourToMeterPerSecond(valueInUnit);
                km_h = WeatherUtils.MilesPerHourToKmPerHour(valueInUnit);
                miles_h = valueInUnit;
                break;
            default:
                Assert.fail(String.format("Could not find conversion rates for %s", unit.name()));
        }
        Assert.assertEquals(speed.get_meter_sec(), ms, 0.01, "Speed in m/s not as expected");
        Assert.assertEquals(speed.get_kilometer_hour(), km_h, 0.01, "Speed in km/h not as expected");
        Assert.assertEquals(speed.get_miles_hour(), miles_h, 0.01, "Speed in miles/h not as expected");
    }

    public static void checkIfJSONParsedCorrectly(JSONObject json, WeatherUnit unit) {
        WeatherEntry entry = new WeatherEntry();
        OpenWeatherMapProvider.updateFromJSON(entry, json);
        if (JSONFunctions.has(json, "main/temp")) {
            checkTemperature(entry.getTemperature(), unit, (double) JSONFunctions.get(json, "main/temp"));
        }
        if (JSONFunctions.has(json, "main/feels_like")) {
            checkTemperature(entry.getFeelsLike(), unit, (double) JSONFunctions.get(json, "main/feels_like"));
        }
        if (JSONFunctions.has(json, "main/temp_min")) {
            checkTemperature(entry.getTempMin(), unit, (double) JSONFunctions.get(json, "main/temp_min"));
        }
        if (JSONFunctions.has(json, "main/temp_max")) {
            checkTemperature(entry.getTempMax(), unit, (double) JSONFunctions.get(json, "main/temp_max"));
        }
        if (JSONFunctions.has(json, "wind/gust")) {
            checkSpeed(entry.getWindGust(), unit, (double) JSONFunctions.get(json, "wind/gust"));
        }
        if (JSONFunctions.has(json, "wind/speed")) {
            checkSpeed(entry.getWindSpeed(), unit, (double) JSONFunctions.get(json, "wind/speed"));
        }
        try {
            Assert.assertTrue(JSONChecker.equals(entry.getVisibility(), JSONFunctions.get(json, "visibility")), "Visibility was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getCloudiness(), JSONFunctions.get(json, "clouds/all")), "Cloudiness was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getPressure(), JSONFunctions.get(json, "main/pressure")), "Pressure was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getWindDegree(), JSONFunctions.get(json, "wind/deg")), "WindDegree was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getWeatherIcon(), JSONFunctions.get(json, "weather[0]/icon")), "WeatherIcon was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getWeatherCode(), JSONFunctions.get(json, "weather[0]/id")), "WeatherCode was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getRain1h(), JSONFunctions.get(json, "rain/1h")), "rain1h was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getRain3h(), JSONFunctions.get(json, "rain/3h")), "rain3h was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getSnow1h(), JSONFunctions.get(json, "snow/1h")), "snow1h was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getSnow3h(), JSONFunctions.get(json, "snow/3h")), "snow3h was not parsed correctly");
            Assert.assertTrue(JSONChecker.equals(entry.getHumidity(), JSONFunctions.get(json, "main/humidity")), "humidity was not parsed correctly");
        } catch (CompareException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public static void testWeather() {

        ContentResponse response = TestBase.clientNoLogin.getResponse("/weather/current?value=temp&q=bremen,germany&units=metric", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_metric = Double.parseDouble(response.getContentAsString());
        response = TestBase.clientNoLogin.getResponse("/weather/current/value?value=temp&q=bremen,germany&units=metric", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_metric2 = Double.parseDouble(response.getContentAsString());
        Assert.assertEquals(in_metric, in_metric2);
        response = TestBase.clientNoLogin.getResponse("/weather/current?value=temp&q=bremen,germany&units=imperial", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_imperial = Double.parseDouble(response.getContentAsString());

        response = TestBase.clientNoLogin.getResponse("/weather/current?value=temp&q=bremen,germany&units=standard", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_standard = Double.parseDouble(response.getContentAsString());

        Assert.assertEquals(in_imperial, WeatherUtils.CelsiusToFahrenheit(in_metric), 1);
        Assert.assertEquals(in_standard, WeatherUtils.CelsiusToKelvin(in_metric), 1);
    }

    @Test
    public static void testForecast() {

        ContentResponse response = TestBase.clientNoLogin.getResponse("/weather/forecast/value?value=temp&q=bremen,germany&units=metric&slot=1", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_metric = Double.parseDouble(response.getContentAsString());

        response = TestBase.clientNoLogin.getResponse("/weather/forecast?value=temp&q=bremen,germany&units=metric&slot=1", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_metric2 = Double.parseDouble(response.getContentAsString());
        Assert.assertEquals(in_metric, in_metric2);
        response = TestBase.clientNoLogin.getResponse("/weather/forecast?value=temp&q=bremen,germany&units=imperial&slot=1", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_imperial = Double.parseDouble(response.getContentAsString());

        response = TestBase.clientNoLogin.getResponse("/weather/forecast?value=temp&q=bremen,germany&units=standard&slot=1", HttpMethod.GET);
        Assert.assertEquals(response.getStatus(), 200, "Request did not return 200");
        double in_standard = Double.parseDouble(response.getContentAsString());

        Assert.assertEquals(in_imperial, WeatherUtils.CelsiusToFahrenheit(in_metric), 1);
        Assert.assertEquals(in_standard, WeatherUtils.CelsiusToKelvin(in_metric), 1);
    }


    @Test
    public void testWeatherParseStandard() {
        /*
        standard:
        {"coord":{"lon":8.8078,"lat":53.0752},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":292.59,"feels_like":292.7,"temp_min":290.27,"temp_max":293.04,"pressure":1015,"humidity":81},"visibility":10000,"wind":{"speed":2.06,"deg":250},"clouds":{"all":100},"dt":1656330816,"sys":{"type":1,"id":1281,"country":"DE","sunrise":1656298811,"sunset":1656359711},"timezone":7200,"id":2944388,"name":"Bremen","cod":200}
        metric:
        {"coord":{"lon":8.8078,"lat":53.0752},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":19.44,"feels_like":19.55,"temp_min":17.12,"temp_max":19.89,"pressure":1015,"humidity":81},"visibility":10000,"wind":{"speed":2.06,"deg":250},"clouds":{"all":100},"dt":1656330604,"sys":{"type":1,"id":1281,"country":"DE","sunrise":1656298811,"sunset":1656359711},"timezone":7200,"id":2944388,"name":"Bremen","cod":200}
        imperial:
        {"coord":{"lon":8.8078,"lat":53.0752},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":66.99,"feels_like":67.19,"temp_min":62.82,"temp_max":67.8,"pressure":1015,"humidity":81},"visibility":10000,"wind":{"speed":4.61,"deg":250},"clouds":{"all":100},"dt":1656330560,"sys":{"type":1,"id":1281,"country":"DE","sunrise":1656298811,"sunset":1656359711},"timezone":7200,"id":2944388,"name":"Bremen","cod":200}
         */
        JSONObject standard = new JSONObject("{\"coord\":{\"lon\":8.8078,\"lat\":53.0752},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":292.59,\"feels_like\":292.7,\"temp_min\":290.27,\"temp_max\":293.04,\"pressure\":1015,\"humidity\":81},\"visibility\":10000,\"wind\":{\"speed\":2.06,\"deg\":250},\"clouds\":{\"all\":100},\"dt\":1656330816,\"sys\":{\"type\":1,\"id\":1281,\"country\":\"DE\",\"sunrise\":1656298811,\"sunset\":1656359711},\"timezone\":7200,\"id\":2944388,\"name\":\"Bremen\",\"cod\":200}");
        checkIfJSONParsedCorrectly(standard, WeatherUnit.STANDARD);
        standard.remove("visibility");
        checkIfJSONParsedCorrectly(standard, WeatherUnit.STANDARD);
        standard.getJSONArray("weather").getJSONObject(0).remove("icon");
        checkIfJSONParsedCorrectly(standard, WeatherUnit.STANDARD);


    }
}
