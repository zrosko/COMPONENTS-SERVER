package hr.adriacomsoftware.app.server.sjednica.facade;
 
import hr.adriacomsoftware.app.server.sjednica.da.file.SjednicaFile;
import hr.adriacomsoftware.app.server.sjednica.da.jdbc.SjednicaJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

public final class SjednicaFacadeServer extends AS2FacadeServerLayer {

	private static SjednicaFacadeServer _instance = null;

	public static SjednicaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new SjednicaFacadeServer();
		}
		return _instance;
	}

	private SjednicaFacadeServer() {
	}

	public AS2RecordList procitajProizvode(AS2Record value)  {
		SjednicaJdbc dao = new SjednicaJdbc();
		return dao.daoFind(value);
	}
	public AS2Record dodajInvPlan(AS2Record value)  {
		SjednicaFile dao = new SjednicaFile();
		dao.daoCreate(value);
		return value;
	}
}