package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.Parser;
import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.controller.command.impl.AdminCheck;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class GetUserLoans implements Command, AdminCheck {
    @Override
    public String execute (String request){
        String login = Parser.parseStr(request, 1);
        String response;

        ClientService clientService = ServiceFactory.getInstance().getClientService();

        try {
            if (notAdmin()) {
                return "Wrong command";
            }
            response = clientService.getAllUserLoans(login);
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
