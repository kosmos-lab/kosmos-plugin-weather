package de.kosmos_lab.platform.plugins.weather.web;

import de.dfki.baall.helper.webserver.exceptions.ParameterNotFoundException;
import de.kosmos_lab.kosmos.annotations.Operation;
import de.kosmos_lab.kosmos.annotations.Parameter;
import de.kosmos_lab.kosmos.annotations.enums.ParameterIn;
import de.kosmos_lab.kosmos.annotations.media.Content;
import de.kosmos_lab.kosmos.annotations.media.ExampleObject;
import de.kosmos_lab.kosmos.annotations.media.Schema;
import de.kosmos_lab.kosmos.annotations.responses.ApiResponse;
import de.kosmos_lab.kosmos.doc.openapi.ApiEndpoint;
import de.kosmos_lab.kosmos.platform.IController;
import de.kosmos_lab.kosmos.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.kosmos.platform.web.WebServer;
import de.kosmos_lab.kosmos.platform.web.servlets.KosmoSServlet;
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

    public WeatherForecastServlet(WebServer webServer, IController controller) {
        super(webServer, controller);
        this.controller = WeatherController.getInstance(controller);
    }

    @Operation(

            tags = {"weather"},

            description = "get information about the current weather",

            parameters = {

                    @Parameter(
                            name = "q",
                            description = "A searchquery to look for a place",
                            schema = @Schema(
                                    type = "string"
                            ),
                            in = ParameterIn.QUERY,
                            required = false,

                            examples = {
                                    @ExampleObject(name = "Bremen, Germany"),
                                    @ExampleObject(name = "Las Vegas")

                            }

                    ),
                    @Parameter(
                            name = "lon",
                            description = "the longitude to look for",
                            schema = @Schema(
                                    type = "number"

                            ),
                            in = ParameterIn.QUERY,
                            required = true,
                            examples = {@ExampleObject(name = "8.806422")}
                    ),
                    @Parameter(
                            name = "lat",
                            description = "the latitude to look for",
                            schema = @Schema(
                                    type = "number"

                            ),
                            in = ParameterIn.QUERY,
                            required = true,
                            examples = {@ExampleObject(name = "53.073635")}
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "unit",
                            schema=  @Schema(ref = "#/components/schemas/weatherUnit")
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "value",
                            schema=  @Schema(ref = "#/components/schemas/weatherValue")
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "slot",
                            description = "the slot you are interested in, 1 means in 3h, 2 means in 6h, 3 in 9h and so on",
                            schema = @Schema(
                                    type = "number",
                                    minimum = "1",
                                    maximum = "10"
                            )


                    )


            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "The wanted value", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/UnknownError"),
                    @ApiResponse(responseCode = "422", ref = "#/components/responses/MissingValuesError"),

            }
    )
    public void get(KosmoSHttpServletRequest request, HttpServletResponse response) throws IOException, ParameterNotFoundException {


        String parameters = WeatherWebHelper.getParameters(request);

        if (parameters != null) {

            WeatherForecast f = controller.getForecast(parameters);

            if (f != null) {

                WeatherWebHelper.sendValue(request, response, f.getForecast(request.getInt("slot", 1)), null);

            }
        }
    }


}

