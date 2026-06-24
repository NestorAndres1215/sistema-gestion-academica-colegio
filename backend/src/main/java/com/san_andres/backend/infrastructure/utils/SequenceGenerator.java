package com.san_andres.backend.infrastructure.utils;


import com.san_andres.backend.domain.exceptions.BadRequestException;

public final class SequenceGenerator  {

    private static final String DEFAULT_SEQUENCE = "00000000";
    private static final int NUMERIC_LENGTH = 2;

    public static String generateCode(String secuenciaActual) {

        if (secuenciaActual == null) {
            secuenciaActual = DEFAULT_SEQUENCE;
        }

        String parteAlfabetica = secuenciaActual.substring(0, secuenciaActual.length() - NUMERIC_LENGTH);
        String parteNumerica = secuenciaActual.substring(secuenciaActual.length() - NUMERIC_LENGTH);

        try {
            int number = Integer.parseInt(parteNumerica);
            number++;

            if (number > 99) {
                parteAlfabetica = incrementAlphabetical(parteAlfabetica);
                number = 0;
            }

            return parteAlfabetica + String.format("%02d", number);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error in ID generation");
        }
    }

    private static String incrementAlphabetical(String prefigure) {

        char[] letters = prefigure.toCharArray();

        for (int i = letters.length - 1; i >= 0; i--) {
            if (letters[i] == 'Z') {
                letters[i] = 'A';
            } else {
                letters[i]++;
                break;
            }
        }

        return new String(letters);
    }
}