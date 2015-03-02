package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class PrometKreditJdbc extends BankarskiJdbc implements BankaConstants {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT ISNULL(MAX(broj_stavke), 0) AS max_broj_stavke FROM bi_gr_promet_kredit ";
 
    public PrometKreditJdbc() {
        setTableName("bi_gr_promet_kredit");
    } 
}