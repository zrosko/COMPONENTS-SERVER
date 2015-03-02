package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EEOLTPJDBCService;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;



public class OLTPJdbc extends BankarskiJdbc {
	public OLTPJdbc() {
		// setTableName("oltp");
	}

	public J2EEConnectionJDBC getConnection() {
		try {
			return (J2EEConnectionJDBC) J2EEOLTPJDBCService.getInstance()
					.getConnection();
		} catch (Exception e) {
			// TODO aspect J2EETrace.trace(J2EETrace.E, e);
		}
		return null;
	}
}