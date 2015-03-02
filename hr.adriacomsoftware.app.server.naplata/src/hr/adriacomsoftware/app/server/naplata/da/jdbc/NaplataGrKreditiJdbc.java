package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrKreditiRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrKreditiVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class NaplataGrKreditiJdbc extends TESTNaplataJdbc {
    public NaplataGrKreditiJdbc() {
        setTableName("naplata_gr_krediti_predmet");
    }

    public NaplataGrKreditiRs daoFindKreditiPromet(NaplataGrKreditiVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " from view_naplata_gr_krediti_pogled " +
				   " order by broj_dana_kasnjenja desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			NaplataGrKreditiRs j2eers = new  NaplataGrKreditiRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
			
	}
	  public NaplataGrKreditiRs daoIzvjestaji(NaplataGrKreditiVo value)  {
	    J2EESqlBuilder sp = new J2EESqlBuilder();	
	    int counter = 0;
		sp.append("{call ");
		sp.append("stp_naplata_gr_krediti_facade");
		sp.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(++counter, value.get("@@report_selected"));
			cs.setObject(++counter, value.get("status_predmeta"));
			cs.setObject(++counter, value.get("javni_biljeznik"));
			cs.setObject(++counter, value.get("sud"));
			cs.setObject(++counter, value.get("referent"));
			cs.setObject(++counter, value.get("broj_partije"));
			cs.setDate(++counter, value.getAsSqlDate("datum_ssp"));
			cs.setObject(++counter, value.get("koristi_alt_adresu"));
			cs.setDate(++counter, value.getAsSqlDate("datum_izdavanja"));
			cs.setDate(++counter, value.getAsSqlDate("datum_zahtjeva"));
			cs.setObject(++counter, value.get("namjena_izvjestaja"));
			cs.setObject(++counter, value.get("dopuna_izvjestaja"));
			cs.setObject(++counter, value.get("lista_nasljednika"));
			cs.setObject(++counter, value.get("id_biljeske"));
			NaplataGrKreditiRs j2eers = new NaplataGrKreditiRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 

	public NaplataGrKreditiRs daoIzvjestajReferat(NaplataGrKreditiVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from oltp_prod.dbo.fn_tbs_naplata_gr_ssp_referat("
				+ value.get("broj_partije")
				+ ",'"
				+ value.get("datum_ssp")
				+ "') " + " order by rb,vrsta,datum_dogadaja,id_biljeske ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			NaplataGrKreditiRs j2eers = new NaplataGrKreditiRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }