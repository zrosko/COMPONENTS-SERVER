package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;

public final class PokazateljVrstaJdbc extends RIZIKJdbc {
    public PokazateljVrstaJdbc() {
        setTableName("kr_pokazatelj_vrsta_log");
    }
}