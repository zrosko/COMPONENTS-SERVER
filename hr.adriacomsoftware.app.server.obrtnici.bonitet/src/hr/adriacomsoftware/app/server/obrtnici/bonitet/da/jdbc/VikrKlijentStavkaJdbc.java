package hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.obrasci.dto.VikrStavkaRs;
import hr.adriacomsoftware.app.common.pravneosobe.obrasci.dto.VikrStavkaVo;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class VikrKlijentStavkaJdbc extends PoZahJdbc {
    public VikrKlijentStavkaJdbc() {
        setTableName("po_vikr_klijent_stavka");
    }
    public VikrStavkaRs daoCitajSveVikrZaKlijenta(VikrStavkaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT K.maticni_broj,CONVERT(char(11),CONVERT(decimal(11,0), K.oib)) as oib,bi_prod.dbo.bi_fn_po_naziv_pravne_osobe_maticni_broj(K.maticni_broj) as naziv,");
        sql.appendln("convert(char(15),K.datum_stanja,104) as datum_,K.datum_stanja,");
        sql.appendln("S.id_vikr_stavka,S.id_vikr_klijenta,S.vrsta_iznosa,");
        sql.appendln("S.valuta,S.iznos,S.opis ");
        sql.appendln("FROM po_vikr_klijent_stavka S inner join ");
        sql.appendln("po_vikr_klijent K on K.id_vikr_klijenta = S.id_vikr_klijenta ");
        sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); 
        sql.appendln("ORDER BY K.id_vikr_klijenta, K.datum_stanja, S.valuta ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        VikrStavkaRs j2eers = new VikrStavkaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    } 
}