package hr.adriacomsoftware.app.server.dionice.reports;

import hr.adriacomsoftware.app.server.dionice.da.jdbc.UpisnicaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.reports.excel.AS2ExcelWorkbookXlsx;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

public final class DioniceReportServer extends AS2FacadeServerLayer {

	private static DioniceReportServer _instance = new DioniceReportServer();

	public static DioniceReportServer getInstance() {
		return _instance;
	}

	private DioniceReportServer() {
		AS2Context.setSingletonReference(this);
	}

	public byte[] citajIzvjestajExcel(AS2Record vo) {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		AS2RecordList podaci = dao.daoFindUpisnice(vo);
		try {
			AS2ExcelWorkbookXlsx wb = AS2ExcelWorkbookXlsx
					.convertRecordListToExcelXlsx(podaci);
			return wb.saveWorkbookAsBytes();
		} catch (Exception e) {
			throw new AS2Exception(e);
		}

	}
}
