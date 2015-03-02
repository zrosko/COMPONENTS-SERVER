package hr.adriacomsoftware.app.server.gradani.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataGradaniJdbc extends J2EEDataAccessObjectJdbc {
	public NaplataGradaniJdbc() {
		setTableName("");
	}

	public OsobaRs daoPronadiOsobe(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_naplata_pogled ");
		sql.appLike("AND", "ime_prezime", value.getImePrezime());
		sql.appEqualNoQuote("AND", "jmbg", value.getJmbg());
		sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		sql.appendln("ORDER BY ime_prezime");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPregledUgovora(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM bi_gr_ugovor ");
		sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		sql.appendln("ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}