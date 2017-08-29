package workers;

import java.sql.*;
import java.util.ArrayList;

public abstract class AbstractDBWorker<T> {

    private static final String URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "antinel";

    protected Connection connection;
    protected Statement statement;
    protected PreparedStatement preparedStatement;

    public AbstractDBWorker() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        }catch (SQLException e){
            System.err.println("Unable to connect to database! \n" + e.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    abstract void insert(T object);

    abstract ArrayList<T> select();

    public void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            System.err.println("Unable to close connection \n" + e.toString());
        }
    }

}
