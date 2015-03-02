package hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class PrnCrnaListaOsobaAliasJdbc extends BankarskiJdbc {

    public PrnCrnaListaOsobaAliasJdbc() {
        setTableName("prn_crna_lista_osoba_alias");
    }
}