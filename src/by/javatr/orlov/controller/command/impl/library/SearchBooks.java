package by.javatr.orlov.controller.command.impl.library;

import by.javatr.orlov.Parser;
import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class SearchBooks implements Command {
    @Override
    public String execute (String request){
        String searchStr = Parser.parseStr(request, 1);
        String response;

        LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();

        try {
            response = libraryService.searchBooksByFields(searchStr);
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
