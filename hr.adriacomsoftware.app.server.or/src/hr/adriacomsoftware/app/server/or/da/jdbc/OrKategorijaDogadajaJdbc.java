package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;



public final class OrKategorijaDogadajaJdbc extends OrJdbc {
    public static final J2EESqlBuilder sql = new J2EESqlBuilder("SELECT kategorija AS id, kategorija + ' - ' + isnull(RTRIM(naziv) ,'')AS name FROM or_kategorija_dogadaja where razina in(1,2)");
    
    public OrKategorijaDogadajaJdbc() {
        setTableName("or_kategorija_dogadaja");
    }
 }