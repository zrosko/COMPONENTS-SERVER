package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.AS2CRMPRODJDBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;


public class CRMPRODJdbc extends BankarskiJdbc {
    public CRMPRODJdbc() {
        //setTableName("crm_prod");
    }
    public J2EEConnectionJDBC getConnection() {
        try {
            return (J2EEConnectionJDBC) AS2CRMPRODJDBCService.getInstance().getConnection();
        } catch (Exception e) {
            //TODO aspect J2EETrace.trace(J2EETrace.E, e);
        }
        return null;
    }
 }