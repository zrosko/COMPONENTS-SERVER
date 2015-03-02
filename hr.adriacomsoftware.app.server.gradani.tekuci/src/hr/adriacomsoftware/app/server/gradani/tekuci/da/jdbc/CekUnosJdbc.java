package hr.adriacomsoftware.app.server.gradani.tekuci.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.tekuci.dto.CekRs;
import hr.adriacomsoftware.app.common.gradani.tekuci.dto.CekVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CekUnosJdbc extends OLTPJdbc {
	public CekUnosJdbc() {
		setTableName("bi_gr_tekuci_cek_unos");
	}

	public CekRs daoFind(CekVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select *   ");
		sql.append(" FROM view_gr_tekuci_cek_pogled order by datum_prometa desc, datum_realizacije, broj_partije, serijski_broj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			CekRs j2eers = new CekRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return new CekRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public CekRs daoValidacija(CekVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select dbo.fn_scl_gr_tekuci_cek_validacija(?,?,?,?,?) as error_code  ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getAsObject("broj_partije"));
			pstmt.setObject(2, value.getAsObject("oib"));
			pstmt.setObject(3, value.getAsObject("jmbg"));
			pstmt.setObject(4, value.getAsObject("serijski_broj"));
			pstmt.setObject(5, value.getAsObject("akcija"));
			pstmt.setMaxRows(0);
			CekRs j2eers = new CekRs(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			if (j2eers != null && j2eers.getAsInt("error_code", 0) > 0) {
				throw new AS2Exception(j2eers.get("error_code"));
			}
			return new CekRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public CekRs daoFindNeiskoristeni(CekVo value)  {
		// neki cekovi nemaju status 1 iako su knjizeni
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select serijski_broj as name from BI_PROD.dbo.bi_gr_tekuci_cek ");
		sql.append("where broj_partije = ? and (isnull(status_ceka,0) = 0");
		sql.append("and serijski_broj not in (select serijski_broj from OLTP_PROD.dbo.bi_gr_tekuci_cek_unos ");
		sql.append("where broj_partije = ? and datum_knjizenja is not null))");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getAsObject("broj_partije"));
			pstmt.setObject(2, value.getAsObject("broj_partije"));
			pstmt.setMaxRows(0);
			CekRs j2eers = new CekRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return new CekRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}