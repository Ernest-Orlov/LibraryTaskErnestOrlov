package by.javatr.orlov.dao;


import by.javatr.orlov.bean.User;
import by.javatr.orlov.dao.exception.DAOException;

import java.util.ArrayList;

public interface UserDAO {


    ArrayList<User> deserialize () throws DAOException;

    void serialize (ArrayList<User> array) throws DAOException;
}
