package hr.adriacomsoftware.app.server.crm.gr.krediti.facade;

import hr.adriacomsoftware.app.server.crm.gr.krediti.da.jdbc.KreditJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class CrmKreditFacadeServer extends AS2FacadeServerLayer {
	private static CrmKreditFacadeServer _instance = null;
	public static CrmKreditFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new CrmKreditFacadeServer();
		}
		return _instance;
	}
	private CrmKreditFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	/************* FACADE SERVER  krediti gr ************/
	
	public AS2RecordList procitajKredite(AS2Record value) throws Exception {
		KreditJdbc dao = new KreditJdbc();
		return dao.daoFindKredite(value);
	}
}
