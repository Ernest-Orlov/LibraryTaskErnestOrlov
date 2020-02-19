package by.javatr.orlov.controller;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.controller.command.CommandName;
import by.javatr.orlov.controller.command.impl.client.*;
import by.javatr.orlov.controller.command.impl.ResetFiles;
import by.javatr.orlov.controller.command.impl.library.GetAllBooks;
import by.javatr.orlov.controller.command.impl.library.LoanBook;
import by.javatr.orlov.controller.command.impl.library.ReturnBook;
import by.javatr.orlov.controller.command.impl.library.SearchBooks;
import by.javatr.orlov.controller.command.impl.library.admin.*;

import java.util.HashMap;
import java.util.Map;

final class CommandProvider {
    private final Map<CommandName, Command> repository = new HashMap<>();

    CommandProvider (){
        repository.put(CommandName.LOG_IN, new LogIn());
        repository.put(CommandName.LOG_OUT, new LogOut());
        repository.put(CommandName.REGISTER_USER, new RegisterUser());
        repository.put(CommandName.REGISTER_ADMIN, new RegisterAdmin());
        repository.put(CommandName.EDIT_NAME, new EditName());
        repository.put(CommandName.EDIT_LOGIN, new EditLogin());
        repository.put(CommandName.EDIT_PASSWORD, new EditPassword());
        repository.put(CommandName.GET_LOANS, new GetLoans());
        repository.put(CommandName.GET_ALL_LOANS, new GetAllLoans());
        repository.put(CommandName.GET_ALL_USERS, new GetAllUsers());
        repository.put(CommandName.GET_USER_LOANS, new GetUserLoans());


        repository.put(CommandName.ADD_BOOK, new AddBook());
        repository.put(CommandName.REMOVE_BOOK, new RemoveBook());
        repository.put(CommandName.EDIT_AUTHOR, new EditAuthor());
        repository.put(CommandName.EDIT_ISBN, new EditISBN());
        repository.put(CommandName.EDIT_SUBJECT, new EditSubject());
        repository.put(CommandName.EDIT_TITLE, new EditTitle());

        repository.put(CommandName.LOAN_BOOK, new LoanBook());
        repository.put(CommandName.RETURN_BOOK, new ReturnBook());
        repository.put(CommandName.GET_ALL_BOOKS, new GetAllBooks());
        repository.put(CommandName.SEARCH_BOOKS, new SearchBooks());

        repository.put(CommandName.WRONG_REQUEST, new WrongRequest());
        repository.put(CommandName.RESET_FILES, new ResetFiles());
    }

    Command getCommand (String name){
        CommandName commandName = null;
        Command command = null;

        try {
            commandName = CommandName.valueOf(name.toUpperCase());
            command = repository.get(commandName);
        } catch (IllegalArgumentException | NullPointerException e) {
            //write log
            command = repository.get(CommandName.WRONG_REQUEST);
        }
        return command;
    }
}
