package de.kosmos_lab.platform.plugins.weather;

public class WeatherUtils {
    public final static double absolute_zero = -273.15;

    public static <T extends Enum<?>> T searchEnum(Class<T> enumeration,
                                                   String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

    // Method to convert Celcius to Fahrenheit
    public static double CelsiusToFahrenheit(double C) {
        return (C * 9 / 5) + 32;

    }

    // Method to convert Celcius to Kelvin
    public static double CelsiusToKelvin(double C) {
        return C - absolute_zero;

    }

    // Method to convert Fahrenheit to Celcius
    public static double FahrenheitToCelsius(double F) {
        return (F - 32) * 5 / 9;

    }

    // Method to convert Fahrenheit to Kelvin
    public static double FahrenheitToKelvin(double F) {
        return FahrenheitToCelsius(F) - absolute_zero;

    }

    // Method to convert Kelvin to Celcius
    public static double KelvinToCelsius(double K) {
        return K + absolute_zero;

    }

    // Method to convert Kelvin to Fahrenheit
    public static double KelvinToFahrenheit(double K) {
        return CelsiusToFahrenheit(KelvinToCelsius(K));

    }

    public static double MeterPerSecondToKmPerHour(double value) {
        return value * 3.6;
    }

    public static double MeterPerSecondToMilesPerHour(double value) {
        return value * 2.236936;
    }

    public static Double KmPerHourToMeterPerSecond(double value) {
        return value / 3.6;

    }
    public static Double MilesPerHourToMeterPerSecond(double value) {
        return value / 2.236936;

    }

    public static Double KmPerHourToMilesPerHour(double value) {
        return MeterPerSecondToMilesPerHour(KmPerHourToMeterPerSecond(value));
    }

    public static Double MilesPerHourToKmPerHour(double value) {
        return MilesPerHourToMeterPerSecond(MeterPerSecondToKmPerHour(value));
    }
}
