package by.javatr.orlov.dao.impl;

import by.javatr.orlov.Parser;
import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.UserDAO;
import by.javatr.orlov.dao.exception.DAOException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileUserDAO implements UserDAO, FilePath {

//    @Override
//    public ArrayList<User> deserialize () throws DAOException{
//        try (FileInputStream fileInputStream = new FileInputStream(USER_FILE_PATH);
//             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//            return (ArrayList<User>) objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new DAOException(e);
//        }
//    }

//    @Override
//    public void serialize (ArrayList<User> array) throws DAOException{
//        try (FileOutputStream outputStream = new FileOutputStream(USER_FILE_PATH);
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//            objectOutputStream.writeObject(array);
//        } catch (IOException e) {
//            throw new DAOException(e);
//        }
//    }

    @Override
    public void serialize (ArrayList<User> users) throws DAOException{
        File file = new File(USER_FILE_PATH_TXT);
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (User user :
                    users) {
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
            }
            writer.write(String.valueOf(stringBuilder));

        } catch (IOException e) {
            throw new DAOException(e);
        }
    }


    @Override
    public ArrayList<User> deserialize () throws DAOException{

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(USER_FILE_PATH_TXT), StandardCharsets.UTF_8))) {

            ArrayList<User> users = new ArrayList<>();
            String line = null;
            boolean isAdmin = false;
            while ((line = reader.readLine()) != null) {
                isAdmin = Parser.parseStr(line, 3).equals("true");
                User user = new User(Parser.parseStr(line, 0),
                        Parser.parseStr(line, 1),
                        Parser.parseStr(line, 2),
                        isAdmin);
                for (int i = 5; i < Integer.parseInt(Parser.parseStr(line, 4)) * 2 + 5; i += 2) {
                    SimpleDateFormat format1 = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    Date date = null;
                    date = format1.parse((Parser.parseStr(line, i + 1)));
                    Loan loan = new Loan(Parser.parseStr(line, i), date);
                    user.addLoan(loan);
                }
                users.add(user);
            }
            return users;
        } catch (IOException | ParseException e) {
            throw new DAOException(e);
        }
    }

}
