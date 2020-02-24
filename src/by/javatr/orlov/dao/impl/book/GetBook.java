package by.javatr.orlov.dao.impl.book;

import by.javatr.orlov.Parser;
import by.javatr.orlov.dao.exception.DAOException;

import java.io.BufferedReader;
import java.io.IOException;

public class GetBook implements BookReader {
    @Override
    public String read (BufferedReader bufferedReader, String param) throws IOException, DAOException{
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (Parser.parseStr(line, 0).equals(param)) {
                return line;
            }
        }
        throw new DAOException("No such book");
    }
}
