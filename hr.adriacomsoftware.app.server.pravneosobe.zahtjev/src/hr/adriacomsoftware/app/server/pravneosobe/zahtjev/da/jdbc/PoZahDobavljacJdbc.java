package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahDobavljacJdbc extends PoZahJdbc {
    public PoZahDobavljacJdbc() {
        setTableName("po_zah_dobavljac");
    }
    public AS2RecordList daoFind(AS2Record value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select naziv, prethodna_promet, prethodna_saldo, tekuca_promet, tekuca_saldo, id_dobavljaca,broj_zahtjeva, strani,  ");
		sql.append("prethodna_postotak_prometa,tekuca_postotak_prometa, prethodna_saldo_ukupno, prethodna_promet_ukupno, tekuca_saldo_ukupno,");
		sql.append("tekuca_promet_ukupno,datum_analize, tekuca_godina, prethodna_godina ");
		sql.append(" FROM dbo.fn_po_zah_dobavljaci (?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoRemoveSveDobavljaceZaZahtjev(DobavljacVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_dobavljac WHERE broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoRemoveUcitaneDobavljace(DobavljacVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_dobavljac WHERE broj_zahtjeva = ? ");
		sql.append(" and isnull(ucitano,0) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public DobavljacVo daoFindZadnjiBrojZahtjeva(DobavljacVo value)  {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TOP (1) ZA.broj_zahtjeva  ");
		sql.append("FROM po_zah_dobavljac AS KP LEFT OUTER JOIN ");
		sql.append("po_zah_podaci_klijenta AS PK ON KP.broj_zahtjeva = PK.broj_zahtjeva LEFT OUTER JOIN ");
		sql.append("po_zah_zahtjev AS ZA ON PK.broj_zahtjeva = ZA.broj_zahtjeva ");
		sql.append("WHERE (PK.maticni_broj = ? AND KP.broj_zahtjeva <> ?)");
		sql.append("ORDER BY ZA.broj_zahtjeva DESC"); //uzimamo samo jedna red i to zadnji iz baze
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());		
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,value.getBrojZahtjeva());
			pstmt.setMaxRows(1);
			DobavljacVo j2eers = new DobavljacVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }