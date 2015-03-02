package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.Bon2UpitRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.Bon2UpitVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PartijaPravnihOsobaJdbc extends J2EEDataAccessObjectJdbc {
    public PartijaPravnihOsobaJdbc() {
        setTableName("bi_po_partija");
    }
	public boolean daoFindIfExists(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT broj_partije FROM bi_po_partija WHERE (broj_partije = ?) ");
        int counter = 1;
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(counter, value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		} 
    } 
    public PartijaRs daoSaldoSvihPartijaPravneOsobe(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT broj_partije_char as broj_partije, tip_racuna, ");
        sql.appendln("datum_otvaranja_char as datum_otvaranja, naziv_organizacijske_jedinice as jedinica, saldo ");
        sql.appendln("FROM dbo.bi_fn_po_partija_sve_saldo(?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,null);
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    
    public PartijaRs daoListaSvihKredita(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT distinct broj_partije_char as broj_partije, naziv_programa as program,valuta, ");
        sql.appendln("protuvaluta,datum_odobravanja_char as datum_odobravanja, datum_dospijeca_char as datum_dospijeca,");
        sql.appendln("odobreni_iznos, kamatna_stopa, nedospjela_glavnica, dospjela_glavnica, dospjela_kamata,");
        sql.appendln("naziv_organizacijske_jedinice as org_jedinica,broj_dana_kasnjenja_glavnica, broj_dana_kasnjenja_kamate, rok_otplate ");
        sql.appendln("FROM dbo.fn_tbl_po_partija_krediti(?,?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,value.getOib());
	        pstmt.setObject(3,null);
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoListaSvihUgovora(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_ugovori(?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,null);
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoListaSvihDepozita(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_partija_devize(?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,null);
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public Bon2UpitRs daoListaSvihBon2Podataka(Bon2UpitVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_prosjek_stanja_BON2(?,?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,value.getBrojDana());
	        pstmt.setDate(3,value.getAsSqlDate(Bon2UpitVo.BON2_UPIT__DATUM));
	        pstmt.setMaxRows(0);
	        Bon2UpitRs j2eers = new Bon2UpitRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoBonitetPravneOsobe(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("SELECT * FROM dbo.bi_fn_po_BONITET(?,?)");
        sql.append("order by pripadnost,broj_konta");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoFindListaOtvorenihPartijaPravneOsobe(PravnaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("SELECT *, convert(decimal(15,0),broj_partije) as broj_partije_ FROM bi_po_partija WHERE datum_zatvaranja is null and maticni_broj = ? ");
        int counter = 1;
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(counter, value.getMaticniBroj());
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public boolean daoFindPartijaPravneOsobe(PravnaOsobaVo value)  {
    	 J2EESqlBuilder sql = new J2EESqlBuilder();
    	 sql.append("SELECT broj_partije FROM bi_po_partija WHERE maticni_broj = ? and broj_partije = ? ");
    	 try{
    		PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getMaticniBroj());
	        pstmt.setObject(2, value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoListaSvihIzlozenosti(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT vrsta_duga,convert(char(10),convert(decimal(10,0),broj_partije)) as broj_partije, ");
        sql.appendln("odobreni_iznos,kamatna_stopa,nedospjela_glavnica,dospjela_glavnica,dospjela_kamata,nedospjela_kamata, ");
        sql.appendln("datum_odobravanja_char,datum_dospijeca_char,valuta,protuvaluta,broj_dana_kasnjenja ");
        sql.appendln("FROM fn_tbl_po_partija_pravna_osoba(?,?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,null);
	        pstmt.setObject(3,null);//TODO datum u budućnosti
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();  
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    //prebaceno iz po_zah PoZahZaduzenostKodBankeJdbc
    public PartijaRs daoListaSvihZaduzenosti(ZaduzenostKodBankeVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT distinct * ");
        sql.appendln("FROM dbo.fn_tbl_po_partija_pravna_osoba(?,?,?)");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setObject(2,null); //oib
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoListaSvihOstalihZaduzenja(ZaduzenostKodBankeVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT maticni_broj, broj_partije, broj_partije_char, vrsta_duga, ");
        sql.appendln("SUM(odobreni_iznos) AS odobreni_iznos, SUM(nedospjela_glavnica) AS nedospjela_glavnica, ");
        sql.appendln("SUM(dospjela_kamata) AS dospjela_kamata, SUM(neiskoristeni_iznos) AS neiskoristeni_iznos ");
        sql.appendln("FROM dbo.bi_fn_po_partija_ostalo(?,?) ");
        sql.appendln("GROUP BY maticni_broj, broj_partije, broj_partije_char, vrsta_duga ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}