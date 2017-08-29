package workers;

import enums.Country;

import java.sql.SQLException;
import java.util.ArrayList;

public class CountryDBWorker extends AbstractDBWorker<Country> {

    private static final String query = "INSERT INTO provider_country (country) VALUES (?)";

    @Override
    public void insert(Country object) {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, object.toString());
            preparedStatement.execute();
        }catch (SQLException e){
            System.err.println("Unable to insert in Country table \n" + e.toString());
        }
    }

    ArrayList select() {
        throw new UnsupportedOperationException();
    }
}
