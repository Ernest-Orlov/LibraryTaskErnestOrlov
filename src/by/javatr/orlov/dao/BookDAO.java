package by.javatr.orlov.dao;

import by.javatr.orlov.bean.Book;
import by.javatr.orlov.dao.exception.DAOException;

import java.util.ArrayList;

public interface BookDAO {
    ArrayList<Book> loadAllBooks () throws DAOException;

    void addBook (Book book) throws DAOException;

    void removeBook (Book book) throws DAOException;

    Book getBook (String iSBN) throws DAOException;

    ArrayList<Book> searchBooks (String searchStr) throws DAOException;

    boolean isInLibrary (String iSBN) throws DAOException;

    void setISBN (String iSBN, String newISBN) throws DAOException;

    void setTitle (String iSBN, String newValue) throws DAOException;

    void setSubject (String iSBN, String newValue) throws DAOException;

    void setAuthor (String iSBN, String newValue) throws DAOException;

    void setIssued (String iSBN, String newValue) throws DAOException;

    void saveAllBooks (ArrayList<Book> array) throws DAOException;


}
