package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConeccion {
        
    private static final String DRIVER = "jdbc:postgresql://";
    
    private Connection connection;
    private final String user;
    private final String password; //root
    private final String host;
    private final String port;
    private final String name;
    private final String url;

    public DBConeccion() {
        this(Constantes.USER, Constantes.PASSWORD, Constantes.HOST, Constantes.PORT, Constantes.DBNAME);
    }

    public DBConeccion(String user, String password, String host, String port, String name) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.name = name;
        this.url = DRIVER + host + ":" + port + "/" + name;
    }
    
    public Connection connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.err.println("Class SqlConnection.java dice: "
                    + "Ocurrio un error al momento de establecer una conexion connect()");
        }
        return connection;
    }
    
    public void closeConnection() {
    if (connection != null) {
        try {
            connection.close();
            connection = null; // Opcionalmente, puedes asignarlo a null después de cerrarlo.
        } catch (SQLException ex) {
            System.err.println("Class SqlConnection.java dice: "
                    + "Ocurrió un error al momento de cerrar la conexión closeConnection(): " + ex.getMessage());
        }
    } else {
        System.err.println("Class SqlConnection.java dice: La conexión ya es null, no se puede cerrar.");
    }
}

}



