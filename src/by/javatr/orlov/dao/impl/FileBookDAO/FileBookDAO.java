package by.javatr.orlov.dao.impl.FileBookDAO;

import by.javatr.orlov.Parser;
import by.javatr.orlov.bean.Book;
import by.javatr.orlov.dao.BookDAO;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.impl.FilePath;

import java.io.*;
import java.util.ArrayList;

public class FileBookDAO implements BookDAO, FilePath {

    private void writeInFile (String string) throws DAOException{
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(
                        new File(BOOK_FILE_PATH_TXT)))) {
            bufferedWriter.write(string);
        } catch (IOException e) {
            throw new DAOException("Error in file writing", e);
        }
    }

    public String readFromFile (BookReader bookReader, String param) throws DAOException{
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(
                        new File(BOOK_FILE_PATH_TXT)))) {
            return bookReader.read(bufferedReader, param);
        } catch (IOException e) {
            throw new DAOException("Error in reading from file:", e);
        }
    }

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
        writeInFile(readFromFile(new RemoveBook(), parseBookToString(book)));
    }

    @Override
    public Book getBook (String iSBN) throws DAOException{
        return parseBookFromString(readFromFile(new GetBook(), iSBN));
    }

    @Override
    public ArrayList<Book> loadBooks () throws DAOException{
        String[] mas = Parser.parseStr(readFromFile(new GetAllBooks(),null),'*');
        ArrayList<Book> books = new ArrayList<>();
        for (String str :
                mas) {
            books.add(parseBookFromString(str));
        }
        return books;
    }


    @Override
    public void saveBooks (ArrayList<Book> books) throws DAOException{
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book :
                books) {
            stringBuilder.append(parseBookToString(book));
        }
        writeInFile(String.valueOf(stringBuilder));
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

    private void edit (String iSBN, String newValue, BookEditor bookEditor) throws DAOException{
        ArrayList<Book> books = loadBooks();
        for (Book book :
                books) {
            if (book.getISBN().equals(iSBN)) {
                bookEditor.editBook(book, newValue);
                break;
            }
        }
        saveBooks(books);

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

}
