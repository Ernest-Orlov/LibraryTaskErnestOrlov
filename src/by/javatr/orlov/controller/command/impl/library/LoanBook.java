package by.javatr.orlov.controller.command.impl.library;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.Parser;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class LoanBook implements Command {

    @Override
    public String execute (String request){
        String iSBN = Parser.parseStr(request, 1);
        String response;

        LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();
        ClientService clientService = ServiceFactory.getInstance().getClientService();

        try {
            libraryService.loanBook(iSBN, clientService.getCurrentUser());
            response = "Book is loaned";
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
