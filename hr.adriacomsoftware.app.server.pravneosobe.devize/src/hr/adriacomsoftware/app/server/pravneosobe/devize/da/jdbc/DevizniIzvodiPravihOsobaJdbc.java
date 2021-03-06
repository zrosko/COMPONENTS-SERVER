package hr.adriacomsoftware.app.server.pravneosobe.devize.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.dto.IzvodRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.IzvodVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class DevizniIzvodiPravihOsobaJdbc extends OLTPJdbc {
    public DevizniIzvodiPravihOsobaJdbc() {
        setTableName("bi_po_devize_izvjestaj_izvod");
    }
    public IzvodRs daoListaIzvodaDevizni(IzvodVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();                      
        if(value.getAkcija().equals("Prikazi")){
            sql.appendln("select * from bi_view_po_devize_izvod "); 
            sql.appendWhere();
            sql.appPrefix();
            sql.append(" WHERE datum_pocetak between ? and ? "); 
            sql.appLike("AND", "naziv", value.getNaziv()); 
            sql.appEqual("AND", "maticni_broj", value.getMaticniBroj());
            sql.appIn("AND", "broj_izvoda", value.getBrojIzvoda());
            sql.appEqual("AND", "broj_konta", value.getBrojKonta());
            /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
            sql = odrediRazinuOvlasti(sql);
            /*ograničavanje za sigurnosne razine KRAJ*/
            sql.appendln(" order by datum_pocetak, maticni_broj, broj_konta, valuta_racuna_oznaka, id_izvoda ");  
		}else if(value.getAkcija().equals("Priprema")){			    
		    daoCallStoredProcedureIzvodiDevizni(value);
		    return new IzvodRs();
		}else if(value.getAkcija().equals("Koverte")){
		    sql.appendln("select * from ????? ");  //ne koristi se
		    sql.appendln(" WHERE datum_pocetak = ? and datum_kraj = ?"); 
		} 
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(JBDataDictionary.BI_IZVOD__DATUM_POCETAK)); 
	        pstmt.setDate(2,value.getAsSqlDate(JBDataDictionary.BI_IZVOD__DATUM_KRAJ)); 
	        pstmt.setMaxRows(0);
	        IzvodRs j2eers = new IzvodRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}	        
    }
    public IzvodRs daoListaObradaDeviznihIzvoda(IzvodVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();                      
        sql.appendln("select CONVERT(char(10), datum_pocetak, 104) AS od_datuma, ");
        sql.appendln("CONVERT(char(10), datum_kraj, 104) AS do_datuma, count(*) as ukupno_stavki,");
        sql.appendln("datum_obrade AS obrada, datum_pocetak, datum_kraj, datum_obrade from bi_po_devize_izvjestaj_izvod "); 
        sql.append(" WHERE vrsta = 'D'"); 
        sql.append(" GROUP BY datum_obrade, datum_pocetak, datum_kraj "); 
        sql.append(" ORDER BY datum_obrade DESC ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IzvodRs j2eers = new IzvodRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

	private int daoCallStoredProcedureIzvodiDevizni(IzvodVo input_value)  {
		String sp_name = "bi_po_devize_izvodi";
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int count=0;
	    if(input_value.get(IzvodVo.BI_IZVOD_VO__PONAVLJANJE_OBRADE).length()==0)
	        input_value.set(IzvodVo.BI_IZVOD_VO__PONAVLJANJE_OBRADE, IzvodVo.VALID_IND_NO);
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++count,input_value.getAsSqlDate(JBDataDictionary.BI_IZVOD__DATUM_POCETAK));
			cs.setDate(++count,input_value.getAsSqlDate(JBDataDictionary.BI_IZVOD__DATUM_KRAJ));
			cs.setString(++count,input_value.get(IzvodVo.BI_IZVOD_VO__PONAVLJANJE_OBRADE));
			cs.registerOutParameter(++count,java.sql.Types.INTEGER);
			cs.execute();
			int status = cs.getInt(count);
			cs.close();
			if(status==100)
				throw new AS2DataAccessException("10004");
			else if(status==200)
			    throw new AS2DataAccessException("10005");
			else if(status==300)
			    throw new AS2DataAccessException("11015");
			else if(status==400)
			    throw new AS2DataAccessException("11014");
			return status;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}