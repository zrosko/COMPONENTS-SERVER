package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;


public final class IsmsZgradaJdbc extends CMDBJdbc {
    public IsmsZgradaJdbc() {
        setTableName("isms_zgrada");
    }
}