package ca.bcit.comp2522.termproject.jjo;

import java.sql.*;

public final class AuthenticationHandler {
    private static AuthenticationHandler instance;
    private static Connection connection;
    private static final String dbUser = "root";
    private static final String dbPassword = "";


    private AuthenticationHandler() throws ClassNotFoundException {
    }

    public static boolean login(final String userName, final String password) {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/comp2522",
                    dbUser, dbPassword);
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select user_id, password from users where user_id=? and password=?");

            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful");
                return true;
            } else {
                System.out.println("Login Does Not Exist");
                return false;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
    public static boolean createAccount(final String userName, final String password) {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/comp2522",
                    dbUser, dbPassword);
            // Create a statement to send on the connection...
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users VALUES (?,?)");
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.executeUpdate();

            return login(userName, password);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }

}
