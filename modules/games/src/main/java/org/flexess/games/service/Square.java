package org.flexess.games.service;

import java.util.regex.Pattern;

public class Square {

    static final String LINE_NAMES = "abcdefgh";

    /** Tool class, construction forbidden */
    private Square() {
    }

    static int toNumber(String name) {

        int result;

        Pattern pattern = Pattern.compile("[a-h][1-8]");
        if (pattern.matcher(name).matches()) {
            int row  = Integer.parseInt(name.substring(1));
            char  line = name.charAt(0);
            result = LINE_NAMES.indexOf(line) + (8-row) * 8;
        } else {
            throw new IllegalArgumentException(name + " is not a valid square name.");
        }

        return result;
    }
}
