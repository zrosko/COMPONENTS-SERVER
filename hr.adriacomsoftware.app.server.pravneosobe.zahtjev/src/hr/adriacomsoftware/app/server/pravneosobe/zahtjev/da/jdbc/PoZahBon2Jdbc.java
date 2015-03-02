package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.Bon2Rs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.Bon2Vo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahBon2Jdbc extends PoZahJdbc {
    public PoZahBon2Jdbc() {
        setTableName("po_zah_bon2");
    }
    public AS2RecordList daoFind(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
   		sql.append("select BI_PROD.dbo.bi_fn_banka_vbdi_naziv(naziv_banke) as banka,CONVERT(char(10), razdoblje_od,104) as razdoblje_pocetak, "+
		"CONVERT(char(10), razdoblje_do,104) as razdoblje_kraj,"+
		"CONVERT(decimal(18,0),broj_partije)as broj_partije,id_bon2, broj_zahtjeva, naziv_banke, razdoblje_od, razdoblje_do,"+
		"iznos_primitaka, iznos_prosjecnog_stanja, stanje_racuna, broj_dana_blokade, neprekidna_blokada,nepodmirene_obveze FROM "+ 
		getTableName()+"  where broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList mmrs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return mmrs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public Bon2Rs daoListaSvihBon2Podataka(Bon2Vo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT 2411006 as naziv_banke, broj_partije, razdoblje_od, ");
        sql.appendln("razdoblje_do, iznos_primitaka,iznos_prosjecnog_stanja, stanje_racuna,");
        sql.appendln("broj_dana_blokade,nepodmirene_obveze FROM BI_PROD.dbo.bi_fn_po_prosjek_stanja_BON2(?,?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,value.getBrojDana());
	        pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_BON2__RAZDOBLJE_DO));
	        pstmt.setMaxRows(0);
	        Bon2Rs j2eers = new Bon2Rs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }