package hr.adriacomsoftware.app.server.sukl.facade;

import hr.adriacomsoftware.app.common.sukl.dto.SuklAktRs;
import hr.adriacomsoftware.app.common.sukl.dto.SuklAktVo;
import hr.adriacomsoftware.app.common.sukl.facade.SukladnostFacade;
import hr.adriacomsoftware.app.server.sukl.da.jdbc.SuklAktJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class SukladnostFacadeServer extends AS2FacadeServerLayer 
		implements SukladnostFacade{
	private static SukladnostFacadeServer _instance = null;
	public static SukladnostFacadeServer getInstance() {
		if (_instance == null){
			_instance = new SukladnostFacadeServer();
		}
		return _instance;
	}
	private SukladnostFacadeServer(){
		AS2Context.setSingletonReference(this);
	}

public SuklAktVo brisiSuklAkt(SuklAktVo value)  {
	SuklAktJdbc dao = new SuklAktJdbc();
	dao.daoRemove(value);
	return value;
}
public SuklAktVo dodajSuklAkt(SuklAktVo value)  {
	SuklAktJdbc dao = new SuklAktJdbc();
	AS2Record res = dao.daoCreate(value);
	return new SuklAktVo(res);
}
public SuklAktVo azurirajSuklAkt(SuklAktVo value)  {
	SuklAktJdbc dao = new SuklAktJdbc();
	AS2Record res = dao.daoStore(value);
	return new SuklAktVo(res);
}
public SuklAktRs procitajSveSuklAkt(SuklAktVo value)  {
	SuklAktJdbc dao = new SuklAktJdbc();
	return new SuklAktRs(dao.daoFind(value));
}
public AS2RecordList listajSveSuklAkt()  {
	return new AS2RecordList();
}
public SuklAktRs pretraziSuklAkt(SuklAktVo value)  {
	SuklAktJdbc dao = new SuklAktJdbc();
	return new SuklAktRs(dao.daoFind(value));
}

}
