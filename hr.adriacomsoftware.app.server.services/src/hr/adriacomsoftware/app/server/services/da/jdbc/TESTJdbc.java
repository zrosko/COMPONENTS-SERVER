package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EETESTJDBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;


public class TESTJdbc extends BankarskiJdbc {
    public TESTJdbc() {
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