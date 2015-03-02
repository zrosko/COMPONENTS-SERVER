package hr.adriacomsoftware.app.server.pdv.ostaliposlovi.da.jdbc;

import hr.adriacomsoftware.app.common.pdv.ostaliposlovi.dto.PdvOstaliPosloviRs;
import hr.adriacomsoftware.app.common.pdv.ostaliposlovi.dto.PdvOstaliPosloviVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PdvOstaliPosloviJdbc extends J2EEDataAccessObjectJdbc {

	public PdvOstaliPosloviJdbc() {
		setTableName("bi_ostali_poslovi_pdv");
	}

	public PdvOstaliPosloviVo daoLoad(PdvOstaliPosloviVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_ostali_poslovi_pdv " + "WHERE id = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getId());
			pstmt.setMaxRows(0);
			PdvOstaliPosloviVo j2eers = new PdvOstaliPosloviVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PdvOstaliPosloviRs daoLoadAll(PdvOstaliPosloviVo value){
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_ostali_poslovi_pdv ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PdvOstaliPosloviRs j2eers = new PdvOstaliPosloviRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PdvOstaliPosloviRs daoFind(PdvOstaliPosloviVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_ostali_poslovi_pdv ");
		sql.append("where  datum >= ? and datum <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appEqual("AND", "broj_partije", value.get("broj_partije"));
		sql.appEqual("AND", "maticni_broj", value.get("maticni_broj"));
		sql.appEqual("AND", "organizacijska_jedinica",
				value.get("organizacijska_jedinica"));
		sql.appLike("AND", "oznaka", value.get("oznaka"));
		sql.append("order by redni_broj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate("datum_od"));
			pstmt.setDate(2, value.getAsSqlDate("datum_do"));
			pstmt.setMaxRows(0);
			PdvOstaliPosloviRs j2eers = new PdvOstaliPosloviRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}