package hr.adriacomsoftware.app.server.pranjenovca.transakcija.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;


public final class PrnTransakcijaOsobaJdbc extends BankarskiJdbc {
   
	public PrnTransakcijaOsobaJdbc() {
        setTableName("prn_transakcija_osoba");
    }
    
}