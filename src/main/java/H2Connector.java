import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class H2Connector {
    private static final String user = "user";
    private static final String password = "user";
    private static final String driver = "org.h2.Driver";
    private static final String url = "jdbc:h2:file:C:\\Users\\Administrator\\Desktop\\Homework\\DevTools\\database\\database";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private static final String insert = "INSERT INTO SEARCH (WORD, COUNT, PATH) VALUES ('%s', %s, '%s' )";
    private static final String get = "SELECT COUNT, PATH FROM SEARCH WHERE WORD = '%s' ORDER BY COUNT DESC";

    private static H2Connector instance;

    private H2Connector() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized H2Connector getInstance() {
        if (instance == null) {
            instance = new H2Connector();
        }

        return instance;
    }

    public void insertWord(String word, Long count, String path) {
        String query = String.format(insert, word, count, path);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Long> getData(String word) {
        String query = String.format(get, word);
        Map<String, Long> result = new HashMap<>();
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.put(resultSet.getString("PATH"), resultSet.getLong("COUNT"));
            }
            return result;
        } catch (SQLException ignored) {
            return result;
        }
    }
}
