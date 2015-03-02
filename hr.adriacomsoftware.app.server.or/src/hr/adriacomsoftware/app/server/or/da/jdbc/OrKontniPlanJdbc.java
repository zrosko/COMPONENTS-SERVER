package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrKontniPlanJdbc extends OrJdbc {
    public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT broj_konta AS id, broj_konta + ' - ' + isnull(RTRIM(naziv) ,'')AS name FROM or_kontni_plan");

	public OrKontniPlanJdbc() {
		setTableName("or_kontni_plan");
	}
}
