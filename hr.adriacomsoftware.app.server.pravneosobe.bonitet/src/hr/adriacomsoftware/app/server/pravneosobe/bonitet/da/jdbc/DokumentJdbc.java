package hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public final class DokumentJdbc extends J2EEDataAccessObjectJdbc {
     public DokumentJdbc() {
        setTableName("bonitet_dokument");
    }    
}