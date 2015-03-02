package hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.CallableStatement;


public final class KontaktKlijentJdbc extends KontaktJdbc {
	public KontaktKlijentJdbc() {
		setTableName("kontakt_klijent");
	}
	public AS2RecordList daoFindListuKlijenata(AS2Record value) {
		StringBuffer sql = new StringBuffer();
		int counter = 0;
		sql.append("{call ");
		sql.append("stp_crm_kontakt_klijent");
		sql.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sql.toString());
			cs.setObject(++counter, value.get("vrsta"));
			AS2RecordList as2rs = transformResultSet( cs.executeQuery());
			cs.close();
			return as2rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}