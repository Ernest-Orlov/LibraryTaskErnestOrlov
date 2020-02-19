package by.javatr.orlov.controller.command.impl.library.admin;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.controller.command.impl.AdminCheck;
import by.javatr.orlov.Parser;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class EditSubject implements Command, AdminCheck {
    @Override
    public String execute (String request){
        String iSBN = Parser.parseStr(request, 1);
        String newSubject = Parser.parseStr(request, 2);
        String response;

        LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();
        try {
            if (notAdmin()) {
                return "Wrong command";
            }
            libraryService.setSubject(iSBN, newSubject);
            response = "Subject changed";
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
