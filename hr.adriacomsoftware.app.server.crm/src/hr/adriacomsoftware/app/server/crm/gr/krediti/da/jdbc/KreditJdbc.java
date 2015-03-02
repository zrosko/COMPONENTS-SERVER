package hr.adriacomsoftware.app.server.crm.gr.krediti.da.jdbc;

import hr.adriacomsoftware.app.server.crm.da.jdbc.CrmJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;


public final class KreditJdbc extends CrmJdbc {
	public AS2RecordList daoFindKredite(AS2Record value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_proizvodi_gr");
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter, value.getAsSqlDate("datum"));
			cs.setObject(++counter, value.get("pretrazivanje"));
			AS2RecordList as2rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }