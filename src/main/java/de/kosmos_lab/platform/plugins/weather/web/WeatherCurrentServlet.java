package de.kosmos_lab.platform.plugins.weather.web;

import de.kosmos_lab.web.annotations.Operation;
import de.kosmos_lab.web.annotations.Parameter;
import de.kosmos_lab.web.annotations.enums.Explode;
import de.kosmos_lab.web.annotations.enums.ParameterIn;
import de.kosmos_lab.web.annotations.enums.SchemaType;
import de.kosmos_lab.web.annotations.media.ArraySchema;
import de.kosmos_lab.web.annotations.media.Content;
import de.kosmos_lab.web.annotations.media.ExampleObject;
import de.kosmos_lab.web.annotations.media.Schema;
import de.kosmos_lab.web.annotations.responses.ApiResponse;
import de.kosmos_lab.web.annotations.tags.Tag;
import de.kosmos_lab.web.doc.openapi.ApiEndpoint;
import de.kosmos_lab.web.doc.openapi.ResponseCode;
import de.kosmos_lab.web.exceptions.ParameterNotFoundException;
import de.kosmos_lab.platform.IController;
import de.kosmos_lab.platform.web.KosmoSHttpServletRequest;
import de.kosmos_lab.platform.web.KosmoSWebServer;
import de.kosmos_lab.platform.web.servlets.KosmoSServlet;
import de.kosmos_lab.platform.plugins.weather.WeatherController;
import de.kosmos_lab.web.doc.openapi.ApiEndpoint;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

import java.io.IOException;

@Extension
@ApiEndpoint(
        path = "/weather/current"
)
@Parameter(
        name = "q",
        componentName = "weatherQuery",
        description = "A search query to look for a place  \nIncompatible with lon&lat.",
        schema = @Schema(
                type = SchemaType.STRING
        ),
        in = ParameterIn.QUERY,
        required = false,
        examples = {
                @ExampleObject(value = "Bremen, Germany"),
                @ExampleObject(value = "Las Vegas")
        }

)
@Parameter(
        name = "lon",
        componentName = "weatherLon",
        description = "The longitude to look for.  \nIncompatible with q.",
        schema = @Schema(
                type = SchemaType.NUMBER
        ),
        in = ParameterIn.QUERY,
        required = false,
        examples = {@ExampleObject(value = "8.806422")}
)
@Parameter(
        name = "lat",
        componentName = "weatherLat",
        description = "The latitude to look for.  \nIncompatible with q.",
        schema = @Schema(
                type = SchemaType.NUMBER
        ),
        in = ParameterIn.QUERY,
        required = false,
        examples = {@ExampleObject(value = "53.073635")}
)
@Parameter(
        name = "unit",
        in = ParameterIn.QUERY,
        componentName = "weatherUnit",
        description = "The unit system you want to get the data in.",
        schema = @Schema(
                name = "weatherUnit",

                type = SchemaType.STRING,
                allowableValues = {"metric", "imperial", "standard"},
                defaultValue = "metric"
        )
)
@Parameter(
        in = ParameterIn.QUERY,
        componentName = "weatherValue",
        description = "The value you are interested in.  \n Incompatible with 'values' and 'all', use only one of 'value','values' or 'all'.",
        name = "value",
        required = false,
        schema = @Schema(ref = "#/components/schemas/weatherValue")
)
@Parameter(
        in = ParameterIn.QUERY,
        componentName = "weatherValues",
        name = "values",
        required = false,
        description = "A comma seperated list of all #/components/schemas/weatherValue you want to get returned.  \n Incompatible with 'value' and 'all', use only one of 'value','values' or 'all'.",
        array = @ArraySchema(
                minItems = 1,
                uniqueItems = true,
                arraySchema = @Schema(ref = "#/components/schemas/weatherValue")
        ),
        explode = Explode.TRUE


)
@Parameter(
        in = ParameterIn.QUERY,
        componentName = "weatherAll",
        description = "Set to true to return all available values in a JSONObject.  \n Incompatible with 'value' and 'values', use only one of 'value','values' or 'all'.",
        name = "all",
        required = false,
        allowEmptyValue = true,
        schema = @Schema(type = SchemaType.BOOLEAN, defaultValue = "false")

)
@Schema(
        name = "weatherValue",
        description = "The value you are interested in",
        type = SchemaType.STRING,
        allowableValues = {
                "temp",
                "tempMax",
                "tempMin",
                "feelsLike",
                "pressure",
                "humidity",
                "weatherIcon",
                "weatherCode",
                "cloudiness",
                "windSpeed",
                "windGust",
                "windDegree",
                "visibility",
                "rain1h",
                "rain3h",
                "snow1h",
                "snow3h"},

        required = true
)
@Tag(name = "weather", description = "Provides weather information from the Plugin 'kosmos-plugin-weather'.  \nAll operations need to have the following parameters:  \nEither 'q' or 'lat' and 'lon' to set the location you are looking for.  \nEither 'value','values' or 'all' to set the value(s) you want to get.")
public class WeatherCurrentServlet extends KosmoSServlet implements ExtensionPoint {

    private final WeatherController controller;

    public WeatherCurrentServlet(KosmoSWebServer webServer, IController controller) {
        super(webServer, controller);
        this.controller = WeatherController.getInstance(controller);
    }

    @Operation(

            tags = {"weather"},

            description = "Get information about the current weather",
            summary = "get current weather",

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

                            name = "values",
                            ref = "#/components/parameters/weatherValues"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "all",
                            ref = "#/components/parameters/weatherAll"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,

                            name = "unit",
                            ref = "#/components/parameters/weatherUnit"
                    ),


            },
            responses = {
                    @ApiResponse(responseCode = @ResponseCode(statusCode = de.kosmos_lab.web.server.WebServer.STATUS_OK), description = "The wanted value", content = @Content(mediaType = MediaType.TEXT_PLAIN)),

            }
    )
    public void get(KosmoSHttpServletRequest request, HttpServletResponse response) throws IOException, ParameterNotFoundException {


        String parameters = WeatherWebHelper.getParameters(request);

        if (parameters != null) {

            de.kosmos_lab.platform.plugins.weather.data.WeatherCurrent w = controller.getCurrentWeather(parameters);

            if (w != null) {
                WeatherWebHelper.sendValue(request, response, w.getData(), null);

            }
        }
    }


}

