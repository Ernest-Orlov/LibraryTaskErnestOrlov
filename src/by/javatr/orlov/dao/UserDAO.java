package by.javatr.orlov.dao;


import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.exception.DAOException;

import java.util.ArrayList;

public interface UserDAO {


    void addUser (User user) throws DAOException;

    ArrayList<User> loadAllUsers () throws DAOException;

    void manageLoans (User user, Loan loan, boolean flag) throws DAOException;

    void saveAllUsers (ArrayList<User> array) throws DAOException;
}
