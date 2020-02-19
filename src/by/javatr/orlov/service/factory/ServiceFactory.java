package by.javatr.orlov.service.factory;

import by.javatr.orlov.service.ClientService;
import by.javatr.orlov.service.LibraryService;
import by.javatr.orlov.service.impl.ClientServiceImpl;
import by.javatr.orlov.service.impl.LibraryServiceImpl;

public final class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    private final ClientService clientService = new ClientServiceImpl();
    private final LibraryService libraryService = new LibraryServiceImpl();

    private ServiceFactory (){
    }

    public static ServiceFactory getInstance (){
        return instance;
    }

    public ClientService getClientService (){
        return clientService;
    }

    public LibraryService getLibraryService (){
        return libraryService;
    }
}
