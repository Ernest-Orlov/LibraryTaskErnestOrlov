package by.javatr.orlov;

import by.javatr.orlov.controller.DelimiterSymbol;

import java.util.ArrayList;

// зачем данный класс помещен в пакет третьего уровня
// и если ты уже используешь в пакетах свою фамилию, а не имя проекта, то разберись, в каких проектах это применяют
public class Parser implements DelimiterSymbol {
    public static String parseStr (String request, int index){// называй методы нормально
        return parse(request,DELIMITER_SYMBOL)[index];
    }
    
    public static String[] parseStr (String request, char delimiter){
        return parse(request,delimiter);
    }

    private static String[] parse (String request, char delimiter){
        ArrayList<String> strings = new ArrayList<>();// давно уже ссылки на коллекции делают типа интерфейса
        int position = 0;

        while (position + 1 < request.length()) {
            request = request.substring(position + 1);
            position = request.indexOf(delimiter);
            strings.add(request.substring(0, position));
        }
        String[] split = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            split[i] = strings.get(i);
        }
        return split;
    }
}
