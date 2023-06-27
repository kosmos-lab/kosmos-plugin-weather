package de.kosmos_lab.platform.plugins.weather;

import de.kosmos_lab.platform.IController;
import de.kosmos_lab.platform.plugins.weather.data.WeatherCurrent;
import de.kosmos_lab.platform.plugins.weather.data.WeatherForecast;
import de.kosmos_lab.platform.plugins.weather.openweathermap.OpenWeatherMapProvider;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.LinkedList;

public class WeatherController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("WeatherController");

    private static WeatherController instance;

    private final IController controller;
    private final LinkedList<WeatherProvider> providers = new LinkedList<>();


    public WeatherController(IController controller) {

        this.controller = controller;
        File dir = controller.getFile("config/weather");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.error("could not create weather directory");
                controller.stop();

            }
        }

        //this.save();
        String owm_key = System.getenv("OWM_KEY");
        if (owm_key != null) {
            this.providers.add(new OpenWeatherMapProvider(owm_key));
        } else {
            logger.error("NO OWM KEY FOUND! Please set the Environment Variable OWM_KEY with an OWM Key, see: https://home.openweathermap.org/api_keys");
        }

    }

    public static synchronized WeatherController getInstance(IController controller) {

        if (instance == null) {
            instance = new WeatherController(controller);
        }

        return instance;
    }

    private void save() {

    }

    @CheckForNull
    public WeatherCurrent getCurrentWeather(@Nonnull String parameters) {
        for (WeatherProvider p : providers) {
            WeatherCurrent weatherCurrent = p.getCurrentWeather(parameters);
            if (weatherCurrent != null) {
                return weatherCurrent;
            }
        }
        //logger.info("did not find any viable data from our providers");
        return null;

    }

    @CheckForNull
    public WeatherForecast getForecast(@Nonnull String parameters) {
        for (WeatherProvider p : providers) {
            WeatherForecast forecast = p.getForecast(parameters);
            if (forecast != null) {
                return forecast;
            }
        }
        return null;

    }
}
