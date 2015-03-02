package hr.adriacomsoftware.app.server.crm.obrada.facade;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostVo;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPrivitakVo;
import hr.adriacomsoftware.app.common.crm.obrada.dto.KontaktObradaVo;
import hr.adriacomsoftware.app.common.crm.obrada.facade.CrmObradaFacade;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktAktivnostJdbc;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktPrivitakJdbc;
import hr.adriacomsoftware.app.server.crm.obrada.da.jdbc.KontaktObradaJdbc;
import hr.as2.inf.common.core.AS2Context;
//import hr.adriacomsoftware.inf.common.reports.AS2ReportRenderer;
//import hr.adriacomsoftware.inf.common.reports.AS2ReportRendererFactory;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.util.Calendar;
import java.util.Iterator;


public final class CrmObradaFacadeServer extends AS2FacadeServerLayer
		implements CrmObradaFacade {

	private static CrmObradaFacadeServer _instance = null;
	public static CrmObradaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new CrmObradaFacadeServer();
		}
		return _instance;
	}
	private CrmObradaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}

	/************* FACADE SERVER  kontakt_obrada ************/
	//SVAKI VO unutar RS ima (broj_partije, id_pracenja (id_predmeta), naziv) osim toga i (datum,vrsta_dokumenta) 
	public AS2Record dodajObradu(AS2RecordList value) {
		//TODO J2EEUser user = (J2EEUser) value.getProperty(J2EEConstants.USER_OBJ);
		String vrsta_dokumenta = value.get("vrsta_dokumenta")+".jasper";
		Calendar datum = AS2Date.getTodayAsCalendar(); //TODO poslati sa klijenta		
		String korisnik = "zrosko";//user.get("korisnik");//TODO
		
//		String DEFAULT_REPORTS_SERVICE_PATH = "hr/adriacomsoftware/resources/common/reports/tekuci/"+vrsta_dokumenta;
//		String DEFAULT_REPORTS_TYPE = ".jasper";
//		String DEFAULT_REPORTS_FORMAT = "pdf";
		
		AS2RecordList grupa_rs = new AS2RecordList();
		KontaktAktivnostJdbc dao_aktivnost = new KontaktAktivnostJdbc();
		KontaktPrivitakJdbc dao_privitak = new KontaktPrivitakJdbc();
		
		Iterator<AS2Record> E = value.getRows().iterator();
		while(E.hasNext()){
			AS2Record vo = E.next();
			AS2RecordList jedan_rs = value;//TODO zovi bazu - vo
			grupa_rs.appendRowsOnly(jedan_rs);
			//pripremi za slanje na server (pojedinačno)
//			File reportFile = new File(DEFAULT_REPORTS_SERVICE_PATH);
//			HashMap<Object, Object> parameters = new HashMap<Object, Object>();
//			AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(DEFAULT_REPORTS_TYPE);
//			byte[] byteArray =	renderer.renderReport(reportFile.getPath(),parameters,jedan_rs,DEFAULT_REPORTS_FORMAT);
			KontaktAktivnostVo vo_aktivnost = new KontaktAktivnostVo(vo);
			vo_aktivnost.setDatum(datum);
			vo_aktivnost.setKorisnik(korisnik);
			vo_aktivnost.setIdPredmeta("16794");
			vo_aktivnost.setKategorija("blokada_gr");
			vo_aktivnost.setPodkategorija("obavijest_o_blokadi");
			vo_aktivnost.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"id_temp");
			dao_aktivnost.daoCreate(vo_aktivnost);
			String id_aktivnosti = dao_aktivnost.daoZadnjiIdAktivnosti(vo_aktivnost);
			KontaktPrivitakVo vo_privitak = new KontaktPrivitakVo(vo_aktivnost);
			vo_privitak.setIdAktivnosti(id_aktivnosti);
//			vo_privitak.setProperty("dokument", byteArray);
			vo_privitak.setNazivDokumenta(vrsta_dokumenta);			
			dao_privitak.daoCreate(vo_privitak);
		}
		//pripremi za spremanje grupnog izvještaja na server
		KontaktObradaVo vo_grupa = new KontaktObradaVo();
//		File reportFile = new File(DEFAULT_REPORTS_SERVICE_PATH);
//		HashMap<Object, Object> parameters = new HashMap<Object, Object>();
//		AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(DEFAULT_REPORTS_TYPE);
//		byte[] byteArray =	renderer.renderReport(reportFile.getPath(),parameters,grupa_rs,DEFAULT_REPORTS_FORMAT);
//		vo_grupa.setProperty("dokument", byteArray);//TODO dodati vo
		vo_grupa.setDatumObrade(datum);
		vo_grupa.setDatumPocetak(datum);
		vo_grupa.setDatumKraj(datum);
		vo_grupa.setVrstaDokumenta(vrsta_dokumenta);
		vo_grupa.setKorisnik(korisnik);		
		KontaktObradaJdbc dao = new KontaktObradaJdbc();
		dao.daoCreate(vo_grupa);
		return value;
	}
}
