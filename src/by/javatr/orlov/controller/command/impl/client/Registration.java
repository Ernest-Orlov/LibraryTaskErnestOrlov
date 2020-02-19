package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.Parser;
import by.javatr.orlov.controller.command.Command;

public abstract class Registration implements Command {

    @Override
    public String execute (String request){

        String name = Parser.parseStr(request, 1);
        String login = Parser.parseStr(request, 2);
        String password = Parser.parseStr(request, 3);
        return register(name, login, password);
    }

    public abstract String register (String name, String login, String password);


}
