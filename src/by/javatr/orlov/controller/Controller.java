package by.javatr.orlov.controller;

import by.javatr.orlov.controller.command.Command;
import by.javatr.orlov.Parser;

public final class Controller {

    private final CommandProvider provider = new CommandProvider();
    private static Controller instance;

    private Controller() {};
    public static Controller getInstance(){
        if (instance == null)
            instance = new Controller();
        return instance;
    }
    public String executeTask (String request){
        String commandName;
        Command command;
        commandName = Parser.parseStr(request, 0);
        command = provider.getCommand(commandName);

        String response = null;

        response = command.execute(request);

        return response;
    }
}
