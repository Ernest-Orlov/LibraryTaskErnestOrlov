package by.javatr.orlov.dao.impl.FileBookDAO;

import by.javatr.orlov.bean.Book;

public class EditBookAuthor implements BookEditor {
    @Override
    public void editBook (Book book, String newValue){
        book.setAuthor(newValue);
    }
}
