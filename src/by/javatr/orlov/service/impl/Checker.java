package by.javatr.orlov.service.impl;

import by.javatr.orlov.service.exception.ServiceException;

public class Checker {
    public static void validateStringField (String string, String fieldName) throws ServiceException{
        checkNullString(string, fieldName);
        checkEmptyString(string, fieldName);

    }

    public static void checkNullString (String string, String fieldName) throws ServiceException{
        if (string == null)
            throw new ServiceException("Null pointer in " + fieldName);
    }

    public static void checkEmptyString (String string, String fieldName) throws ServiceException{
        if (string.isEmpty())
            throw new ServiceException(fieldName + " is empty");
    }
}
