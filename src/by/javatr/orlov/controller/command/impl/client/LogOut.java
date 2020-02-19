package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.factory.ServiceFactory;

public class LogOut implements Command {


    @Override
    public String execute (String request){
        String response;

        ClientService clientService = ServiceFactory.getInstance().getClientService();

        clientService.logOut();
        response = "Logout complete!";
        return response;
    }

}
