package by.javatr.orlov.dao.factory;

import by.javatr.orlov.dao.BookDAO;
import by.javatr.orlov.dao.UserDAO;
import by.javatr.orlov.dao.impl.FileBookDAO;
import by.javatr.orlov.dao.impl.FileUserDAO;

public final class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();

    private final BookDAO bookDAO = new FileBookDAO();
    private final UserDAO userDAO = new FileUserDAO();

    private DAOFactory (){
    }

    public static DAOFactory getInstance (){
        return instance;
    }

    public BookDAO getBookDAO (){
        return bookDAO;
    }

    public UserDAO getUserDAO (){
        return userDAO;
    }
}
