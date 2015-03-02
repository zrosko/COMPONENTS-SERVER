package hr.adriacomsoftware.app.server.pdv.po.da.jdbc;

import hr.adriacomsoftware.app.common.pdv.po.dto.PdvRs;
import hr.adriacomsoftware.app.common.pdv.po.dto.PdvVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PdvJdbc extends OLTPJdbc {

	public PdvJdbc() {
		setTableName("pdv_po_racun");
	}

	public PdvVo daoLoad(PdvVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_pdv_po_racun_lista() "
				+ "WHERE id_unosa = ? and isnull(ispravno,1) = 1 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdUnosa());
			pstmt.setMaxRows(0);
			PdvVo j2eers = new PdvVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PdvRs daoLoadAll(PdvVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_pdv_po_racun_lista() "
				+ "where isnull(ispravno,1) = 1 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PdvRs j2eers = new PdvRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiRedniBroj(PdvVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select isnull(max(redni_broj),0)+1 as max_redni_broj from pdv_po_racun ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			PdvVo j2eers = new PdvVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers.get("max_redni_broj");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PdvRs daoFind(PdvVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.get("izvjestaj").equals("r1_razlike")) {
			sql.append("select *, '" + value.getAsSqlDate("datum_od")
					+ "' as datum_od, '" + value.getAsSqlDate("datum_do")
					+ "' as datum_do ");
			sql.append("from dbo.bi_pdv_unos_podataka ");
			sql.append("where ispravno in(1,0) and datum >= ? and datum <= ? ");
		} else {
			sql.append("select * from dbo.fn_tbl_pdv_po_racun_lista() ");
			sql.append("where isnull(ispravno,1) = 1 and datum >= ? and datum <= ? ");
		}
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
			PdvRs j2eers = new PdvRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}