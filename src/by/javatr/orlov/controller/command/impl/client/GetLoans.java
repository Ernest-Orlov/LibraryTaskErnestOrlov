package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class GetLoans implements Command {
    @Override
    public String execute (String request){
        try {
            return ServiceFactory.getInstance().getClientService().getLoans();
        } catch (ServiceException e) {
            return e.getMessage();
        }
    }
}
