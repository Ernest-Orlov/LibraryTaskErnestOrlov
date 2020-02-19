package by.javatr.orlov.dao.impl.FileBookDAO;

import by.javatr.orlov.Parser;
import by.javatr.orlov.bean.Book;
import by.javatr.orlov.dao.BookDAO;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.impl.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileBookDAO implements BookDAO, FilePath {

    @Override
    public void addBook (Book book) throws DAOException{
        try (BufferedWriter bufferWriter = new BufferedWriter(
                new FileWriter(BOOK_FILE_PATH_TXT, true))) {
            bufferWriter.write(parseBookToString(book));
        } catch (IOException e) {
            throw new DAOException("DB error", e);
        }
    }

    @Override
    public void removeBook (Book book) throws DAOException{
        String targetLine = parseBookToString(book);
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(
                        new File(BOOK_FILE_PATH_TXT)))) {
            while ((line = reader.readLine()) != null) {
                line += "\n";
                if (!line.equals(targetLine)) {
                    stringBuilder.append(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error in file reading: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        new File(BOOK_FILE_PATH_TXT)))) {
            writer.write(String.valueOf(stringBuilder));
        } catch (IOException e) {
            throw new DAOException("Error in file writing: " + e.getMessage());
        }

    }

    @Override
    public Book getBook (String iSBN) throws DAOException{
        String line = null;
        try (BufferedReader reader = new BufferedReader(
                new FileReader(
                        new File(BOOK_FILE_PATH_TXT)))) {
            while ((line = reader.readLine()) != null) {
                if (Parser.parseStr(line, 0).equals(iSBN)) {
                    return parseBookFromString(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error in file reading: " + e.getMessage());
        }
        throw new DAOException("No such book");
    }

    @Override
    public ArrayList<Book> searchBooks (String searchStr) throws DAOException{
        ArrayList<Book> books = loadBooks();
        ArrayList<Book> matchedBooks = new ArrayList<>();
        for (Book b :
                books) {
            if (b.getISBN().equalsIgnoreCase(searchStr) ||
                    b.getTitle().equalsIgnoreCase(searchStr) ||
                    b.getSubject().equalsIgnoreCase(searchStr) ||
                    b.getAuthor().equalsIgnoreCase(searchStr)) {
                matchedBooks.add(b);
            }
        }
        return matchedBooks;
    }

    @Override
    public boolean isInLibrary (String iSBN) throws DAOException{
        ArrayList<Book> books = loadBooks();
        for (Book book :
                books) {
            if (book.getISBN().equals(iSBN))
                return true;
        }
        return false;
    }

    @Override
    public void setISBN (String iSBN, String newValue) throws DAOException{
        edit(iSBN, newValue, new EditBookISBN());
    }

    @Override
    public void setTitle (String iSBN, String newValue) throws DAOException{
        edit(iSBN, newValue, new EditBookTitle());
    }

    @Override
    public void setSubject (String iSBN, String newValue) throws DAOException{
        edit(iSBN, newValue, new EditBookSubject());
    }

    @Override
    public void setAuthor (String iSBN, String newValue) throws DAOException{
        edit(iSBN, newValue, new EditBookAuthor());
    }

    @Override
    public void setIssued (String iSBN, String newValue) throws DAOException{
        edit(iSBN, newValue, new EditBookIssued());
    }

    private void edit (String iSBN, String newValue, EditBook editBook) throws DAOException{
        ArrayList<Book> books = loadBooks();
        for (Book book :
                books) {
            if (book.getISBN().equals(iSBN)) {
                editBook.editBook(book, newValue);
                break;
            }
        }
        saveBooks(books);

    }


    @Override
    public void saveBooks (ArrayList<Book> books) throws DAOException{
        File file = new File(BOOK_FILE_PATH_TXT);
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Book book :
                    books) {
                stringBuilder.append(parseBookToString(book));
            }
            writer.write(String.valueOf(stringBuilder));
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }

    private String parseBookToString (Book book){
        return "|" +
                book.getISBN() +
                "|" +
                book.getTitle() +
                "|" +
                book.getSubject() +
                "|" +
                book.getAuthor() +
                "|" +
                book.isIssued() +
                "|\n";
    }

    private Book parseBookFromString (String bookString){
        boolean issued = false;
        issued = Parser.parseStr(bookString, 4).equals("true");
        return new Book(Parser.parseStr(bookString, 0),
                Parser.parseStr(bookString, 1),
                Parser.parseStr(bookString, 2),
                Parser.parseStr(bookString, 3),
                issued);
    }

    @Override
    public ArrayList<Book> loadBooks () throws DAOException{
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(BOOK_FILE_PATH_TXT), StandardCharsets.UTF_8))) {
            ArrayList<Book> books = new ArrayList<>();
            String bookString;
            while ((bookString = reader.readLine()) != null) {
                books.add(parseBookFromString(bookString));
            }
            return books;
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }
}
