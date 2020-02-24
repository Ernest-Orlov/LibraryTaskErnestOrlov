package by.javatr.orlov.service;

import by.javatr.orlov.bean.User;
import by.javatr.orlov.service.exception.ServiceException;


public interface LibraryService {

    void removeBookFromLibrary (String iSBN) throws ServiceException;

    String searchBooksByFields (String searchStr) throws ServiceException;

    void setISBN (String iSBN, String newISBN) throws ServiceException;

    void setTitle (String iSBN, String newTitle) throws ServiceException;

    void setSubject (String iSBN, String newSubject) throws ServiceException;

    void setAuthor (String iSBN, String newAuthor) throws ServiceException;

    void addNewBook (String iSBN, String title, String subject, String author) throws ServiceException;

    void loanBook (String iSBN) throws ServiceException;

    void returnBook (String iSBN, User user) throws ServiceException, CloneNotSupportedException;

    String getAllBooks () throws ServiceException;

}
