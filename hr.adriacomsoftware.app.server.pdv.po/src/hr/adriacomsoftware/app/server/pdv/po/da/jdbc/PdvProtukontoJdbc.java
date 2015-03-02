package hr.adriacomsoftware.app.server.pdv.po.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;


public final class PdvProtukontoJdbc extends OLTPJdbc {
    
    public PdvProtukontoJdbc() {
        setTableName("bi_pdv_konto_protukonto");
    }
}