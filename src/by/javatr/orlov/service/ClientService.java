package by.javatr.orlov.service;

import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.service.exception.ServiceException;


public interface ClientService {

    User getCurrentUser () throws ServiceException;

    boolean logIn (String login, String password) throws ServiceException;

    void logOut ();

    void setName (String name) throws ServiceException;

    void setLogin (String login) throws ServiceException;

    void setPassword (String password) throws ServiceException;

    void registerUser (String name, String login, String password) throws ServiceException;

    void registerAdmin (String name, String login, String password) throws ServiceException;

    void addLoan(Loan loan) throws ServiceException;

    void removeLoan (Loan loan) throws ServiceException;

    String getAllLoans () throws ServiceException;

    String getAllUserLoans (String login) throws ServiceException;

    String getLoans () throws ServiceException;

    String getAllUsers () throws ServiceException;

}
