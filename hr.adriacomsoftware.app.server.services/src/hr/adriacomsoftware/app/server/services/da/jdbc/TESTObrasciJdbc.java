package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EETESTJDBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public class TESTObrasciJdbc extends J2EEDataAccessObjectJdbc {
    public TESTObrasciJdbc() {
        //setTableName("test");
    }
    public J2EEConnectionJDBC getConnection() {
        try {
            return (J2EEConnectionJDBC) J2EETESTJDBCService.getInstance().getConnection();
        } catch (Exception e) {
        	//TODO aspect J2EETrace.trace(J2EETrace.E, e);
        }
        return null;
    }
 }