package by.javatr.orlov.dao.impl.book;

import by.javatr.orlov.dao.exception.DAOException;

import java.io.BufferedReader;
import java.io.IOException;


public interface BookReader {
    public String read (BufferedReader bufferedReader, String param) throws IOException, DAOException;

}
