package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.Parser;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class LogIn implements Command {

    @Override
    public String execute (String request){

        String login = Parser.parseStr(request, 1);
        String password = Parser.parseStr(request, 2);
        String response;

        ClientService clientService = ServiceFactory.getInstance().getClientService();

        try {
            if (clientService.logIn(login, password))
                response = "Welcome " + clientService.getCurrentUser().getName() + " !";
            else response = "Wrong username or password!";
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
