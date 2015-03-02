package hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc;

import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;


public final class PoduzeceJdbc extends J2EEDataAccessObjectJdbc {
     public PoduzeceJdbc() {
        setTableName("bonitet_poduzece");
    }    
}