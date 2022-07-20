package de.kosmos_lab.platform.plugins.weather.web;

import de.dfki.baall.helper.webserver.exceptions.ParameterNotFoundException;
import de.kosmos_lab.kosmos.doc.openapi.ApiEndpoint;
import de.kosmos_lab.kosmos.platform.IController;
import de.kosmos_lab.kosmos.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.kosmos.platform.web.WebServer;
import de.kosmos_lab.kosmos.platform.web.servlets.KosmoSServlet;
import de.kosmos_lab.platform.plugins.weather.WeatherController;
import de.kosmos_lab.platform.plugins.weather.data.WeatherForecast;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;



@Extension
@ApiEndpoint(
        path = "/weather/forecast/value",
        hidden = true
)
public class WeatherForecastGetValueServlet extends WeatherForecastServlet implements ExtensionPoint {
    public WeatherForecastGetValueServlet(WebServer webServer, IController controller) {
        super(webServer, controller);
    }
}

