package by.javatr.orlov.dao.impl;

import by.javatr.orlov.bean.Book;
import by.javatr.orlov.Parser;
import by.javatr.orlov.dao.BookDAO;
import by.javatr.orlov.dao.exception.DAOException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileBookDAO implements BookDAO, FilePath {

//    @Override
//    public ArrayList<Book> deserialize () throws DAOException{
//        try (FileInputStream fileInputStream = new FileInputStream(BOOK_FILE_PATH);
//             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//            return (ArrayList<Book>) objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new DAOException(e);
//        }
//
//    }

//    @Override
//    public void serialize (ArrayList<Book> array) throws DAOException{
//        try (FileOutputStream outputStream = new FileOutputStream(BOOK_FILE_PATH);
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//            objectOutputStream.writeObject(array);
//        } catch (IOException e) {
//            throw new DAOException(e);
//        }
//    }

    @Override
    public void saveBooks (ArrayList<Book> books) throws DAOException{
        File file = new File(BOOK_FILE_PATH_TXT);
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Book book :
                    books) {
                stringBuilder.append("|");
                stringBuilder.append(book.getISBN());
                stringBuilder.append("|");
                stringBuilder.append(book.getTitle());
                stringBuilder.append("|");
                stringBuilder.append(book.getSubject());
                stringBuilder.append("|");
                stringBuilder.append(book.getAuthor());
                stringBuilder.append("|");
                stringBuilder.append(book.isIssued());
                stringBuilder.append("|\n");
            }
            writer.write(String.valueOf(stringBuilder));

        } catch (IOException e) {
            throw new DAOException(e);
        }
    }


    @Override
    public ArrayList<Book> loadBooks () throws DAOException{
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(BOOK_FILE_PATH_TXT), StandardCharsets.UTF_8))) {
            ArrayList<Book> books = new ArrayList<>();
            String line;
            boolean issued = false;
            while ((line = reader.readLine()) != null) {
                issued = Parser.parseStr(line, 4).equals("true");
                books.add(new Book(Parser.parseStr(line,0),
                        Parser.parseStr(line,1),
                        Parser.parseStr(line,2),
                        Parser.parseStr(line,3),
                        issued));
            }
            return books;
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }
}
