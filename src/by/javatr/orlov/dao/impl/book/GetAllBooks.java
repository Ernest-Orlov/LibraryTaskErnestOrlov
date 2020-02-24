package by.javatr.orlov.dao.impl.book;

import by.javatr.orlov.dao.exception.DAOException;

import java.io.BufferedReader;
import java.io.IOException;

public class GetAllBooks implements BookReader {
    @Override
    public String read (BufferedReader bufferedReader, String param) throws IOException, DAOException{
        String bookString;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('*');
        while ((bookString = bufferedReader.readLine()) != null) {
            stringBuilder.append(bookString).append('*');
        }
        return String.valueOf(stringBuilder);
    }
}
