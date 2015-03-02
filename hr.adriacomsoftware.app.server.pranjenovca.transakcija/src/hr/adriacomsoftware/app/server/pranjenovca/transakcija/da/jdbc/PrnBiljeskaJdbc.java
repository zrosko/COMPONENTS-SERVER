package hr.adriacomsoftware.app.server.pranjenovca.transakcija.da.jdbc;

import hr.adriacomsoftware.app.common.pranjenovca.transakcija.dto.PrnTransakcijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnBiljeskaJdbc extends BankarskiJdbc {
	public PrnBiljeskaJdbc() {
		setTableName("prn_biljeska");
	}

	public PrnTransakcijaVo daoCitajBiljesku(PrnTransakcijaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select id_biljeske,CONVERT(decimal(13,0),jmbg_mb) AS jmbg_mb, "
				+ "CONVERT(decimal(11, 0), oib) AS oib, datum_prometa, biljeska, "
				+ "convert(varchar(10),convert(decimal(10,0),broj_partije)) as broj_partije "
				+ "FROM prn_biljeska where datum_prometa = ? ");
		if (value.get("jmbg_mb_").equals("") && value.get("oib_").equals(""))
			sql.append("and broj_partije = " + value.get("broj_partije_" + ""));
		else
			sql.append("and ( jmbg_mb = " + value.get("jmbg_mb_")
					+ " or oib = " + value.get("jmbg_mb_") + " ) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate("datum_prometa"));
			pstmt.setMaxRows(1);
			AS2RecordList as2rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnTransakcijaVo j2eers = new PrnTransakcijaVo(as2rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoUkloniBiljesku(PrnTransakcijaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("DELETE " + "FROM prn_biljeska where datum_prometa = ? "
				+ "and ( jmbg_mb = ? or oib = ? ) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate("datum_prometa"));
			pstmt.setObject(2, value.get("jmbg_mb"));
			pstmt.setObject(3, value.get("oib"));
			pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}