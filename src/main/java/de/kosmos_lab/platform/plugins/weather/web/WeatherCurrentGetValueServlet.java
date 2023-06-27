package de.kosmos_lab.platform.plugins.weather.web;

import de.kosmos_lab.platform.IController;
import de.kosmos_lab.platform.web.KosmoSWebServer;
import de.kosmos_lab.web.doc.openapi.ApiEndpoint;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;


@Extension
@ApiEndpoint(
        path = "/weather/current/value",
        hidden = true
)
public class WeatherCurrentGetValueServlet extends WeatherCurrentServlet implements ExtensionPoint {
    public WeatherCurrentGetValueServlet(KosmoSWebServer webServer, IController controller) {
        super(webServer, controller);
    }
}

