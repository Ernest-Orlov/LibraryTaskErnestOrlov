package by.javatr.orlov.controller.command.impl.client;

import by.javatr.orlov.controller.command.Command;

public class WrongRequest implements Command {
    @Override
    public String execute (String request){
        return "Wrong request";
    }
}
