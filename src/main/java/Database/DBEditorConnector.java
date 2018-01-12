package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBEditorConnector {
    INSTANCE;
    private String dbUrl = "jdbc:mysql://localhost:3306/blog?useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String user = "root";
    String password = "0GdQ1VQA";

    DBEditorConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl,user,password);
            System.out.println("Got the connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
