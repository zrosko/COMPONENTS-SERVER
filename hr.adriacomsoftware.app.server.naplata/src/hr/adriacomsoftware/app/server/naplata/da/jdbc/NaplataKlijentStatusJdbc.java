package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentStatusRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataKlijentStatusJdbc extends OLTPJdbc {
    public NaplataKlijentStatusJdbc() {
        setTableName("naplata_klijent_status");
    }
    public NaplataKlijentStatusRs daoFind(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select *   ");
		sql.append(" FROM view_naplata_klijent_status where oib = ? order by datum desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(NaplataKlijentVo.NAPLATA_KLIJENT__OIB));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentStatusRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentStatusRs daoFindZadnjiOpis(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select TOP 1 *   ");
		sql.append(" FROM view_naplata_klijent_status where oib = ? and vrsta = ? and datum <= ? order by datum desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(NaplataKlijentVo.NAPLATA_KLIJENT__OIB));
			pstmt.setObject(2,"OPIS");
			pstmt.setDate(3,value.getAsSqlDate(NaplataKlijentVo.NAPLATA_KLIJENT__DATUM));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentStatusRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentStatusRs daoFindZadnjiZakljucak(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select TOP 1 *   ");
		sql.append(" FROM view_naplata_klijent_status where oib = ? and vrsta = ? and datum <= ? order by datum desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(NaplataKlijentVo.NAPLATA_KLIJENT__OIB));
			pstmt.setObject(2,"ZAKLJUČAK");
			pstmt.setDate(3,value.getAsSqlDate(NaplataKlijentVo.NAPLATA_KLIJENT__DATUM));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentStatusRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }