package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrPrijetnjaJdbc extends OrJdbc {
	public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT id_prijetnje AS id, rtrim(CONVERT(char(10), id_prijetnje)) + '-' + RTRIM(naziv) AS name FROM or_prijetnja");

	public OrPrijetnjaJdbc() {
		setTableName("or_prijetnja");
	}
}