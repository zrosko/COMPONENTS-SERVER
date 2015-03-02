package hr.adriacomsoftware.app.server.obrasci.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.TESTObrasciJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class DokumentJdbc extends TESTObrasciJdbc {

	public DokumentJdbc() {
		setTableName("dok_dokument");
	}

	public AS2RecordList daoFindByKorisnik(AS2Record value)  {
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					"SELECT * FROM dbo.view_dok_dokument");
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoFindByVrsta(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from dbo.view_dok_dokument ");
		sql.append(" where aplikacija = '" + value.get("aplikacija") + "' ");
		if (!value.get("id_vrste").equals("0")
				&& value.get("id_vrste").length() > 0)
			sql.append(" and id_vrste = " + value.get("id_vrste") + " ");
		sql.append(" order by id_dokumenta desc ");
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

	public AS2RecordList daoLoadDokument(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.view_dok_dokument where id_dokumenta= ?");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++, value.getProperty("id_dokumenta"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoFindPoljaForme(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.view_dok_dokument_polje WHERE id_dokumenta = ?");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++, value.getProperty("id_dokumenta"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2Record daoBrisiDokumente(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.dok_dokument SET ispravno=0 WHERE id_dokumenta = ?");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++, value.getProperty("id_dokumenta"));
			pstmt.setMaxRows(1);
			int updateCounter = pstmt.executeUpdate();
			if (updateCounter != 1) {
				AS2Exception e = new AS2Exception("162");
				e.setSeverity(updateCounter);
				throw e;
			} else {
				value.set("updated", "true");
			}	
			pstmt.close();
			return value;
		} catch (AS2Exception e) {
			throw e;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}