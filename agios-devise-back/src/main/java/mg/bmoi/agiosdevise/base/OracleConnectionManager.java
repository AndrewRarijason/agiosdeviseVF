package mg.bmoi.agiosdevise.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class OracleConnectionManager {
    private final DataSource dataSource;

    @Autowired
    public OracleConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean isConnected() {
        try(Connection connection = getConnection()){
            return connection.isValid(5);

        }catch (SQLException e){
            return false;
        }
    }
}
