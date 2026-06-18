package database;

public class Constantes {

    // Configuración local
    public static final String USER_LOCAL     = "postgres";
    public static final String PASSWORD_LOCAL = "123";
    public static final String HOST_LOCAL     = "127.0.0.1";
    public static final String PORT_LOCAL     = "5432";
    public static final String DBNAME_LOCAL   = "db_grupo13sa";

    // Configuración remota
    public static final String USER_REMOTE     = "grupo13sa";
    public static final String PASSWORD_REMOTE = "grupo13sagrupo13sa";
    //public static final String PASSWORD_REMOTE = "grup013grup013*";
    public static final String HOST_REMOTE     = "www.tecnoweb13sa.online";
    //public static final String HOST_REMOTE     = "www.tecnoweb.org.bo";
    public static final String PORT_REMOTE     = "5432";
    public static final String DBNAME_REMOTE   = "db_grupo13sa";

    // Configuración activa — cambiar aquí para apuntar a local o remota
    public static final String USER     = USER_REMOTE;
    public static final String PASSWORD = PASSWORD_REMOTE;
    public static final String HOST     = HOST_REMOTE;
    public static final String PORT     = PORT_REMOTE;
    public static final String DBNAME   = DBNAME_REMOTE;

    // PagoFacil MasterQR API
    public static final String PAGOFACIL_TOKEN_SERVICE = "a52063a1be51238364d1e55c3132f7c3b0625cfab2f37ac522a85cabaebdeb993bc8f601284fc35eee7f983e663d375c58f835b94a2aa558e8e2f9693885a2156598e1da9a7bf8caba2f3d6459f167fed3a50a375e0d80d517b560f04f017db078c3ee80e37f608537e752c82629095fbdcc6a4edcf4f22269c2f98f5d431c72";
    public static final String PAGOFACIL_TOKEN_SECRET  = "eed967baf4b64e8980051c1b";
    public static final String PAGOFACIL_CALLBACK_URL  = "https://www.tecnoweb13sa.online/callback";
}
