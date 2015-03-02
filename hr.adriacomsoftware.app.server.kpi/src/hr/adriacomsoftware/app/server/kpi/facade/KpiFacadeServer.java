package hr.adriacomsoftware.app.server.kpi.facade;

import hr.adriacomsoftware.app.server.kpi.da.jdbc.KpiVrijednostJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

public final class KpiFacadeServer extends AS2FacadeServerLayer {

	private static KpiFacadeServer _instance = null;

	public static KpiFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new KpiFacadeServer();
		}
		return _instance;
	}

	private KpiFacadeServer() {
		AS2Context.setSingletonReference(this);
	}
	public AS2RecordList citajVrijednostiPokazatelja(AS2Record value)  {
		KpiVrijednostJdbc dao = new KpiVrijednostJdbc();
		return dao.daoFindVrijednostiPokazatelja(value);
	}

	public AS2RecordList listajOperativniTroskoviDetalji(AS2Record value)  {
		KpiVrijednostJdbc dao = new KpiVrijednostJdbc();
		return dao.daoFindOperTrosakDetalji(value);
	}
	public AS2RecordList citajIzvjestaj(AS2Record value)  {
		KpiVrijednostJdbc dao = new KpiVrijednostJdbc();
		return dao.daoFindIzvjestaj(value);
	}
}
