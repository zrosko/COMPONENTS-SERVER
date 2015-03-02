package hr.adriacomsoftware.app.server.services.da.odbc;

import hr.adriacomsoftware.app.server.services.J2EEODBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public class ODDBJdbc extends J2EEDataAccessObjectJdbc {
    public ODDBJdbc() {
        //setTableName("");
    }
	public J2EEConnectionJDBC getConnection(){
		try {
			return (J2EEConnectionJDBC) J2EEODBCService.getInstance().getConnection();
		} catch (Exception e){
			//TODO aspect J2EETrace.trace(J2EETrace.E, e);
		}
		return null;
	}
}