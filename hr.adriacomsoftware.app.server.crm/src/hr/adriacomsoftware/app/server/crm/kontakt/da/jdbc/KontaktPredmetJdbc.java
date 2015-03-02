package hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPredmetRs;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPredmetVo;
import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class KontaktPredmetJdbc extends KontaktJdbc {
	//TODO brisati
	public static final String LISTA_BLOKADA_PO_SQL = "select * from dbo.view_po_status_potrazivanja_201 order by datum_blokade desc";

	public KontaktPredmetJdbc() {
		setTableName("kontakt_predmet");
	}

	public KontaktPredmetRs daoFindListuPredmeta(KontaktPredmetVo value) {
		StringBuffer sp = new StringBuffer();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_crm_kontakt_predmet");
		sp.append(" (?,?,?,?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++counter, value.getKategorija());
			cs.setObject(++counter, value.getAsObject("@pretrazivanje"));
			cs.setDate(++counter, value.getAsSqlDate("datum_otvaranja_od"));
			cs.setDate(++counter, value.getAsSqlDate("datum_otvaranja_do"));
			cs.setDate(++counter, value.getAsSqlDate("datum_zatvaranja_od"));
			cs.setDate(++counter, value.getAsSqlDate("datum_zatvaranja_do"));
			cs.setObject(++counter, value.getIdPredmeta());
			cs.setObject(++counter, value.getOib());
			cs.setObject(++counter, value.getNaziv());
			cs.setObject(++counter, value.get("maticni_broj"));
			cs.setObject(++counter, value.get("broj_partije"));
			cs.setObject(++counter, value.get("trazi_sve").replaceAll(" ", "%"));
			KontaktPredmetRs j2eers = new KontaktPredmetRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public OsnovniRs daoProcitajSifre(OsnovniVo value) {
	    StringBuffer sp = new StringBuffer();	
	    int counter = 0;
		sp.append("{call ");
		sp.append("stp_crm_kontakt_sifrarnik");
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++counter,value.get("kategorija"));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoIzvjestaji(OsnovniVo value) {
		StringBuffer sp = new StringBuffer();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_crm_" + value.get("kategorija") + "_facade");
		sp.append(" (?,?,?,?,?,?,?,?,?,?,?,?) }");
		try {
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(++counter,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(++counter, value.get("@@report_selected"));
			cs.setObject(++counter, value.get("oib"));
			cs.setObject(++counter, value.get("jmbg"));
			cs.setObject(++counter, value.get("koristi_alt_adresu"));
			cs.setObject(++counter, value.get("broj_partije"));
			cs.setObject(++counter, value.get("id_blokade"));
			cs.setObject(++counter, value.get("id_predmeta"));
			cs.setObject(++counter, value.get("datum_izdavanja"));
			cs.setObject(++counter, value.get("id_zapljene"));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	 public AS2RecordList daoFindListuBlokadaPo(KontaktPredmetVo value)  {
		 String sql = "select * from dbo.view_po_status_potrazivanja_201 order by datum_blokade desc";
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