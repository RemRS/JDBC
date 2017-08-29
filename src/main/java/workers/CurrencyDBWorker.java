package workers;

import enums.Currency;

import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyDBWorker extends AbstractDBWorker<Currency> {

    private static final String query = "INSERT INTO provider_curr (currency) VALUES (?)";

    @Override
    public void insert(Currency object) {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, object.toString());
            preparedStatement.execute();
        }catch (SQLException e){
            System.err.println("Unable to insert in Connection table \n" + e.toString());
        }
    }

    ArrayList select() {
        throw new UnsupportedOperationException();
    }
}
