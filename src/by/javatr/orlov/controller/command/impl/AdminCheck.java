package by.javatr.orlov.controller.command.impl;

import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.exception.ServiceException;
import by.javatr.orlov.service.factory.ServiceFactory;

public interface AdminCheck {
    default boolean notAdmin () throws ServiceException{
        ClientService clientService = ServiceFactory.getInstance().getClientService();
        return !clientService.getCurrentUser().isAdmin();
    }
}
