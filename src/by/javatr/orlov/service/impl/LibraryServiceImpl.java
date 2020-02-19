package by.javatr.orlov.service.impl;

import by.javatr.orlov.bean.Book;
import by.javatr.orlov.bean.Loan;
import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.exception.DAOException;
import by.javatr.orlov.dao.factory.DAOFactory;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.Date;

public class LibraryServiceImpl implements LibraryService {
    private ArrayList<Book> booksInLibrary;

    public LibraryServiceImpl (){
        booksInLibrary = new ArrayList<>();
    }

    private void addBookInLibrary (Book b) throws ServiceException{
        booksInLibrary.add(b);
        try {
            DAOFactory.getInstance().getBookDAO().addBook(b);
        } catch (DAOException e) {
            throw new ServiceException("Adding book error", e);
        }
    }

    private void removeBook (Book book) throws ServiceException{
        booksInLibrary.remove(book);
        try {
            DAOFactory.getInstance().getBookDAO().removeBook(book);
        } catch (DAOException e) {
            throw new ServiceException("Removing book error",e);
        }
    }

    private Book getBook (String iSBN) throws ServiceException{
        Checker.validateStringField(iSBN, "ISBN");
        for (Book book :
                booksInLibrary) {
            if (book.getISBN().equals(iSBN))
                return book;
        }
        throw new ServiceException("No such book in library");
    }

    @Override
    public String searchBooksByFields (String searchStr) throws ServiceException{
        Checker.validateStringField(searchStr, "Key word");
        ArrayList<Book> matchedBooks = null;
        try {
            matchedBooks = DAOFactory.getInstance().getBookDAO().searchBooks(searchStr);
        } catch (DAOException e) {
            throw new ServiceException("No such books in library");
        }
        if (!matchedBooks.isEmpty())
            return getBookTable(matchedBooks);
        return "No matches";
    }

    private boolean isInLibrary (String iSBN) throws ServiceException{
        try {
            return DAOFactory.getInstance().getBookDAO().isInLibrary(iSBN);
        } catch (DAOException e) {
            throw new ServiceException("library Error",e);
        }
    }

    @Override
    public void setISBN (String iSBN, String newISBN) throws ServiceException{
        Checker.validateStringField(newISBN, "New ISBN");
        if (isInLibrary(newISBN))
            throw new ServiceException("Book with this ISBN:" + newISBN + " already is in library");
        try {
            DAOFactory.getInstance().getBookDAO().setISBN(iSBN, newISBN);
        } catch (DAOException e) {
            throw new ServiceException("library Error",e);
        }
    }

    @Override
    public void setTitle (String iSBN, String newTitle) throws ServiceException{
        Checker.validateStringField(newTitle, "New title");
        getBook(iSBN).setTitle(newTitle);
        saveBooks();
    }

    @Override
    public void setSubject (String iSBN, String newSubject) throws ServiceException{
        Checker.validateStringField(newSubject, "New subject");
        getBook(iSBN).setSubject(newSubject);
        saveBooks();
    }

    @Override
    public void setAuthor (String iSBN, String newAuthor) throws ServiceException{
        Checker.validateStringField(newAuthor, "New author");
        getBook(iSBN).setAuthor(newAuthor);
        saveBooks();
    }

    @Override
    public void addNewBook (String iSBN, String title, String subject, String author) throws ServiceException{
        Checker.validateStringField(iSBN, "ISBN");
        Checker.validateStringField(title, "Title");
        Checker.validateStringField(subject, "Subject");
        Checker.validateStringField(author, "Author");

        if (isInLibrary(iSBN))
            throw new ServiceException("Book with this ISBN:" + iSBN + " already is in library");
        addBookInLibrary(new Book(iSBN, title, subject, author));
    }

    @Override
    public void removeBookFromLibrary (String iSBN) throws ServiceException{
        Checker.validateStringField(iSBN, "ISBN");
        try {
            Book book = DAOFactory.getInstance().getBookDAO().getBook(iSBN);
            if (book.isIssued())
                throw new ServiceException("Book is loaned");
            removeBook(book);
        } catch (DAOException e) {
            throw new ServiceException("Error in getting book",e);
        }
    }

    @Override
    public void loanBook (String iSBN, User user) throws ServiceException{
        if (user == null)
            throw new ServiceException("No logged user");
        Checker.validateStringField(iSBN, "ISBN");

        Book book = getBook(iSBN);
        if (book.isIssued())
            throw new ServiceException("Book is already issued");
        book.setIssued(true);
        Loan ln = new Loan(iSBN, new Date());
        user.addLoan(ln);
        saveBooks();
        ServiceFactory.getInstance().getClientService().saveUsers();
    }


    @Override
    public void returnBook (String iSBN, User user) throws ServiceException, CloneNotSupportedException{
        if (user == null)
            throw new ServiceException("No logged user");
        Checker.validateStringField(iSBN, "ISBN");

        Book book = getBook(iSBN);
        ArrayList<Loan> list = user.getBorrowedBooks();
        for (Loan l :
                list) {
            if (l.getBookISBN().equals(book.getISBN())) {
                user.removeLoan(l);
                book.setIssued(false);
                saveBooks();
                ServiceFactory.getInstance().getClientService().saveUsers();
                return;
            }
        }
        throw new ServiceException("This book is not loaned by you");
    }

    @Override
    public String getAllBooks (){
        return getBookTable(booksInLibrary);
    }

    private String getBookTable (ArrayList<Book> books){
        {
            if (books.isEmpty())
                return "Library is empty!";
            int isbnMax = 6;
            int authorMax = 8;
            int titleMax = 7;
            int subjectMax = 9;
            for (Book b :
                    books) {
                if (b.getISBN().length() > isbnMax)
                    isbnMax = b.getISBN().length();
                if (b.getAuthor().length() > authorMax)
                    authorMax = b.getAuthor().length();
                if (b.getTitle().length() > titleMax)
                    titleMax = b.getTitle().length();
                if (b.getSubject().length() > subjectMax)
                    subjectMax = b.getSubject().length();
            }

            String delimiter = "+";
            delimiter += delimiterFormer(isbnMax, '-') + "+";
            delimiter += delimiterFormer(authorMax, '-') + "+";
            delimiter += delimiterFormer(titleMax, '-') + "+";
            delimiter += delimiterFormer(subjectMax, '-') + "+";
            delimiter += delimiterFormer(7, '-') + "+\n";

            String leftAlignFormat = "| %-" + isbnMax + "s | %-" + authorMax + "s | %-" + titleMax + "s | %-" + subjectMax + "s | %-7s |\n";
            StringBuilder res = new StringBuilder();
            res.append(delimiter);
            res.append(String.format(leftAlignFormat, "ISBN", "Author", "Title", "Subject", "Issued"));
            res.append(delimiter);
            for (Book b :
                    books) {
                res.append(String.format(leftAlignFormat, b.getISBN(), b.getAuthor(), b.getTitle(), b.getSubject(), b.isIssued()));
                res.append(delimiter);
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
    public void loadBooks () throws ServiceException{
        try {
            booksInLibrary = DAOFactory.getInstance().getBookDAO().loadBooks();
        } catch (DAOException e) {
            throw new ServiceException("Library is unavailable", e);
        }
    }

    @Override
    public void saveBooks () throws ServiceException{
        try {
            DAOFactory.getInstance().getBookDAO().saveBooks(booksInLibrary);
        } catch (DAOException e) {
            throw new ServiceException("Error", e);
        }
    }

}
