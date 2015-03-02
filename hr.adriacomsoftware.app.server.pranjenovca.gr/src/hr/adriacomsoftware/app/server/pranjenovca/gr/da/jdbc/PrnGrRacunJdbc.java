package hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc;

import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnGrRacunRs;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnGrRacunVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnGrRacunJdbc extends BankarskiJdbc {

	public PrnGrRacunJdbc() {
		setTableName("prn_gr_upitnik_racun");
	}

	public PrnGrRacunVo daoLoad(PrnGrRacunVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_racun_pogled WHERE id_upitnika = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdUpitnika());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnGrRacunVo j2eers = new PrnGrRacunVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnGrRacunRs daoListaRacuna(PrnGrRacunVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT DISTINCT CONVERT(decimal(10,0),O.broj_partije) AS broj_partije, "
				+ "T.opis as tip_racuna, "
				+ "CONVERT(char(15), O.datum_otvaranja, 104) AS datum_otvaranja, "
				+ "CONVERT(char(15), O.datum_zatvaranja, 104) AS datum_zatvaranja, "
				+ "dbo.fn_scl_pnr_gr_brojac_upitnika(U.jmbg) AS brojac_upitnika ");
		sql.append("FROM bi_gr_partija as O LEFT OUTER JOIN prn_gr_upitnik AS U ON O.broj_partije = U.broj_partije "
				+ "LEFT OUTER JOIN bi_tip_racuna T ON T.tip_racuna = substring(convert(char(10), convert(decimal(10,0),O.broj_partije)),1,2) ");
		sql.append("WHERE O.jmbg = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getJmbg());
			pstmt.setMaxRows(0);
			PrnGrRacunRs j2eers = new PrnGrRacunRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}