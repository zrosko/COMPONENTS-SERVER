package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrUzrokDogadajaJdbc extends OrJdbc {
	public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT uzrok AS id, uzrok + ' - ' + isnull(RTRIM(naziv) ,'')AS name FROM or_uzrok_dogadaja");
    
    public OrUzrokDogadajaJdbc() {
        setTableName("or_uzrok_dogadaja");
    }
 }