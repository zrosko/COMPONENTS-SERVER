package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

/************* DAO_JDBC  naplata_gr_tekuci_pracenje ************/

public final class NaplataGrTekuciJdbc extends TESTNaplataJdbc { 
	public NaplataGrTekuciJdbc () {
		setTableName("");
	}	
	
	public OsnovniRs daoProcitajSifre(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" SELECT vrsta, rb, id_sifre, naziv_sifre  /*naziv_sifre as name*/ " +
				   " FROM naplata_gr_tekuci_sifarnik " +
				   " WHERE vrsta = '"+value.get("vrsta")+"' " );
		sql.appendWhere();
		sql.appPrefix();
		sql.appInNoQuote("AND", "rb", value.get("@in"));
		sql.append(" ORDER BY rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new OsnovniRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public OsnovniRs daoIzvjestaji(OsnovniVo value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
	    int counter = 0;
		sp.append("{call ");
		sp.append("stp_naplata_gr_tekuci_facade");
		sp.append(" (?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(++counter,value.get("@@report_selected"));
			cs.setObject(++counter,value.get("oib"));
			cs.setObject(++counter,value.get("jmbg"));
			cs.setObject(++counter,value.get("koristi_alt_adresu"));
			cs.setObject(++counter,value.get("broj_partije"));
			cs.setObject(++counter,value.get("id_pracenja"));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
