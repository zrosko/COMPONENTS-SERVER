package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class PrometStednjaKunaJdbc extends BankarskiJdbc implements BankaConstants {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT max(broj_stavke) as max_broj_stavke FROM bi_gr_promet_stednja_kuna ";
 
    public PrometStednjaKunaJdbc() {
        setTableName("bi_gr_promet_stednja_kuna");
    } 
}