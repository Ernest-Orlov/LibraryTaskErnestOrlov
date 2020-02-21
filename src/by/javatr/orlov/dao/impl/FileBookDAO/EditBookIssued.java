package by.javatr.orlov.dao.impl.FileBookDAO;

import by.javatr.orlov.bean.Book;

public class EditBookIssued implements BookEditor {
    @Override
    public void editBook (Book book, String newValue){
        book.setIssued(Boolean.parseBoolean(newValue));
    }
}
