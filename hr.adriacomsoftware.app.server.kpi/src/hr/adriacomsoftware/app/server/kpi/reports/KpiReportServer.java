package hr.adriacomsoftware.app.server.kpi.reports;

import hr.adriacomsoftware.app.server.kpi.da.jdbc.KpiVrijednostJdbc;
import hr.adriacomsoftware.app.server.kpi.da.jdbc.PoslovnaJedinicaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.reports.excel.AS2ExcelWorkbookXls;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.util.Iterator;


public final class KpiReportServer extends AS2FacadeServerLayer {

	private static KpiReportServer _instance = null;

	public static KpiReportServer getInstance() {
		if (_instance == null) {
			_instance = new KpiReportServer();
		}
		return _instance;
	}

	private KpiReportServer() {
		AS2Context.setSingletonReference(this);
	}	
	public byte[] citajIzvjestajExcel(AS2Record vo)  {
		KpiVrijednostJdbc dao = new KpiVrijednostJdbc();
		AS2RecordList podaci =  dao.daoFindIzvjestaj(vo);
		
		if(vo.get("razina1").equals("Banka")){
			if(vo.get("razina2").equals("Financije")){
				if(vo.get("razina3").equals("RDG"))
					return rdgIzvjestajExcel(vo, podaci);
			}else if(vo.get("razina2").equals("Klijenti")){ 
				if(vo.get("razina3").equals("Prodaja"))
					return prodajaIzvjestajExcel(vo, podaci);
			}
		}
		return null;
	}
	private byte[] rdgIzvjestajExcel(AS2Record vo, AS2RecordList podaci)  {	
		try{
			PoslovnaJedinicaJdbc dao_pj = new PoslovnaJedinicaJdbc();
			AS2RecordList as2rs = dao_pj.daoFindPoslovneJedinice(vo);
			AS2ExcelWorkbookXls wb = new AS2ExcelWorkbookXls(getReseurcesPath(vo),"RDG.xls");
			Iterator<AS2Record> rows = as2rs.iteratorRows();
			while (rows.hasNext()) {
				AS2Record row =  rows.next();
				String sheet_name = row.get("naziv");
				String poslovna_jedinica = row.get("poslovna_jedinica");
				wb.copySheet("JABA", sheet_name);
				wb.setCurrentSheet(sheet_name);
				vo.set("naziv_dugi",  row.get("naziv_dugi"));
				StringBuilder razdoblje = new StringBuilder();
				razdoblje.append(vo.getAsStringRoman("mjesec"));
				razdoblje.append("-");
				razdoblje.append(vo.getAsStringRoman("mjesec2"));
				razdoblje.append(" ");
				razdoblje.append(vo.get("godina"));
				vo.set("razdoblje",razdoblje.toString());
				vo.set("prethodno_razdoblje",vo.getAsInt("godina")-1);
				wb.fillSheetWithParameters(sheet_name, vo);//TODO parametri?
				AS2RecordList jedna_org_jed = podaci.doSearch("poslovnica", "=", poslovna_jedinica);				
				wb.fillSheetWithData(sheet_name, jedna_org_jed, "vrsta_iznosa");
			}
			/******** POČETAK UKUPNO ******/
			KpiVrijednostJdbc dao = new KpiVrijednostJdbc();
			vo.set("razina3", "RDG-UKUPNO");
			AS2RecordList podaci_ukupno =  dao.daoFindIzvjestaj(vo);
			wb.setCurrentSheet("REKAPITULACIJA-A");
			wb.fillSheetWithData("REKAPITULACIJA-A", podaci_ukupno, "poslovnica");			
			/******** KRAJ UKUPNO *********/
			wb.deleteSheet("JABA");
			//wb.expandAllColumns();
			return wb.saveWorkbookAsBytes();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	private byte[] prodajaIzvjestajExcel(AS2Record vo, AS2RecordList podaci)  {	
		try{
			AS2ExcelWorkbookXls wb = new AS2ExcelWorkbookXls(getReseurcesPath(vo),"Prodaja_svi.xls");
			String profitniCentar = vo.get("profitni_centar");
			if(profitniCentar.equals("22000")){
				vo.set("profitni_centar", "Šibenik");
				wb.copySheet("JABA", "PC-Šibenik");
				wb.deleteSheet("JABA");
				wb.setCurrentSheet("PC-Šibenik");
				wb.fillSheetWithParameters("PC-Šibenik", vo);
				wb.fillSheetWithData("PC-Šibenik", podaci);
			}else if(profitniCentar.equals("22999")){
				vo.set("profitni_centar", "Šibenik - SME");
				wb.copySheet("JABA", "PC-Šibenik - SME");
				wb.deleteSheet("JABA");
				wb.setCurrentSheet("PC-Šibenik - SME");
				wb.fillSheetWithParameters("PC-Šibenik - SME", vo);
				wb.fillSheetWithData("PC-Šibenik - SME", podaci);
			}else if(profitniCentar.equals("21000")){
				vo.set("profitni_centar", "Split");
				wb.copySheet("JABA", "PC-Split");
				wb.deleteSheet("JABA");
				wb.setCurrentSheet("PC-Split");
				wb.fillSheetWithParameters("PC-Split", vo);
				wb.fillSheetWithData("PC-Split", podaci);
			}else if(profitniCentar.equals("10000")){
				vo.set("profitni_centar", "Zagreb");
				wb.copySheet("JABA", "PC-Zagreb");
				wb.deleteSheet("JABA");
				wb.setCurrentSheet("PC-Zagreb");
				wb.fillSheetWithParameters("PC-Zagreb", vo);
				wb.fillSheetWithData("PC-Zagreb", podaci);
			}else {//svi profitni centri
				vo.set("profitni_centar", "JABA");
				wb.setCurrentSheet("JABA");
				wb.fillSheetWithParameters("JABA", vo);
				wb.fillSheetWithData("JABA", podaci);
			}
			wb.expandAllColumns();
			return wb.saveWorkbookAsBytes();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
