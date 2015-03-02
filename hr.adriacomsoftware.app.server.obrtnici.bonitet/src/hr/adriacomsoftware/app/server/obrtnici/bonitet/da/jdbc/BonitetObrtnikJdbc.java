package hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc;

import hr.adriacomsoftware.app.common.obrtnici.bonitet.dto.BonitetObrtnikRs;
import hr.adriacomsoftware.app.common.obrtnici.bonitet.dto.BonitetObrtnikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class BonitetObrtnikJdbc extends RIZIKJdbc {
    public BonitetObrtnikJdbc() {
        setTableName("bonitet_obrtnik");
    }
    public static short VRSTA_SVE = 0;
    public static short VRSTA_TRAZI_SVE = 1;
    public static short VRSTA_TRAZI_OBRTNIKA = 2;
    public BonitetObrtnikRs daoPronadiBonitete(BonitetObrtnikVo value, short vrsta) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT maticni_broj,CONVERT(char(11),CONVERT(decimal(11,0), oib)) as oib,bi_prod.dbo.bi_fn_po_naziv_pravne_osobe_maticni_broj(maticni_broj) as naziv,");
        sql.appendln("convert(char(15),datum,104) as datum_,ukupni_primici,ukupni_izdaci,dohodak,napomena,broj_zaposlenih,");
        sql.appendln("broj_dana_blokade,opis,ispravno,id_boniteta,opis,vrijeme_zadnje_izmjene,");
        sql.appendln("poslovne_banke, pocetak_poslovnog_odnosa, godina_pocetka_poslovanja,datum, ");
        sql.appendln("CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(954) as isms_povjerljivost ");
        sql.appendln("FROM bonitet_obrtnik ");
        if(vrsta == VRSTA_TRAZI_SVE){
            sql.appLike("AND", " bi_prod.dbo.bi_fn_po_naziv_pravne_osobe_maticni_broj(maticni_broj) ", value.getImePrezime()); 
            sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); 
            sql.appEqualNoQuote("AND", "oib", value.getOib()); 
        }else if(vrsta == VRSTA_TRAZI_OBRTNIKA){
            sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); 
        }
        sql.appendln("ORDER BY maticni_broj, datum desc");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        BonitetObrtnikRs j2eers = new BonitetObrtnikRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    } 
	public boolean daoFindIfExists(BonitetObrtnikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT datum FROM bonitet_obrtnik ");
		sql.append(" WHERE datum = ? AND id_boniteta <> ? AND (maticni_broj = ? )" );// OR oib = ?
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(BonitetObrtnikVo.BONITET_OBRTNIK__DATUM));
	        pstmt.setObject(2, value.getIdBoniteta());
	        pstmt.setObject(3, value.getMaticniBroj());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}