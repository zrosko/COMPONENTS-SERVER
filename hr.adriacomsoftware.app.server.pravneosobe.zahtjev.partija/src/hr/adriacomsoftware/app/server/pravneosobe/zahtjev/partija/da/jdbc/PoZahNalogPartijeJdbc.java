package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.partija.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogPartijeRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogPartijeVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.util.Calendar;

public final class PoZahNalogPartijeJdbc extends BankarskiJdbc {
    public PoZahNalogPartijeJdbc() {
        setTableName("po_zah_nalog_otvaranja_partije");
    }
    public int daoFindNextEvidencijskiBroj(ZahNalogPartijeVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT max(redni_broj) as redni_broj "+
		" FROM po_zah_nalog_otvaranja_partije where YEAR(datum_naloga) = ? and isnull(ispravno,1) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setInt(1, value.getDatumNaloga().get(Calendar.YEAR));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.getAsInt(ZahNalogPartijeVo.PO_ZAH_NALOG_OTVARANJA_PARTIJE__REDNI_BROJ)+1;//povećaj za 1
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public ZahNalogPartijeRs daoPronadiNaloge(ZahNalogPartijeVo value, boolean pretrazivanje)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from view_po_zah_nalozi_partija_pogled ");
		sql.append(" where datum_naloga between ? and ? ");
		sql.appendWhere();
		sql.appPrefix();
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appLike("AND", "broj_zahtjeva", value.getBrojZahtjeva());
			sql.appEqual("AND", "maticni_broj", value.getMaticniBroj());
			// sql.appEqual("AND", "tip", value.getTip());
			// sql.appEqual("AND", "vrsta", value.getVrsta());
			sql.appLike("AND", "naziv", value.getNaziv());
			sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj());
			sql.appEqualNoQuote("AND", "oib", value.getOib());
			sql.appEqualNoQuote("AND", "organizacijska_jedinica",
					value.get("organizacijska_jedinica"));
		}
		sql.appendln(" ORDER BY datum_naloga");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate("datum_naloga"));
			pstmt.setDate(2, value.getAsSqlDate("datum_naloga_do"));
			pstmt.setMaxRows(0);
			ZahNalogPartijeRs j2eers = new ZahNalogPartijeRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}			
    }
    public ZahNalogPartijeRs daoProcitajNaloge(ZahNalogPartijeVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from view_po_zah_nalozi_partija_pogled ");
		sql.append(" where isnull(ispravno,1) = 1 ");
		sql.appendWhere();
		sql.appPrefix();
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY datum_naloga desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			ZahNalogPartijeRs j2eers = new ZahNalogPartijeRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }