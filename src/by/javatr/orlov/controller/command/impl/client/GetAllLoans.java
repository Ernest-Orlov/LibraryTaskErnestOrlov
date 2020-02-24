package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.controller.command.impl.AdminCheck;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class GetAllLoans implements Command, AdminCheck {
    @Override
    public String execute (String request){
        ClientService clientService = ServiceFactory.getInstance().getClientService();

        try {
            if (notAdmin()) {
                return "Wrong command";
            }
            return clientService.getAllLoans();
        } catch (ServiceException e) {
            return e.getMessage();// никогда так не делай
        }
    }
}
