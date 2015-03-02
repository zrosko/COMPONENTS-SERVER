package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EEOLTPJDBCService;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;

public class PoZahJdbc extends BankarskiJdbc {
    public static boolean _OLTP = false;
    public PoZahJdbc() {
        //setTableName("oltp");
    }
    public J2EEConnectionJDBC getConnection() {
        try {
            if(_OLTP)
                return (J2EEConnectionJDBC) J2EEOLTPJDBCService.getInstance().getConnection();
            else
                return (J2EEConnectionJDBC) J2EEDefaultJDBCService.getInstance().getConnection();
        } catch (Exception e) {
            //TODO aspect J2EETrace.trace(J2EETrace.E, e);
        }
        return null;
    }
 }