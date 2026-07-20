package com.san_andres.backend.shared.util;

import com.san_andres.backend.shared.exception.BadRequestException;

public final class SequenceGenerator {

    private static final String DEFAULT_SEQUENCE = "00000000";
    private static final int NUMERIC_LENGTH = 2;
    private static final int MAX_NUMBER = 99;

    private SequenceGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateCode(String currentSequence) {

        String sequence = normalizeSequence(currentSequence);
        String alphabeticPart = getAlphabeticPart(sequence);
        int numericPart = getNumericPart(sequence);

        numericPart++;

        if (numericPart > MAX_NUMBER) {
            alphabeticPart = incrementAlphabetical(alphabeticPart);
            numericPart = 0;
        }

        return buildSequence(alphabeticPart, numericPart);
    }

    private static String normalizeSequence(String sequence) {
        return sequence == null ? DEFAULT_SEQUENCE : sequence;
    }

    private static String getAlphabeticPart(String sequence) {
        return sequence.substring(0, sequence.length() - NUMERIC_LENGTH);
    }

    private static int getNumericPart(String sequence) {

        String numeric = sequence.substring(sequence.length() - NUMERIC_LENGTH);

        try {
            return Integer.parseInt(numeric);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Error in ID generation");
        }
    }

    private static String buildSequence(String alphabeticPart, int numericPart) {
        return alphabeticPart + String.format("%02d", numericPart);
    }

    private static String incrementAlphabetical(String alphabeticPart) {

        char[] letters = alphabeticPart.toCharArray();

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