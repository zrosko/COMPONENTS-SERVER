package hr.adriacomsoftware.app.server.kpi.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.KPIJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.CallableStatement;


public final class KpiVrijednostJdbc extends KPIJdbc {
	public KpiVrijednostJdbc() {
		setTableName("kpi_vrijednost");
	}
	public AS2RecordList daoFindVrijednostiPokazatelja(AS2Record value)	{
		StringBuilder sp = new StringBuilder();		
		sp.append("{call dbo.stp_kpi_prikaz ");
		sp.append("(?,?,?,?,?,?,?,?,?,?) }");
		try {
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			int counter = 1;
			cs.setObject(counter++, value.getAsObject("razina1"));
			cs.setObject(counter++, value.getAsObject("razina2"));
			cs.setObject(counter++, value.getAsObject("razina3"));
			cs.setObject(counter++, value.getAsObject("godina"));
			cs.setObject(counter++, value.getAsObject("godina2"));
			cs.setObject(counter++, value.getAsObject("mjesec"));
			cs.setObject(counter++, value.getAsObject("mjesec2"));
			cs.setObject(counter++, value.getAsObject("profitni_centar"));
			cs.setObject(counter++, value.getAsObject("organizacijska_jedinica"));
			cs.setObject(counter++, value.getAsObject("chart_type"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoFindOperTrosakDetalji(AS2Record value)  {
		StringBuilder sp = new StringBuilder();
		sp.append("{call dbo.stp_kpi_prikaz_banka_detalji ");
		sp.append("(?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			int counter = 1;
			cs.setObject(counter++, value.getAsObject("oznaka"));
			cs.setObject(counter++, value.getAsObject("godina"));
			cs.setObject(counter++, value.getAsObject("godina2"));
			cs.setObject(counter++, value.getAsObject("mjesec"));
			cs.setObject(counter++, value.getAsObject("profitni_centar"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoFindIzvjestaj(AS2Record value)  {
		StringBuilder sp = new StringBuilder();
		sp.append("{call ");
		sp.append("dbo.stp_kpi_izvjestaj ");
		sp.append("(?,?,?,?,?,?,?,?)}");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			int counter = 1;
			cs.setObject(counter++, value.getAsObject("razina1"));
			cs.setObject(counter++, value.getAsObject("razina2"));
			cs.setObject(counter++, value.getAsObject("razina3"));
			cs.setObject(counter++, value.getAsObject("profitni_centar"));
			cs.setObject(counter++, value.getAsObject("mjesec"));
			cs.setObject(counter++, value.getAsObject("mjesec2"));
			cs.setObject(counter++, value.getAsObject("godina"));
			cs.setObject(counter++, value.getAsObject("godina2"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}