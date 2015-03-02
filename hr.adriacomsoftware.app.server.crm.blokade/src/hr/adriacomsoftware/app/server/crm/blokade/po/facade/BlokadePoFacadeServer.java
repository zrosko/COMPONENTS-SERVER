package hr.adriacomsoftware.app.server.crm.blokade.po.facade;

import hr.adriacomsoftware.app.server.crm.blokade.po.da.jdbc.BlokadaPoJdbc;
import hr.adriacomsoftware.app.server.crm.blokade.po.da.jdbc.BlokadaPoSifrarnikJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class BlokadePoFacadeServer extends AS2FacadeServerLayer{

	private static BlokadePoFacadeServer _instance = null;
	public static BlokadePoFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new BlokadePoFacadeServer();
		}
		return _instance;
	}
	private BlokadePoFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	public AS2RecordList procitajSveBlokade(AS2Record value) {
		BlokadaPoJdbc dao = new BlokadaPoJdbc();
		return dao.daoFindListuBlokadaPo(value);
	}
  	public AS2RecordList procitajSifrarnik(AS2Record value) {
  		BlokadaPoSifrarnikJdbc dao = new BlokadaPoSifrarnikJdbc();
  		return dao.daoFindSifrarnik(value, false);
  	}
}
