package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;


public final class IsmsLokacijaJdbc extends CMDBJdbc {
    public IsmsLokacijaJdbc() {
        setTableName("isms_lokacija");
    }
}