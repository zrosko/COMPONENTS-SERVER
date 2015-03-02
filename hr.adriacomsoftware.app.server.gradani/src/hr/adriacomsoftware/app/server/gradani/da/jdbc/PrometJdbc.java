package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class PrometJdbc extends BankarskiJdbc implements BankaConstants {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT isnull(max(broj_stavke),0) as max_broj_stavke  FROM bi_gr_promet ";
 
    public PrometJdbc() {
        setTableName("bi_gr_promet");
    } 
}