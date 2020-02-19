package by.javatr.orlov.controller.command.impl;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class ResetFiles implements Command {
    @Override
    public String execute (String request){
        try {
            ServiceFactory.getInstance().getLibraryService().saveBooks();
            ServiceFactory.getInstance().getClientService().saveUsers();
        }catch (ServiceException e){
            return e.getMessage();
        }
        return "Done";
    }
}
