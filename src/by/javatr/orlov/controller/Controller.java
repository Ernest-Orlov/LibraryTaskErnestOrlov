package by.javatr.orlov.controller;

import by.javatr.orlov.Parser;
import by.javatr.orlov.controller.command.Command;

public final class Controller {

    private static Controller instance;
    private final CommandProvider provider = new CommandProvider();

    private Controller (){
    }

    public static Controller getInstance (){
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
