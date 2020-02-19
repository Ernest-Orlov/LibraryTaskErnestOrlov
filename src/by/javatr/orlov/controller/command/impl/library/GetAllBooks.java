package by.javatr.orlov.controller.command.impl.library;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.factory.ServiceFactory;

public class GetAllBooks implements Command {
    @Override
    public String execute (String request){
        LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();
        return libraryService.getAllBooks();
    }
}
