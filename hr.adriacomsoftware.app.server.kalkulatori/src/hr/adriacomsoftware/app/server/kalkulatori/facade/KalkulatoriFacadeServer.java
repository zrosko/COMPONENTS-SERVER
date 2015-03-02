package hr.adriacomsoftware.app.server.kalkulatori.facade;

import hr.adriacomsoftware.app.common.kalkulatori.dto.FinancijskiKalkulatorVo;
import hr.adriacomsoftware.app.common.kalkulatori.facade.KalkulatoriFacade;
import hr.adriacomsoftware.app.server.kalkulatori.da.jdbc.KalkulatoriJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KalkulatoriFacadeServer extends AS2FacadeServerLayer
		implements KalkulatoriFacade {
	private static KalkulatoriFacadeServer _instance = null;
	public static KalkulatoriFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new KalkulatoriFacadeServer();
		}
		return _instance;
	}
	private KalkulatoriFacadeServer() {
		AS2Context.setSingletonReference(this);
	}
	//TODO see how to use it @AS2ValueListHandler
	public FinancijskiKalkulatorVo izracunajZateznukamatu(FinancijskiKalkulatorVo value)  {
		KalkulatoriJdbc dao = new KalkulatoriJdbc();
		value.setKamatnaStopa(dao.daoIzracunajZateznuKamatu(value));
		return value;
	}

	public AS2RecordList izracunajPlanOtplateEks(FinancijskiKalkulatorVo value)	 {
		KalkulatoriJdbc dao = new KalkulatoriJdbc();
		return dao.daoIzracunajkOtplatniPlanEks(value);
	}

	public AS2RecordList izracunajPlanOtplate(FinancijskiKalkulatorVo value)  {
		KalkulatoriJdbc dao = new KalkulatoriJdbc();
		return dao.daoIzracunajOtplatniPlan(value);
	}

	public AS2RecordList izracunajKamatu(FinancijskiKalkulatorVo value)	 {
		KalkulatoriJdbc dao = new KalkulatoriJdbc();
		return dao.daoIzracunajKamatu(value);
	}
}
