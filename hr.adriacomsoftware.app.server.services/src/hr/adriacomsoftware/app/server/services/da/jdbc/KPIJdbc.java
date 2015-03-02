package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EEKPIJDBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public class KPIJdbc extends J2EEDataAccessObjectJdbc {
    public KPIJdbc() {
        //setTableName("");
    }
    public J2EEConnectionJDBC getConnection() {
        try {
            return (J2EEConnectionJDBC) J2EEKPIJDBCService.getInstance().getConnection();
        } catch (Exception e) {			
            //TODO aspect J2EETrace.trace(J2EETrace.E, e);
        }
        return null;
    }
}