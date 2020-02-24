package by.javatr.orlov.dao.impl;

import by.javatr.orlov.Parser;
import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.UserDAO;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.impl.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileUserDAO implements UserDAO, FilePath {

    private String parseUserToString (User user){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        stringBuilder.append(user.getName());
        stringBuilder.append("|");
        stringBuilder.append(user.getLogin());
        stringBuilder.append("|");
        stringBuilder.append(user.getPassword());
        stringBuilder.append("|");
        stringBuilder.append(user.isAdmin());
        stringBuilder.append("|");
        stringBuilder.append(user.getBorrowedBooks().size());
        stringBuilder.append("|");
        for (Loan loan :
                user.getBorrowedBooks()) {
            stringBuilder.append(loan.getBookISBN());
            stringBuilder.append("|");
            stringBuilder.append(loan.getIssuedDate());
            stringBuilder.append("|");
        }
        stringBuilder.append("\n");
        return String.valueOf(stringBuilder);
    }


    private User parseUserFromString (String line) throws DAOException{
        User user = new User(Parser.parseStr(line, 0),
                Parser.parseStr(line, 1),
                Parser.parseStr(line, 2),
                Parser.parseStr(line, 3).equals("true"));
        for (int i = 5; i < Integer.parseInt(Parser.parseStr(line, 4)) * 2 + 5; i += 2) {
            SimpleDateFormat format1 = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = format1.parse((Parser.parseStr(line, i + 1)));
            } catch (ParseException e) {
                throw new DAOException("Eror in Date format", e);
            }
            Loan loan = new Loan(Parser.parseStr(line, i), date);
            user.addLoan(loan);
        }
        return user;
    }

    @Override
    public void addUser (User user) throws DAOException{
        ArrayList<User> users = loadAllUsers();
        users.add(user);
        saveAllUsers(users);
    }

    @Override
    public void manageLoans (User user, Loan loan, boolean flag) throws DAOException{
        ArrayList<User> users = loadAllUsers();
        for (User user1 :
                users) {
            if (user1.equals(user)) {
                if (flag) {
                    user1.addLoan(loan);
                }
                else {
                    user1.removeLoan(loan);
                }
            }
        }
        saveAllUsers(users);
    }

    @Override
    public void saveAllUsers (ArrayList<User> users) throws DAOException{
        File file = new File(USER_FILE_PATH_TXT);
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (User user :
                    users) {
                stringBuilder.append(parseUserToString(user));
            }
            writer.write(String.valueOf(stringBuilder));

        } catch (IOException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public ArrayList<User> loadAllUsers () throws DAOException{

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(USER_FILE_PATH_TXT), StandardCharsets.UTF_8))) {

            ArrayList<User> users = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                users.add(parseUserFromString(line));
            }
            return users;
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }

}
