package hr.adriacomsoftware.app.server.gk.da.jdbc;

import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class GlavnaKnjigaBsaJdbc extends BankarskiJdbc implements BankaConstants {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT max(abs(redni_broj_stavke)) as max_broj_stavke FROM bi_glavna_knjiga_bsa";
 
    public GlavnaKnjigaBsaJdbc() {
        setTableName("bi_glavna_knjiga_bsa");
    }
}