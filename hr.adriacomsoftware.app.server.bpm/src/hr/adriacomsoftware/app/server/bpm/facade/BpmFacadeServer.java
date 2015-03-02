package hr.adriacomsoftware.app.server.bpm.facade;

import hr.adriacomsoftware.app.common.bpm.dto.BpmProcesRs;
import hr.adriacomsoftware.app.common.bpm.dto.BpmProcesVo;
import hr.adriacomsoftware.app.common.bpm.facade.BpmFacade;
import hr.adriacomsoftware.app.server.bpm.da.jdbc.BpmProcesJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class BpmFacadeServer extends AS2FacadeServerLayer implements
		BpmFacade {

	private static BpmFacadeServer _instance = null;

	public static BpmFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new BpmFacadeServer();
		}
		return _instance;
	}

	private BpmFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public AS2RecordList listajSveProcese() {
		BpmProcesJdbc dao = new BpmProcesJdbc();
		return dao.daoFindListu(BpmProcesJdbc.SQL_PROCESI,
				" order by razina,id_procesa");
	}

	public BpmProcesRs procitajSveProcese(BpmProcesVo value) {
		BpmProcesJdbc dao = new BpmProcesJdbc();
		BpmProcesRs rs = new BpmProcesRs(dao.daoFind(value));
		return rs;
	}

	public BpmProcesVo azurirajProces(BpmProcesVo value) {
		BpmProcesJdbc dao = new BpmProcesJdbc();
		dao.daoStore(value);
		return value;
	}

	public BpmProcesVo dodajProces(BpmProcesVo value) {
		BpmProcesJdbc dao = new BpmProcesJdbc();
		dao.daoCreate(value);
		return value;
	}

	public BpmProcesVo brisiProces(BpmProcesVo value) {
		BpmProcesJdbc dao = new BpmProcesJdbc();
		dao.daoRemove(value);
		return value;
	}
}
