import de.kosmos_lab.platform.plugins.weather.WeatherUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WeatherUtilsTest {
    final static double allowed_delta = 0.01;

    @Test
    public static void CelsiusToFahrenheit() {
        Assert.assertEquals(WeatherUtils.CelsiusToFahrenheit(0), 32, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToFahrenheit(25), 77, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToFahrenheit(50), 122, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToFahrenheit(100), 212, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToFahrenheit(-40), -40, allowed_delta);
    }

    @Test
    public static void CelsiusToKelvin() {
        Assert.assertEquals(WeatherUtils.CelsiusToKelvin(0), 273.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToKelvin(25), 298.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToKelvin(50), 323.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToKelvin(100), 373.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.CelsiusToKelvin(-40), 233.15, allowed_delta);
    }

    @Test
    public static void FahrenheitToCelsius() {
        Assert.assertEquals(WeatherUtils.FahrenheitToCelsius(0), -17.777777778, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToCelsius(25), -3.8888888889, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToCelsius(50), 10, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToCelsius(100), 37.777777778, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToCelsius(-40), -40, allowed_delta);
    }

    @Test
    public static void FahrenheitToKelvin() {
        Assert.assertEquals(WeatherUtils.FahrenheitToKelvin(0), 255.37222222, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToKelvin(25), 269.26111111, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToKelvin(50), 283.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToKelvin(100), 310.92777778, allowed_delta);
        Assert.assertEquals(WeatherUtils.FahrenheitToKelvin(-40), 233.15, allowed_delta);
    }

    @Test
    public static void KelvinToCelsius() {
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(0), -273.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(100), -173.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(200), -73.15, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(273.15), 0, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(373.15), 100, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToCelsius(500), 226.85, allowed_delta);
    }

    @Test
    public static void KelvinToFahrenheit() {
        Assert.assertEquals(WeatherUtils.KelvinToFahrenheit(0), -459.67, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToFahrenheit(25), -414.67, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToFahrenheit(50), -369.67, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToFahrenheit(100), -279.67, allowed_delta);
        Assert.assertEquals(WeatherUtils.KelvinToFahrenheit(-40), -531.67, allowed_delta);
    }
}
