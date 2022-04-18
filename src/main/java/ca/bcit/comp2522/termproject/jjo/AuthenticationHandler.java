package ca.bcit.comp2522.termproject.jjo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Utility class for authentication.
 * @author adedejitoki & vasilyshorin
 * @version 1.0
 */
public final class AuthenticationHandler {
    private static Connection connection;
    private static final String DB_USER = "root";
    //fixme: change password
    private static final String DB_PASSWORD = "";


    private AuthenticationHandler() {
    }

    /**
     * Logs in a user.
     *
     * @param userName the user name.
     * @param password the password.
     * @return true if the user is logged in successfully, false otherwise.
     */
    public static boolean login(final String userName, final String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp2522",
                         DB_USER, DB_PASSWORD);
            PreparedStatement st = connection.prepareStatement(
                    "Select user_id, password from users where user_id=? and password=?");

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

    /**
     * Registers a user.
     * @param userName the user name.
     * @param password the password.
     * @return true if the user is registered successfully, false otherwise.
     */
    public static boolean createAccount(final String userName, final String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp2522",
                    DB_USER, DB_PASSWORD);
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
