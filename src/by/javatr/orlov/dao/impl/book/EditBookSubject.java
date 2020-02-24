package by.javatr.orlov.dao.impl.book;

import by.javatr.orlov.bean.Book;

public class EditBookSubject implements BookEditor {
    @Override
    public void editBook (Book book, String newValue){
        book.setSubject(newValue);
    }
}
