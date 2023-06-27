package de.kosmos_lab.platform.plugins.weather.web;

import de.kosmos_lab.web.doc.openapi.ApiEndpoint;
import de.kosmos_lab.platform.IController;
import de.kosmos_lab.platform.web.KosmoSWebServer;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;


@Extension
@ApiEndpoint(
        path = "/weather/forecast/value",
        hidden = true
)
public class WeatherForecastGetValueServlet extends WeatherForecastServlet implements ExtensionPoint {
    public WeatherForecastGetValueServlet(KosmoSWebServer webServer, IController controller) {
        super(webServer, controller);
    }
}

