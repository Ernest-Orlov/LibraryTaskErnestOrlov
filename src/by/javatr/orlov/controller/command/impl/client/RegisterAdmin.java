package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public class RegisterAdmin extends Registration {
    @Override
    public String register (String name, String login, String password){
        String response;
        try {
            ClientService clientService = ServiceFactory.getInstance().getClientService();
            clientService.registerAdmin(name, login, password);
            response = "Admin registration complete";
        } catch (ServiceException e) {
            response = e.getMessage();
        }
        return response;
    }
}
