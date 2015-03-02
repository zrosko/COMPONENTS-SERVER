package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrKontrolaJdbc extends OrJdbc {
    public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT id_kontrole AS id, rtrim(CONVERT(char(10), id_kontrole)) + '-' + RTRIM(naziv) AS name FROM or_kontrola");

	public OrKontrolaJdbc() {
		setTableName("or_kontrola");
	}
}