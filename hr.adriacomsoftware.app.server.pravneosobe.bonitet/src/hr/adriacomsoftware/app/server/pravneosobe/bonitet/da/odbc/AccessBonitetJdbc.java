package hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.odbc;

import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.services.da.odbc.ODDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class AccessBonitetJdbc extends ODDBJdbc {
    public AccessBonitetJdbc() {
        setTableName("");
    }
    public AS2RecordList daoETLDokumentPodatak(PravnaOsobaVo value)  {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DokumentID as id_dokumenta,Kolona,polje,vrijednost FROM tblDokumentPodaci ");
		sql.append("WHERE DokumentID IN ( select DokumentID FROM tblDokument ");
		sql.append("WHERE PoduzeceID in (select PoduzeceID from tblPoduzeca ");
		sql.append("WHERE MaticniBroj = '" + value.getMaticniBroj() + "'))");
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
    public AS2RecordList daoETLDokument(PravnaOsobaVo value)  {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DokumentID as id_dokumenta, PoduzeceID as id_poduzeca, VrstaDokumenta as id_vrste,datum ");
		sql.append("FROM tblDokument WHERE PoduzeceID in (select PoduzeceID from tblPoduzeca ");
		sql.append("WHERE MaticniBroj = '" + value.getMaticniBroj() + "')");
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
    public AS2RecordList daoETLPoduzece(PravnaOsobaVo value)  {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT poduzeceid as id_poduzeca, NazivPoduzeca as naziv, OblikVlasnistva as oblik_vlasnistva,");
		sql.append("MaticniBroj as maticni_broj, MBS, Velicina as velicina_poduzeca,Kontakt as kontakt_osoba,");
		sql.append("RadnoMjesto as radno_mjesto, Telefon, Fax, Mobitel, Email, Web, Sjediste as mjesto,");
		sql.append("PostanskiBroj as postanski_broj, Adresa as ulica, Opcina, Zupanija as sifra_zupanije,");
		sql.append("Sifra as sifra_djelatnosti, Referent, Podruznica  FROM  tblPoduzeca ");
		sql.append("WHERE MaticniBroj = ? ");// '"+value.getMaticniBroj()+"'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}