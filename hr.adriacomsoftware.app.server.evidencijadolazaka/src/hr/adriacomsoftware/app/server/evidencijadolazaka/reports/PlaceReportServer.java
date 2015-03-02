package hr.adriacomsoftware.app.server.evidencijadolazaka.reports;

import hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc.PlaceEvidencijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.reports.AS2ReportRenderer;
import hr.as2.inf.common.reports.AS2ReportRendererFactory;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PlaceReportServer extends AS2FacadeServerLayer {

	private static PlaceReportServer _instance = new PlaceReportServer();

	public static PlaceReportServer getInstance() {
		return _instance;
	}

	private PlaceReportServer() { 
		AS2Context.setSingletonReference(this);
	}	
	 //IZVJEŠTAJI
    public byte[] izvjestajiJasper(AS2Record value)  {
    	PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
    	AS2RecordList recordList = dao.daoIzvjestaji(value);
    	AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(".jasper");
    	return renderer.renderReport(AS2ReportRenderer.REPORT_FILE_NAME, value, recordList,value.getAsString(AS2ReportRenderer.AS2_REPORT_FORMAT, AS2ReportRenderer.DEFAULT_REPORT_FORMAT));
    }
    
    public byte[] izvjestajiXls(AS2Record value)  {		
    	PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
    	AS2RecordList recordList = dao.daoIzvjestaji(value);
    	AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(".xlsx");
    	return renderer.renderReport("", value, recordList,value.getAsString(AS2ReportRenderer.AS2_REPORT_FORMAT, AS2ReportRenderer.DEFAULT_REPORT_FORMAT));
    }

}
