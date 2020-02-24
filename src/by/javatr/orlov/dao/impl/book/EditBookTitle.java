package by.javatr.orlov.dao.impl.book;

import by.javatr.orlov.bean.Book;

public class EditBookTitle implements BookEditor {
    @Override
    public void editBook (Book book, String newValue){
        book.setTitle(newValue);
    }
}
