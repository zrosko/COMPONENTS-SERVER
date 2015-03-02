package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentBiljeskaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentBiljeskaVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataKlijentBiljeskaJdbc extends OLTPJdbc {
    public NaplataKlijentBiljeskaJdbc() {
        setTableName("naplata_klijent_biljeska");
    }
    public NaplataKlijentBiljeskaRs daoFind(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if(value.getVrsta().equals("POVIJEST SSP")){
			sql.append(" select bi_prod.dbo.fnFormatDate(datum_ulaska,'dd.MM.yyyy') as datum_ulaska_, " +
					   " bi_prod.dbo.fnFormatDate(datum_izlaska,'dd.MM.yyyy') as datum_izlaska_, " +
					   " operater_ulaska, operater_izlaska, datum_ulaska, datum_izlaska ");
   			sql.append(" from bi_prod.dbo.bi_gr_tekuci_ssp_arhiva ");
			sql.append(" where broj_partije = "+value.get("broj_partije")+" order by datum_ulaska desc ");
		}else{
			sql.append(" select *,broj_ovrhe as broj_ovrhe_stari   ");
			sql.append(" FROM view_naplata_klijent_biljeska ");
			if(!value.get("tip_vrste").equals("pojedinačna")){
				sql.append(" where oib = "+value.getOib()+" order by datum_dogadaja desc ");
			}else{
				sql.append(" where broj_partije = "+value.get("broj_partije")+" and datum_ssp = '"+value.get("datum_ssp")+
						   "' and vrsta = '"+value.getVrsta()+"' and za_sluzbu = '" +value.get("za_sluzbu")+
						   "'  ");
				if(value.get("vrsta").equals("POŽURNICA_DOPIS"))
					sql.append(" and id_biljeske_veza = "+value.get("id_biljeske_glavni"));
				if(value.get("vrsta").equals("TROŠKOVI OVRHE"))
					sql.append(" order by datum_uplate desc ");
				else
					sql.append(" order by datum_dogadaja desc, vrijeme_zadnje_izmjene desc ");
			}
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentBiljeskaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentBiljeskaRs daoFindIzvjesceSkupno(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select *  ");
		sql.append(" FROM view_naplata_klijent_izvjesce_skupno where vrsta in (");
		sql.append(value.getVrsta());
		sql.append(" ) ORDER BY vrsta, naziv, datum_dogadaja");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentBiljeskaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.naplata_klijent_biljeska SET ispravno = 0 " +
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

    public String daoZadnjiIdBiljeske(NaplataKlijentBiljeskaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_biljeske from dbo.naplata_klijent_biljeska ");
		sql.append(" where id_temp = '"+value.get("id_temp")+"' and isnull(ispravno,1)=1 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			NaplataKlijentBiljeskaVo vo = new NaplataKlijentBiljeskaVo(j2eers.getRowAt(0));
			return vo.get("id_biljeske");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentBiljeskaRs daoProcitajMogucePotpisnikeNagodbe(NaplataKlijentBiljeskaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select name from(" +
				   "	select naziv as name,1 as rb,broj_partije,datum_ssp " +
				   "	from OLTP_PROD.dbo.fn_gr_naplata_pogled(0) n " +
				   "	UNION " +
				   "	select naziv_nasljednika as name,2 as rb,broj_partije,datum_ssp " +
				   "	from OLTP_PROD.dbo.view_naplata_klijent_biljeska b " +
				   "	where b.vrsta = 'NASLJEDNICI' and b.za_sluzbu = '" +value.get("za_sluzbu")+"' " +
				   ")a " +
				   "where broj_partije = "+value.get("broj_partije")+
				   " and datum_ssp='"+value.get("datum_ssp")+"'"+
				   "order by broj_partije,datum_ssp,rb,name ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentBiljeskaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentBiljeskaRs daoProcitajSifre(NaplataKlijentBiljeskaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" SELECT naziv_sifre as name " +
				   " FROM naplata_klijent_sifarnik " +
				   " WHERE vrsta = '"+value.get("vrsta")+"'" +
				   " ORDER BY rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentBiljeskaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}