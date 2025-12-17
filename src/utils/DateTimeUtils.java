package utils;

import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils
{
    private DateTimeUtils() {}

    public static final DateTimeFormatter DATE_CL =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT)
                    .withLocale(Locale.forLanguageTag("es-CL"));

    public static final DateTimeFormatter HORA_MIN =
            DateTimeFormatter.ofPattern("HH:mm")
                    .withLocale(Locale.getDefault());


    public static LocalDate epochALocalDate(long epochSegundos) {
        return Instant.ofEpochSecond(epochSegundos).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static long localDateAEpoch(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    public static LocalTime minutosALocalTime(int minutos) {
        if (minutos < 0 || minutos >= 24 * 60) {
            throw new IllegalArgumentException("Minutos fuera de rango.");
        }
        return LocalTime.of(minutos / 60, minutos % 60);
    }

    public static int localTimeAMinutos(LocalTime tiempo) {
        return tiempo.getHour() * 60 + tiempo.getMinute();
    }

    public static int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public static LocalTime dateALocalTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
    }

    public static Date localTimeToDate(LocalTime time) {
        return Date.from(time.atDate(LocalDate.now())
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static int convertirTextoADia(String texto) {
        if (texto == null || texto.isEmpty()) return -1;

        String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase();

        if (limpio.contains("lunes")) return 1;
        if (limpio.contains("martes")) return 2;
        if (limpio.contains("miercoles")) return 3;
        if (limpio.contains("jueves")) return 4;
        if (limpio.contains("viernes")) return 5;
        if (limpio.contains("sabado")) return 6;
        if (limpio.contains("domingo")) return 7;

        return -1;
    }

    public static String aTitleCase(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
