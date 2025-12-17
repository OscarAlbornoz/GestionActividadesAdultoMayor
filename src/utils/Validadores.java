package utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.ResolverStyle;

public final class Validadores
{
    private Validadores() {}

    public static boolean validarRUT(String rut) {
        if (rut == null) return false;

        rut = rut.trim()
                .toUpperCase()
                .replaceAll("[^0-9K]", "");

        if (rut.length() < 2) return false;

        String cuerpo = rut.substring(0, rut.length() - 1);
        char dvIngresado = rut.charAt(rut.length() - 1);

        if (!cuerpo.matches("\\d+")) return false;

        int suma = 0;
        int multiplicador = 2;

        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int residuo = 11 - (suma % 11);

        char dvCalculado;
        if (residuo == 11) dvCalculado = '0';
        else if (residuo == 10) dvCalculado = 'K';
        else dvCalculado = (char) ('0' + residuo);

        return dvIngresado == dvCalculado;
    }

    public static boolean validarFecha(String strDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(strDate, formatter);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
