package de.kosmos_lab.platform.plugins.weather.web;

import de.kosmos_lab.web.annotations.Operation;
import de.kosmos_lab.web.annotations.Parameter;
import de.kosmos_lab.web.annotations.enums.ParameterIn;
import de.kosmos_lab.web.annotations.enums.SchemaType;
import de.kosmos_lab.web.annotations.media.Content;
import de.kosmos_lab.web.annotations.media.Schema;
import de.kosmos_lab.web.annotations.responses.ApiResponse;
import de.kosmos_lab.web.doc.openapi.ApiEndpoint;
import de.kosmos_lab.web.doc.openapi.ResponseCode;
import de.kosmos_lab.web.exceptions.ParameterNotFoundException;
import de.kosmos_lab.platform.IController;
import de.kosmos_lab.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.platform.web.KosmoSWebServer;
import de.kosmos_lab.platform.web.servlets.KosmoSServlet;
import de.kosmos_lab.platform.plugins.weather.WeatherController;
import de.kosmos_lab.platform.plugins.weather.data.WeatherForecast;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

import java.io.IOException;

@Extension
@ApiEndpoint(
        path = "/weather/forecast"
)
public class WeatherForecastServlet extends KosmoSServlet implements ExtensionPoint {

    private final WeatherController controller;

    public WeatherForecastServlet(KosmoSWebServer webServer, IController controller) {
        super(webServer, controller);
        this.controller = WeatherController.getInstance(controller);
    }

    @Operation(

            tags = {"weather"},

            description = "get information about the future weather.\nEither lon&lat or q needs to be used",
            summary = "get weather forecast",

            parameters = {

                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "q",
                            ref = "#/components/parameters/weatherQuery"

                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "lon",
                            ref = "#/components/parameters/weatherLon"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "lat",
                            ref = "#/components/parameters/weatherLat"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "value",
                            ref = "#/components/parameters/weatherValue"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "unit",
                            ref = "#/components/parameters/weatherUnit"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "values",
                            ref = "#/components/parameters/weatherValues"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "all",
                            ref = "#/components/parameters/weatherAll"
                    ),
                    @Parameter(
                            required = true,
                            in = ParameterIn.QUERY,
                            name = "slot",
                            description = "The slot you are interested in, 1 means in 3h, 2 means in 6h, 3 in 9h and so on",
                            schema = @Schema(
                                    type = SchemaType.INTEGER,
                                    minimum = "1",
                                    maximum = "40"
                            )


                    )


            },

            responses = {
                    @ApiResponse(responseCode = @ResponseCode(statusCode = de.kosmos_lab.web.server.WebServer.STATUS_OK), description = "The wanted value", content = @Content(mediaType = MediaType.TEXT_PLAIN)),

            }
    )
    public void get(KosmoSHttpServletRequest request, HttpServletResponse response) throws IOException, ParameterNotFoundException {
        String parameters = WeatherWebHelper.getParameters(request);
        if (parameters != null) {
            WeatherForecast f = controller.getForecast(parameters);
            if (f != null) {
                try {
                    WeatherWebHelper.sendValue(request, response, f.getForecast(request.getInt("slot", 1)), null);
                    return;
                } catch (IndexOutOfBoundsException ex) {
                    response.sendError(de.kosmos_lab.web.server.WebServer.STATUS_NOT_FOUND, "The timeslot you wanted is not available");
                    return;
                }
            }
            response.sendError(de.kosmos_lab.web.server.WebServer.STATUS_NOT_FOUND, "The forecast could not be found");
            return;
        }
    }


}

