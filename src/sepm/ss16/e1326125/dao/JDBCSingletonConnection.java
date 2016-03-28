package sepm.ss16.e1326125.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A singleton class handling the database connection.
 */
public class JDBCSingletonConnection {

    private static Connection connection = null;
    private static final Logger logger = LogManager.getLogger(JDBCSingletonConnection.class);

    private JDBCSingletonConnection() throws DAOException {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/sepm", "1326125", "Barta639");
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            throw new DAOException("Could not connect to database!");
        } catch (ClassNotFoundException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException("Driver not found.");
        }
    }

    /**
     * @return The one and only (singleton) database connection in the application.
     * @throws DAOException If not able to connect to database.
     */
    public static Connection getConnection() throws DAOException {
        if (connection == null) {
            new JDBCSingletonConnection();
            logger.info("Connection successfully established.");
        }

        return connection;
    }

    /**
     * Closes the database connection.
     * @throws DAOException If not able to close the connection.
     */
    public static void closeConnection() throws DAOException {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connection successfully closed.");
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                throw new DAOException(ex.getMessage());
            }
        }
    }

    /**
     * Tries to reestablish the connection if it has been lost.
     * @return The database connection.
     * @throws DAOException If can not reconnect.
     */
    public static Connection reconnectIfConnectionLost() throws DAOException {
        try {
            if (connection != null && !connection.isValid(3)) {
                connection = null;
                logger.error("Connection lost.");
            }
        } catch (SQLException ex) {
                logger.debug(ex.getMessage());
                throw new DAOException(ex.getMessage());
            }

            connection = JDBCSingletonConnection.getConnection();
            return connection;
    }
}
