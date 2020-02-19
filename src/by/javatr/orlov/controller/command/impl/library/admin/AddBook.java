package by.javatr.orlov.controller.command.impl.library.admin;

import by.javatr.orlov.Parser;
import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.controller.command.impl.AdminCheck;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class AddBook implements Command, AdminCheck {
    @Override
    public String execute (String request){
        //String iSBN, String title, String subject, String author
        String iSBN = Parser.parseStr(request, 1);
        String title = Parser.parseStr(request, 2);
        String subject = Parser.parseStr(request, 3);
        String author = Parser.parseStr(request, 4);
        String response;

        LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();

        try {
            if (notAdmin()) {
                return "Wrong command";
            }
            libraryService.addNewBook(iSBN, title, subject, author);
            response = "Book added";
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
