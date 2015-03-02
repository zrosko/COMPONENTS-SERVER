package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.partija.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogObavijestVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;

public final class PoZahObavijestNalogaJdbc extends BankarskiJdbc {
    public PoZahObavijestNalogaJdbc() {
        setTableName("po_zah_nalog_obavijest");
    }
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "select * from view_po_zah_nalog_obavijesti_pogled "+
		"where id_naloga = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
			pstmt.setObject(1,value.get(ZahNalogObavijestVo.PO_ZAH_NALOG_OBAVIJEST__ID_NALOGA));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
  }