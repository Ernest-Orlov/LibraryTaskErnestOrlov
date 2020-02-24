package by.javatr.orlov.service.impl;

import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.factory.DAOFactory;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;

import java.util.ArrayList;

public class ClientServiceImpl implements ClientService {

    private User currentUser;// не нужны логике изменяемые несинхронизированные поля экземпляра класса
    

    public ClientServiceImpl (){
    }

    private void checkLoggedIn () throws ServiceException{
        if (currentUser == null)
            throw new ServiceException("No logged user");
    }

    @Override
    public User getCurrentUser () throws ServiceException{
        checkLoggedIn();
        return currentUser;
    }

    @Override
    public boolean logIn (String login, String password) throws ServiceException{
        Checker.validateStringField(login, "Login");
        Checker.validateStringField(password, "Password");// как предложение - продумать, как это реализовать с помощью паттерна Цепочка обязанностей
        for (User user :
                loadUsersFromDb()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(Hasher.getInstance().getHashedPassword(password))) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    @Override
    public void logOut (){
        currentUser = null;
    }

    private void addUser (User user) throws ServiceException{
        try {
            DAOFactory.getInstance().getUserDAO().addUser(user);
        } catch (DAOException e) {
            throw new ServiceException("Error in adding user", e);
        }
    }

    @Override
    public void setName (String name) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(name, "Name");
        ArrayList<User> userArrayList = loadUsersFromDb();
        for (User user :
                userArrayList) {
            if (user.equals(currentUser)) {
                user.setName(name);
                currentUser.setName(name);
            }
        }
        saveUsersToDB(userArrayList);
    }

    @Override
    public void setLogin (String login) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(login, "Login");
        ArrayList<User> userArrayList = loadUsersFromDb();
        for (User user :
                userArrayList) {
            if (user.getLogin().equals(login))
                throw new ServiceException("User with this login already exists");
        }
        for (User user :
                userArrayList) {
            if (user.equals(currentUser)) {
                user.setLogin(login);
                currentUser.setLogin(login);
            }
        }
        saveUsersToDB(userArrayList);
    }

    @Override
    public void setPassword (String password) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(password, "Password");
        ArrayList<User> userArrayList = loadUsersFromDb();
        for (User user :
                userArrayList) {
            if (user.equals(currentUser)) {
                user.setPassword(Hasher.getInstance().getHashedPassword(password));
                currentUser.setPassword(Hasher.getInstance().getHashedPassword(password));
            }
        }
        saveUsersToDB(userArrayList);
    }

    private User registration (String name, String login, String password) throws ServiceException{
        Checker.validateStringField(name, "Name");
        Checker.validateStringField(login, "Login");
        Checker.validateStringField(password, "Password");

        ArrayList<User> userArrayList = loadUsersFromDb();
        for (User user :
                userArrayList) {
            if (user.getLogin().equals(login))
                throw new ServiceException("User with this login already exists");
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setLogin(login);
        newUser.setPassword(Hasher.getInstance().getHashedPassword(password));
        return newUser;
    }

    @Override
    public void registerUser (String name, String login, String password) throws ServiceException{
        addUser(registration(name, login, password));
    }

    @Override
    public void registerAdmin (String name, String login, String password) throws ServiceException{
        User user = registration(name, login, password);
        user.setAdmin(true);
        addUser(user);
    }

    @Override
    public void addLoan (Loan loan) throws ServiceException{
        checkLoggedIn();
        try {
            DAOFactory.getInstance().getUserDAO().manageLoans(currentUser, loan, true);
        } catch (DAOException e) {
            throw new ServiceException("Error in adding loan ", e);
        }
    }

    @Override
    public void removeLoan (Loan loan) throws ServiceException{
        checkLoggedIn();
        try {
            DAOFactory.getInstance().getUserDAO().manageLoans(currentUser, loan, false);
        } catch (DAOException e) {
            throw new ServiceException("Error in removing loan ", e);
        }
    }

    @Override
    public String getAllLoans () throws ServiceException{
        try {
            return getLoanTable(DAOFactory.getInstance().getUserDAO().loadAllUsers());
        } catch (DAOException e) {
            throw new ServiceException("Error in getting loans", e);
        }
    }

    @Override
    public String getAllUserLoans (String login) throws ServiceException{
        Checker.validateStringField(login, "Login");
        ArrayList<User> userLoans;
        for (User user :
                loadUsersFromDb()) {
            if (user.getLogin().equals(login)) {
                userLoans = new ArrayList<>();
                userLoans.add(user);
                return getLoanTable(userLoans);
            }
        }
        throw new ServiceException("No such user");
    }

    @Override
    public String getLoans () throws ServiceException{
        return getAllUserLoans(currentUser.getLogin());
    }

    private ArrayList<User> loadUsersFromDb () throws ServiceException{
        ArrayList<User> userArrayList = null;
        try {
            userArrayList = DAOFactory.getInstance().getUserDAO().loadAllUsers();
        } catch (DAOException e) {
            throw new ServiceException("Error in loading users", e);
        }
        return userArrayList;
    }

    private void saveUsersToDB(ArrayList<User> userArrayList) throws ServiceException{
        try {
            DAOFactory.getInstance().getUserDAO().saveAllUsers(userArrayList);
        } catch (DAOException e) {
            throw new ServiceException("Error is saving users", e);
        }
    }

    @Override
    public String getAllUsers () throws ServiceException{
        ArrayList<User> userArrayList = loadUsersFromDb();
        if (userArrayList.isEmpty())
            return "No users";
        int nameMax = 6;
        int loginMax = 9;
        for (User user :
                userArrayList) {
            if (user.getName().length() > nameMax)
                nameMax = user.getName().length();
            if (user.getLogin().length() > loginMax)
                loginMax = user.getLogin().length();
        }
        String leftAlignFormat = "| %-" + nameMax + "s | %-" + loginMax + "s |\n";
        String delimiter = "+";
        delimiter += delimiterFormer(nameMax, '-') + "+";
        delimiter += delimiterFormer(loginMax, '-') + "+\n";
        StringBuilder res = new StringBuilder();
        res.append(delimiter);
        res.append(String.format(leftAlignFormat, "Name", "Username"));
        res.append(delimiter);
        for (User user :
                userArrayList) {
            res.append(String.format(leftAlignFormat, user.getName(), user.getLogin()));
            res.append(delimiter);
        }
        return String.valueOf(res);
    }

    private String getLoanTable (ArrayList<User> users) throws ServiceException{
        { //todo refactor
            boolean empty = true;
            for (User user :
                    users) {
                if (!user.getBorrowedBooks().isEmpty()) {
                    empty = false;
                }
            }

            if (empty) {
                return "No loans";
            }

            int isbnMax = 6;
            int userMax = 6;
            int loginMax = 6;

            for (User user :
                    users) {

                if (user.getName().length() > isbnMax) {
                    userMax = user.getName().length();
                }
                if (user.getLogin().length() > isbnMax) {
                    loginMax = user.getName().length();
                }

                ArrayList<Loan> loans = user.getBorrowedBooks();
                for (Loan loan :
                        loans) {
                    if (loan.getBookISBN().length() > isbnMax) {
                        isbnMax = loan.getBookISBN().length();
                    }
                }
            }
            String delimiter = "+";
            delimiter += delimiterFormer(isbnMax, '-') + "+";
            delimiter += delimiterFormer(userMax, '-') + "+";
            delimiter += delimiterFormer(loginMax, '-') + "+";
            delimiter += delimiterFormer(28, '-') + "+\n";

            String leftAlignFormat = "| %-" + isbnMax + "s | %-" + userMax + "s | %-" + loginMax + "s | %-28s |\n";
            StringBuilder res = new StringBuilder();
            res.append(delimiter);
            res.append(String.format(leftAlignFormat, "ISBN", "User", "Login", "Date"));
            res.append(delimiter);
            for (User user :
                    users) {
                ArrayList<Loan> loans = user.getBorrowedBooks();
                for (Loan loan :
                        loans) {
                    res.append(String.format(leftAlignFormat, loan.getBookISBN(), user.getName(), user.getLogin(), loan.getIssuedDate()));
                    res.append(delimiter);
                }
            }
            return String.valueOf(res);
        }
    }

    private String delimiterFormer (int length, char ch){
        length += 2;
        StringBuilder delimiter = new StringBuilder();
        for (int i = 0; i < length; i++) {
            delimiter.append(ch);
        }
        return String.valueOf(delimiter);
    }

}
