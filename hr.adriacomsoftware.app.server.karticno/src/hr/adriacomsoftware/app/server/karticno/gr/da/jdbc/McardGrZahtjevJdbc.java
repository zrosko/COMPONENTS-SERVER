package hr.adriacomsoftware.app.server.karticno.gr.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevRs;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class McardGrZahtjevJdbc extends OLTPJdbc {
    public McardGrZahtjevJdbc() {
        setTableName("mcard_gr_zahtjev");
    }
    public McardGrZahtjevRs daoFindZahtjeve(McardGrZahtjevVo value, boolean pretrazivanje) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from view_mcard_gr_zahtjev_pogled ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appEqual("AND", "broj_zahtjeva", value.getBrojZahtjeva());
			sql.appEqualNoQuote("AND", "oib", value.getOib());
			sql.appLike("AND", "ime_prezime", value.getImePrezime());
			sql.appEqualNoQuote("AND", "broj_partije_mcard",
					value.getBrojPartijeMcard());
			sql.appGreatherOrEqual("AND", "datum_zaprimanja",value.get("datum_zaprimanja_od"));
			sql.appLessOrEqual("AND", "datum_zaprimanja", value.get("datum_zaprimanja_do"));
			sql.appGreatherOrEqual("AND", "datum_odobravanja", value.get("datum_odobravanja_od"));
			sql.appLessOrEqual("AND", "datum_odobravanja", value.get("datum_odobravanja_do"));
			sql.appLike("AND", "status_zahtjeva", value.getStatusZahtjeva());
			sql.appLike("AND", "vrsta_zahtjeva_", value.get("vrsta_zahtjeva").trim());
		}
		sql.append(" order by broj_zahtjeva desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList  j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new McardGrZahtjevRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public McardGrZahtjevVo daoLoadZahtjevOdluka(McardGrZahtjevVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from dbo.fn_tbl_mcard_gr_zahtjev_odluka(?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			McardGrZahtjevVo j2eevo = new McardGrZahtjevVo(loc_rs);
			pstmt.close();
	        return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public McardGrZahtjevVo daoLoadZahtjev(McardGrZahtjevVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from view_mcard_gr_zahtjev where broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			McardGrZahtjevVo j2eevo = new McardGrZahtjevVo(loc_rs);
			j2eevo.setMetaData(loc_rs.getMetaData());
			pstmt.close();
	        return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public McardGrZahtjevVo daoLoadPodatkeKlijenta(McardGrZahtjevVo value)  {
		McardGrZahtjevVo j2eevo = null;
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from dbo.fn_tbl_mcard_gr_zahtjev_postojeci_podaci(?,?,?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getOib());
			pstmt.setObject(2, value.getAsSqlDate(McardGrZahtjevVo.MCARD_GR_ZAHTJEV__DATUM_OBRADE));
			pstmt.setObject(3, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			j2eevo = new McardGrZahtjevVo(loc_rs);
			pstmt.close();
			return j2eevo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public String daoZadnjiBrojZahtjeva(McardGrZahtjevVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select broj_zahtjeva from view_mcard_gr_zahtjev ");
		sql.append(" where vrijeme_izmjene = '"+value.get("vrijeme_izmjene")+"' ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			McardGrZahtjevVo vo = new McardGrZahtjevVo(j2eers.getRowAt(0));
			return vo.get("broj_zahtjeva");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public McardGrZahtjevRs daoProcitajSifre(McardGrZahtjevVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" SELECT naziv_sifre as name " +
				   " FROM mcard_gr_sifarnik " +
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
			return new McardGrZahtjevRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

    public McardGrZahtjevRs daoIzvjestaji(McardGrZahtjevVo value)  {
    	J2EESqlBuilder sp = new J2EESqlBuilder();
	    int counter = 0;
		sp.append("{call ");
		sp.append("stp_mcard_gr_zahtjev_facade");
		sp.append(" (?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(++counter,value.get("@@report_selected"));
			cs.setObject(++counter,value.get("oib"));
			cs.setObject(++counter,value.get("jmbg"));
			cs.setObject(++counter,value.get("broj_zahtjeva"));
			McardGrZahtjevRs j2eers = new McardGrZahtjevRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

    public OsnovniRs daoFindSvePartijeStednje(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT * FROM dbo.fn_tbl_mcard_gr_stednja_partije(?,getdate()) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get("value"));
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoFindSvePartijeMcard(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT cast(broj_partije as bigint) as broj_partije_mcard FROM BI_PROD.dbo.bi_gr_partija " +
        			 "where broj_partije between 7500000000 and 7699999999 and oib = ?");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get("value"));
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoFindPartijaTekuci(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT top 1 cast(broj_partije as bigint) as broj_partije_tekuceg FROM BI_PROD.dbo.bi_gr_partija " +
        			 "where broj_partije between 3200000000 and 3299999999 and datum_zatvaranja is null and oib = ?");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get("value"));
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFindListu(String sql, String order_by)  {
        if(order_by == null)
            sql = sql +" order by id";
        else
            sql = sql + " ORDER BY " +order_by;
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }