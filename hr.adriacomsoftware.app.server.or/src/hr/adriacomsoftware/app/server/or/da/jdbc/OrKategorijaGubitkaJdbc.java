package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrKategorijaGubitkaJdbc extends OrJdbc {
    public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT id_kategorije_gubitka AS id, rtrim(CONVERT(char(10), id_kategorije_gubitka)) + '-' + RTRIM(naziv) AS name FROM or_kategorija_gubitka");

	public OrKategorijaGubitkaJdbc() {
		setTableName("or_kategorija_gubitka");
	}
}