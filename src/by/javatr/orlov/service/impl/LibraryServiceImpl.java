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

    public LibraryServiceImpl (){
    }

    private void addBookInLibrary (Book b) throws ServiceException{
        try {
            DAOFactory.getInstance().getBookDAO().addBook(b);
        } catch (DAOException e) {
            throw new ServiceException("Adding book error", e);
        }
    }

    @Override
    public void removeBookFromLibrary (String iSBN) throws ServiceException{
        Checker.validateStringField(iSBN, "ISBN");
        try {
            Book book = DAOFactory.getInstance().getBookDAO().getBook(iSBN);
            if (book.isIssued())
                throw new ServiceException("Book is loaned");
            DAOFactory.getInstance().getBookDAO().removeBook(book);
        } catch (DAOException e) {
            throw new ServiceException("Error in removing book", e);
        }
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
            throw new ServiceException("library Error", e);
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
            throw new ServiceException("library Error", e);
        }
    }

    @Override
    public void setTitle (String iSBN, String newTitle) throws ServiceException{
        Checker.validateStringField(newTitle, "New title");
        try {
            DAOFactory.getInstance().getBookDAO().setTitle(iSBN, newTitle);
        } catch (DAOException e) {
            throw new ServiceException("library Error", e);
        }
    }

    @Override
    public void setSubject (String iSBN, String newSubject) throws ServiceException{
        Checker.validateStringField(newSubject, "New subject");
        try {
            DAOFactory.getInstance().getBookDAO().setSubject(iSBN, newSubject);
        } catch (DAOException e) {
            throw new ServiceException("library Error", e);
        }
    }

    @Override
    public void setAuthor (String iSBN, String newAuthor) throws ServiceException{
        Checker.validateStringField(newAuthor, "New author");
        try {
            DAOFactory.getInstance().getBookDAO().setAuthor(iSBN, newAuthor);
        } catch (DAOException e) {
            throw new ServiceException("library Error", e);
        }
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
    public void loanBook (String iSBN, User user) throws ServiceException{
        if (user == null)
            throw new ServiceException("No logged user");
        Checker.validateStringField(iSBN, "ISBN");

        Book book = null;
        try {
            book = DAOFactory.getInstance().getBookDAO().getBook(iSBN);
            if (book.isIssued())
                throw new ServiceException("Book is already issued");

            book.setIssued(true);
            DAOFactory.getInstance().getBookDAO().setIssued(iSBN, "" + true);
            Loan ln = new Loan(iSBN, new Date());
            user.addLoan(ln);
            ServiceFactory.getInstance().getClientService().saveUsers();
        } catch (DAOException e) {
            throw new ServiceException("Library Error", e);
        }
    }


    @Override
    public void returnBook (String iSBN, User user) throws ServiceException, CloneNotSupportedException{
        if (user == null)
            throw new ServiceException("No logged user");
        Checker.validateStringField(iSBN, "ISBN");
        Book book = null;
        try {
            book = DAOFactory.getInstance().getBookDAO().getBook(iSBN);

            ArrayList<Loan> list = user.getBorrowedBooks();
            for (Loan l :
                    list) {
                if (l.getBookISBN().equals(book.getISBN())) {
                    user.removeLoan(l);
                    DAOFactory.getInstance().getBookDAO().setIssued(iSBN, "" + false);
                    ServiceFactory.getInstance().getClientService().saveUsers();
                    return;
                }
            }
        } catch (DAOException e) {
            throw new ServiceException("Library Error", e);
        }
        throw new ServiceException("This book is not loaned by you");
    }

    @Override
    public String getAllBooks () throws ServiceException{
        try {
            return getBookTable(DAOFactory.getInstance().getBookDAO().loadBooks());
        } catch (DAOException e) {
            throw new ServiceException("Library Error", e);
        }
    }

    private String getBookTable (ArrayList<Book> books){
        { //todo refactor
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

}
