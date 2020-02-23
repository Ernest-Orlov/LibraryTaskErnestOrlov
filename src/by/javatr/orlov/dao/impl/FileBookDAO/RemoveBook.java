package by.javatr.orlov.dao.impl.FileBookDAO;

import by.javatr.orlov.dao.exception.DAOException;

import java.io.BufferedReader;
import java.io.IOException;

public class RemoveBook implements BookReader {
    @Override
    public String read (BufferedReader bufferedReader, String param) throws IOException, DAOException{
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            line += "\n";
            if (!line.equals(param)) {
                stringBuilder.append(line);
            }
        }
        return String.valueOf(stringBuilder);
    }
}
