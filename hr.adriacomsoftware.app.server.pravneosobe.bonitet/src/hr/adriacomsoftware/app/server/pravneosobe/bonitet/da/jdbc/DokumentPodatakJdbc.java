package hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public final class DokumentPodatakJdbc extends J2EEDataAccessObjectJdbc {
     public DokumentPodatakJdbc() {
        setTableName("bonitet_dokument_podatak");
    }    
}