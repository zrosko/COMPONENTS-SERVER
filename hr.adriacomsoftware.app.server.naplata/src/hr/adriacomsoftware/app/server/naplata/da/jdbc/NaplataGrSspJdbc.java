package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class NaplataGrSspJdbc extends OLTPJdbc {
    public NaplataGrSspJdbc() {
        setTableName("naplata_gr_ssp");
    }

	public NaplataGrSspRs daoLoadSSP(NaplataGrSspVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from fn_tbs_naplata_gr_ssp_pogled(0) "
				+ " where partija_zatvorena = 'NE' and datum_izlaska is null "
				+ " order by partija_zatvorena desc, status_partije desc, "
				+ " datum_ulaska desc, datum_izlaska desc, naziv ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrSspRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public NaplataGrSspRs daoFindSSP(NaplataGrSspVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from fn_tbs_naplata_gr_ssp_pogled("
				+ value.getAsLong("broj_partije") + ") ");
		if (value.get("_refresh").equals("true")) {
			sql.appEqualNoQuote("AND", "broj_partije",
					value.get("broj_partije"));
			sql.appEqual("AND", "datum_ssp", value.get("datum_ssp"));
		} else {
			if (value.get("oib").length() > 0) {
				sql.appEqual("AND", "(oib", value.get("oib"));
				sql.appEqual("OR", "jmbg", value.get("oib"));
				sql.append(")");
			}
			sql.appLike("AND", "naziv", value.get("naziv"));
			sql.appInNoQuote("AND", value.get("broj_partije"),
					"broj_partije,broj_partije_vezane,broj_partije_izuzeti_priljevi");
			sql.appGreatherOrEqual("AND", "datum_ssp",
					value.get("datum_ssp_od"));
			sql.appLessOrEqual("AND", "datum_ssp", value.get("datum_ssp_do"));
			sql.appEqual("AND", "datum_ssp", value.get("datum_ssp"));
			sql.appGreatherOrEqual("AND", "datum_izlaska",
					value.get("datum_izlaska_od"));
			sql.appLessOrEqual("AND", "datum_izlaska",
					value.get("datum_izlaska_do"));
			if (!value.get("broj_ovrhe_prefiks").equals(" "))
				sql.appEqual("AND", "broj_ovrhe_prefiks",
						value.get("broj_ovrhe_prefiks"));
			sql.appEqual("AND", "broj_ovrhe_broj", value.get("broj_ovrhe_broj"));
			sql.appLike("AND", "broj_ovrhe_godina",
					value.get("broj_ovrhe_godina"));

			sql.append(" order by partija_zatvorena desc, status_partije desc, "
					+ " datum_ulaska desc, datum_izlaska desc ");
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrSspRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public NaplataGrSspRs daoIzvjestaji(NaplataGrSspVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_naplata_gr_ssp_facade");
		sp.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++counter, value.getProperty(J2EEDataDictionary.DATUM_OD));
			cs.setObject(++counter, value.getProperty(J2EEDataDictionary.DATUM_DO));
			cs.setObject(++counter, value.getProperty(J2EEDataDictionary.DATUM));
			cs.setObject(++counter, value.getProperty("@@report_selected"));
			cs.setObject(++counter, value.getProperty("status_predmeta"));
			cs.setObject(++counter, value.getProperty("javni_biljeznik"));
			cs.setObject(++counter, value.getProperty("sud"));
			cs.setObject(++counter, value.getProperty("referent"));
			cs.setObject(++counter, value.getProperty("broj_partije"));
			cs.setObject(++counter, value.getProperty("datum_ssp"));
			cs.setObject(++counter, value.getProperty("koristi_alt_adresu"));
			cs.setObject(++counter, value.getProperty("datum_izdavanja"));
			cs.setObject(++counter, value.getProperty("datum_zahtjeva"));
			cs.setObject(++counter, value.getProperty("namjena_izvjestaja"));
			cs.setObject(++counter, value.getProperty("dopuna_izvjestaja"));
			cs.setObject(++counter, value.getProperty("lista_nasljednika"));
			cs.setObject(++counter, value.getProperty("id_biljeske"));
			cs.setObject(++counter, value.getProperty("broj"));
			NaplataGrSspRs j2eers = new NaplataGrSspRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 

	public NaplataGrSspRs daoIzvjestajReferat(NaplataGrSspVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from oltp_prod.dbo.fn_tbs_naplata_gr_ssp_referat("
				+ value.get("broj_partije")
				+ ",'"
				+ value.get("datum_ssp")
				+ "') " + " order by rb,vrsta,datum_dogadaja,id_biljeske ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrSspRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public NaplataGrSspRs daoFindBiljeska(NaplataGrSspVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getVrsta().equals("POVIJEST SSP")) {
			sql.append(" select bi_prod.dbo.fnFormatDate(datum_ulaska,'dd.MM.yyyy') as datum_ulaska_, "
					+ " bi_prod.dbo.fnFormatDate(datum_izlaska,'dd.MM.yyyy') as datum_izlaska_, "
					+ " operater_ulaska, operater_izlaska, datum_ulaska, datum_izlaska ");
			sql.append(" from bi_prod.dbo.bi_gr_tekuci_ssp_arhiva ");
			sql.append(" where broj_partije = " + value.get("broj_partije")
					+ " order by datum_ulaska desc ");
		}else if (value.getVrsta().equals("POVIJEST BLOKADA")) {
			sql.append(" select cast(broj_partije as bigint) as broj_partije_,bi_prod.dbo.bi_fn_gr_osoba_ime_prezime(broj_partije) as naziv, "
					+ " convert(varchar(10),datum_blokade_deblokade,104) as datum_blokade_, "
					+ " convert(varchar(10),datum_deblokade,104) as datum_deblokade_, "
					+ " operater_izmjene ");
			sql.append(" from bi_prod.dbo.bi_blokade ");
			sql.append(" where broj_partije = " + value.get("broj_partije")
					+ " order by datum_blokade_deblokade desc ");
		} else {
			sql.append(" select *,broj_ovrhe as broj_ovrhe_stari   ");
			sql.append(" FROM view_naplata_gr_ssp ");
			sql.append(" where broj_partije = " + value.get("broj_partije")
					+ " and datum_ssp = '" + value.get("datum_ssp")
					+ "' and vrsta = '" + value.getVrsta()
					+ "' and za_sluzbu = '" + value.get("za_sluzbu")
					+ "'  ");
			if (value.get("vrsta").equals("POŽURNICA_DOPIS"))
				sql.append(" and id_biljeske_veza = " + value.get("id_biljeske_glavni"));
			if (value.get("vrsta").equals("OVRHA")&& value.get("id_biljeske") != null && value.get("id_biljeske").length()>0){
				sql.append(" and id_biljeske = " + value.get("id_biljeske"));
				sql.append(" order by datum_uplate desc ");
			}else
				sql.append(" order by datum_dogadaja desc, vrijeme_zadnje_izmjene desc ");
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
				return new NaplataGrSspRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	
	public NaplataGrSspVo daoLoadBiljeska(NaplataGrSspVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select *,broj_ovrhe as broj_ovrhe_stari   ");
		sql.append(" FROM view_naplata_gr_ssp ");
		sql.append(" where id_biljeske = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdBiljeske());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrSspVo j2eevo = new NaplataGrSspVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	
	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.naplata_gr_ssp SET ispravno = 0 " +
				   "WHERE id_biljeske = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_biljeske"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

	public String daoZadnjiIdBiljeske(NaplataGrSspVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_biljeske from dbo.naplata_gr_ssp ");
		sql.append(" where id_temp = '" + value.get("id_temp")
				+ "' and isnull(ispravno,1)=1 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			NaplataGrSspVo vo = new NaplataGrSspVo(j2eers.getRowAt(0));
			return vo.get("id_biljeske");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	   	    
	    public NaplataGrSspRs daoProcitajMogucePotpisnikeNagodbe(NaplataGrSspVo value)  {
			J2EESqlBuilder sql = new J2EESqlBuilder();
			sql.append(" select name from(" +
					   "	select naziv as name,1 as rb,broj_partije,datum_ssp " +
					   "	from OLTP_PROD.dbo.fn_tbs_naplata_gr_ssp_pogled(0) n " +
					   "	UNION " +
					   "	select naziv_nasljednika as name,2 as rb,broj_partije,datum_ssp " +
					   "	from OLTP_PROD.dbo.view_naplata_gr_ssp b" +
					   "	where b.vrsta = 'NASLJEDNICI' and b.za_sluzbu = '" +value.get("za_sluzbu")+"' " +
					   ")a " +
					   "where broj_partije = "+value.get("broj_partije")+
					   " and datum_ssp='"+value.get("datum_ssp")+"'"+
					   "order by broj_partije,datum_ssp,rb,name ");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				NaplataGrSspRs j2eers = new NaplataGrSspRs(transformResultSet(pstmt.executeQuery()));
				pstmt.close();
				return j2eers;
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
	    public NaplataGrSspRs daoProcitajSifre(NaplataGrSspVo value)  {
			J2EESqlBuilder sql = new J2EESqlBuilder();
			sql.append(" SELECT naziv_sifre as name,* " +
					   " FROM naplata_gr_ssp_sifarnik " +
					   " WHERE vrsta = '"+value.get("vrsta")+"'" +
					   " ORDER BY rb ");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				NaplataGrSspRs j2eers = new NaplataGrSspRs(transformResultSet(pstmt.executeQuery()));
				pstmt.close();
				return j2eers;
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
	    public NaplataGrSspRs daoFindTroskoviOvrhe(NaplataGrSspVo value) {
			J2EESqlBuilder sql = new J2EESqlBuilder();
			if(value.get("@kratki").equals("true"))
				sql.append(" select id_biljeske, "+value.get("id_biljeske_veza")+" as id_biljeske_veza ");
			else
				sql.append(" select *,broj_ovrhe as broj_ovrhe_stari   ");
			sql.append(" from view_naplata_gr_ssp ");
			sql.append(" where broj_partije = " + value.get("broj_partije")+
					   " and datum_ssp = '" + value.get("datum_ssp")+
					   "' and vrsta = '" + value.getVrsta()+
					   "' and za_sluzbu = '" + value.get("za_sluzbu")+
					   "'  ");
			if (value.get("@novo").equals("true")){
				sql.append(" and isnull(id_biljeske_veza,0) = 0 ");
			}else{
				sql.append(" and id_biljeske_veza = " + value.get("id_biljeske_veza"));
			}
			sql.append(" order by datum_uplate desc ");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				NaplataGrSspRs j2eers = new NaplataGrSspRs(transformResultSet(pstmt.executeQuery()));
				pstmt.close();
				return j2eers;
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}

	public NaplataGrSspVo daoPripremiDopis(NaplataGrSspVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.get("id_sifre").equals("dopis_prijedlog_za_ovrhu")) {
			sql.append(" select aktivna_, isnull(javni_biljeznik,sud) as primatelj "
					+ "FROM view_naplata_gr_ssp ");
			sql.append(" where broj_partije = " + value.get("broj_partije")
					+ " and datum_ssp = '" + value.get("datum_ssp") + "' "
					+ " and vrsta = 'OVRHA' " + " and za_sluzbu = '"
					+ value.get("za_sluzbu") + "' "
					+ " and aktivna_= 'Da' ");
		} else
			return null;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2Record loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrSspVo j2eers = new NaplataGrSspVo(loc_rs);
			pstmt.close();
			if (value.get("id_sifre").equals("dopis_prijedlog_za_ovrhu")) {
				if (!j2eers.get("aktivna_").equals("Da"))
					j2eers.set(
							"nova_napomena",
							"<html><font color='red' size=4><b>* </font><span><font size=2>Molim prvo unesite ovrhu!");
			}
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }