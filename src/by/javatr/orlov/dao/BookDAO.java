package by.javatr.orlov.dao;

import by.javatr.orlov.bean.Book;
import by.javatr.orlov.dao.exception.DAOException;

import java.util.ArrayList;

public interface BookDAO {
    ArrayList<Book> loadBooks () throws DAOException;

    void saveBooks (ArrayList<Book> array) throws DAOException;


}
