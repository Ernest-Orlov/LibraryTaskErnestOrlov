package by.javatr.orlov.service.impl;

import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.factory.DAOFactory;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

import java.util.ArrayList;

public class ClientServiceImpl implements ClientService {

    private ArrayList<User> users;
    private User currentUser;

    public ClientServiceImpl (){
        users = new ArrayList<>();
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
        loadUsers();
        ServiceFactory.getInstance().getLibraryService().loadBooks();
        Checker.validateStringField(login, "Login");
        Checker.validateStringField(password, "Password");

        for (User user :
                users) {
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
        users.add(user);
        saveUsers();
    }

    @Override
    public void setName (String name) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(name, "Name");
        currentUser.setName(name);
        saveUsers();
    }

    @Override
    public void setLogin (String login) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(login, "Login");
        for (User user :
                users) {
            if (user.getLogin().equals(login))
                throw new ServiceException("User with this login already exists");
        }
        currentUser.setLogin(login);
        saveUsers();
    }

    @Override
    public void setPassword (String password) throws ServiceException{
        checkLoggedIn();
        Checker.validateStringField(password, "Password");
        currentUser.setPassword(Hasher.getInstance().getHashedPassword(password));
        saveUsers();
    }

    @Override
    public void registerUser (String name, String login, String password) throws ServiceException{
        loadUsers();
        Checker.validateStringField(name, "Name");
        Checker.validateStringField(login, "Login");
        Checker.validateStringField(password, "Password");

        for (User user :
                users) {
            if (user.getLogin().equals(login))
                throw new ServiceException("User with this login already exists");
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setLogin(login);
        newUser.setPassword(Hasher.getInstance().getHashedPassword(password));
        addUser(newUser);
    }

    @Override
    public void registerAdmin (String name, String login, String password) throws ServiceException{
        registerUser(name, login, password);
        users.get(users.size() - 1).setAdmin(true);
        saveUsers();
    }


    @Override
    public String getAllLoans () throws ServiceException{
        return getLoanTable(users);
    }

    @Override
    public String getAllUserLoans (String login) throws ServiceException{
        Checker.validateStringField(login, "Login");
        ArrayList<User> userLoans;
        for (User user :
                users) {
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

    @Override
    public String getAllUsers () throws ServiceException{
        loadUsers();
        if (users.isEmpty())
            return "No users";
        int nameMax = 6;
        int loginMax = 9;
        for (User user :
                users) {
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
                users) {
            res.append(String.format(leftAlignFormat, user.getName(), user.getLogin()));
            res.append(delimiter);
        }
        return String.valueOf(res);
    }

    //todo сделать нормальную таблицу
    private String getLoanTable (ArrayList<User> users) throws ServiceException{
        {
            boolean empty = true;
            for (User user:
                 users) {
                if(!user.getBorrowedBooks().isEmpty()){
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

    @Override
    public void saveUsers () throws ServiceException{
        try {
            DAOFactory.getInstance().getUserDAO().serialize(users);
        } catch (DAOException e) {
            throw new ServiceException("Error", e);
        }
    }

    @Override
    public void loadUsers () throws ServiceException{
        try {
            users = DAOFactory.getInstance().getUserDAO().deserialize();
        } catch (DAOException e) {
            throw new ServiceException("Library is unavailable", e);
        }
    }

}
