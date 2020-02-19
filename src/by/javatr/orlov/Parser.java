package by.javatr.orlov;

import by.javatr.orlov.controller.DelimiterSymbol;

import java.util.ArrayList;

public class Parser implements DelimiterSymbol {
    public static String parseStr (String request, int index){

        ArrayList<String> strings = new ArrayList<>();
        int position = 0;

        while (position + 1 < request.length()) {
            request = request.substring(position + 1);
            position = request.indexOf(DELIMITER_SYMBOL);
            strings.add(request.substring(0, position));
        }
        String[] split = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            split[i] = strings.get(i);
        }
        return split[index];
    }
}
