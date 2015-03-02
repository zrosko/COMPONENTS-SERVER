package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahVikrJdbc extends PoZahJdbc {
    public PoZahVikrJdbc() {
        setTableName("po_zah_vikr");
    }
    public AS2RecordList daoFind(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT VAL.oznaka_valute as valuta, V.postotak_zastite, V.napomena, V.broj_zahtjeva, V.ukupno_obveze,V.sifra_valute, V.izvoznik, V.zastita "+
		" FROM po_zah_vikr AS V LEFT OUTER JOIN"+
		" bi_valuta AS VAL ON V.sifra_valute = VAL.sifra_valute"+
		" where broj_zahtjeva = ? ");
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
    public boolean daoFindIfExists(String broj_zahtjeva, String sifra_valute)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT 1 FROM po_zah_vikr where broj_zahtjeva "+
        " = '" +broj_zahtjeva+"'"+"and sifra_valute = '" +sifra_valute+"'");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        int broj_slogova = j2eers.size();
	        if(broj_slogova > 0)
	            return true;
	        else
	            return false;
        } catch (Exception e) {
        	throw new AS2DataAccessException(e);
		}
    }
 }