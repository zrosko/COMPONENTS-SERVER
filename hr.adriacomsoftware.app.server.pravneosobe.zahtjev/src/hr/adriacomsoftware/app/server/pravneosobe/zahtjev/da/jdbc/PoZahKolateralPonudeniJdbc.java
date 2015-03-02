package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KolateralPonudeniRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KolateralPonudeniVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahKolateralPonudeniJdbc extends PoZahJdbc {
    public PoZahKolateralPonudeniJdbc() {
        setTableName("po_zah_kolateral_ponudeni");
    }
    public AS2RecordList daoFind(AS2Record value) 	{
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
    	sql.append("select * FROM dbo.fn_po_zah_ponudeni_kolaterali(?,?) ");
    	try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_KOLATERAL_PONUDENI__ID_BANKARSKOG_PROIZVODA));
			pstmt.setObject(2,value.get(ZAHDataDictionary.PO_ZAH_KOLATERAL_PONUDENI__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoRemoveSveKolateraleZaProizvod(KolateralPonudeniVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_kolateral_ponudeni WHERE broj_zahtjeva = ? and id_bankarskog_proizvoda = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setObject(2, value.get(ZAHDataDictionary.PO_ZAH_BANKARSKI_PROIZVOD__ID_BANKARSKOG_PROIZVODA));
	        pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoRemoveUcitaneKolateraleZaProizvod(KolateralPonudeniVo value, int ucitano_) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_kolateral_ponudeni WHERE broj_zahtjeva = ? AND id_bankarskog_proizvoda = ? ");
		if(ucitano_==1)
		    sql.append(" AND isnull(ucitano,0) = 1");//KOL
		else if(ucitano_==2)
		    sql.append(" AND isnull(ucitano,0) = 2");//ZAH
		else if(ucitano_==3)
		    sql.append(" AND isnull(ucitano,0) = 3");//BSA
		else 
		    sql.append(" AND isnull(ucitano,0) = 1");//KOL -- default
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setObject(2, value.get(ZAHDataDictionary.PO_ZAH_BANKARSKI_PROIZVOD__ID_BANKARSKOG_PROIZVODA));
	        pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public KolateralPonudeniVo daoFindZadnjiBrojZahtjeva(KolateralPonudeniVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT TOP (1) ZA.broj_zahtjeva  ");
		sql.append("FROM po_zah_kolateral_ponudeni AS KP LEFT OUTER JOIN ");
		sql.append("po_zah_podaci_klijenta AS PK ON KP.broj_zahtjeva = PK.broj_zahtjeva LEFT OUTER JOIN ");
		sql.append("po_zah_zahtjev AS ZA ON PK.broj_zahtjeva = ZA.broj_zahtjeva ");
		sql.append("WHERE (PK.maticni_broj = ? AND KP.broj_zahtjeva <> ?)");
		sql.append("ORDER BY ZA.broj_zahtjeva DESC"); //uzimamo samo jedna red i to zadnji iz baze
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,value.getBrojZahtjeva());
			pstmt.setMaxRows(1);
			KolateralPonudeniVo j2eers = new KolateralPonudeniVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public KolateralPonudeniRs daoFindForZahtjev(KolateralPonudeniVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select * FROM po_zah_kolateral_ponudeni where broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_KOLATERAL_PONUDENI__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			KolateralPonudeniRs j2eers = new KolateralPonudeniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }