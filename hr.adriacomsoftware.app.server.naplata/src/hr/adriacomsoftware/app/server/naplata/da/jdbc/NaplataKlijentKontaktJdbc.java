package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentKontaktRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataKlijentKontaktJdbc extends OLTPJdbc {
    public NaplataKlijentKontaktJdbc() {
        setTableName("naplata_klijent_kontakt");
    }
    public NaplataKlijentKontaktRs daoFind(NaplataKlijentVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select *   ");
		sql.append(" FROM view_naplata_klijent_kontakt where oib = ? order by status_kontakta desc, ime, prezime");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(NaplataKlijentVo.NAPLATA_KLIJENT__OIB));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentKontaktRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }