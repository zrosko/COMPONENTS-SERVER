package hr.adriacomsoftware.app.server.naplata.reports;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspVo;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrSspJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.reports.word.AS2WordDoc;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;

import java.io.File;
import java.io.FileOutputStream;


public final class NaplataReportServer extends AS2FacadeServerLayer {

	private static NaplataReportServer _instance = null;

	public static NaplataReportServer getInstance() {
		if (_instance == null) {
			_instance = new NaplataReportServer();
		}
		return _instance;
	}

	private NaplataReportServer() {
		AS2Context.setSingletonReference(this);
	}	
	public byte[] izvjestajGrSspWord(AS2Record vo)  {
		AS2RecordList podaci = new NaplataGrSspJdbc().daoIzvjestaji(new NaplataGrSspVo(vo));
		AS2Record map = podaci.getRowAt(0);
		if(map!=null)
			map = map.changeKeys("${","}"); //key
		else 
			map = new AS2Record();
		AS2WordDoc doc = new AS2WordDoc();
		String fileName;// =  "C:\\AS2_ZR\\WS\\AS2 PLATFORM COMMON\\OVRHA.doc";
		fileName = vo.get("fileLocation")+vo.get("fileName")+".doc";
		return doc.generateWord(fileName, map);
	}

	public static void main(String[] args) {
		AS2Transaction.begin();
		AS2Record vo = new AS2Record();
		vo.set("@@report_selected","ovrha_na_imovini");
		try {
			byte[] ba = NaplataReportServer.getInstance().izvjestajGrSspWord(vo);

			String destpathString = "C:\\AS2_ZR\\WS\\AS2 PLATFORM COMMON\\OVRHA_ANTE2.doc";
			File file = new File(destpathString);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(ba);
			fos.close();
			AS2Transaction.commit();
		} catch (Exception e) {
			AS2Transaction.rollback();
			e.printStackTrace();
		}
	}
}
