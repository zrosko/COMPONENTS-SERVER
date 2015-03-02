package hr.adriacomsoftware.app.server.naplata.facade;


import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrKreditiRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrKreditiVo;
import hr.adriacomsoftware.app.common.naplata.facade.NaplataGrKreditiFacade;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrKreditiJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrKreditiPovijestJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class NaplataGrKreditiFacadeServer extends AS2FacadeServerLayer 
		implements NaplataGrKreditiFacade {

	private static NaplataGrKreditiFacadeServer _instance = null;
	public static NaplataGrKreditiFacadeServer getInstance() {
		if (_instance == null){
			_instance = new NaplataGrKreditiFacadeServer();
		}
		return _instance;
	}
	private NaplataGrKreditiFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public NaplataGrKreditiVo brisiNaplataGrKrediti(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrKreditiVo dodajNaplataGrKrediti(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		AS2Record res = dao.daoCreate(value);
		return new NaplataGrKreditiVo(res);
	}
	public NaplataGrKreditiVo azurirajNaplataGrKrediti(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		AS2Record res = dao.daoStore(value);
		return new NaplataGrKreditiVo(res);
	}
	public NaplataGrKreditiRs procitajSveNaplataGrKrediti(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		return new NaplataGrKreditiRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveNaplataGrKrediti()  {
		return new AS2RecordList();
	}
	public NaplataGrKreditiRs pretraziNaplataGrKrediti(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		return new NaplataGrKreditiRs(dao.daoFindKreditiPromet(value));
	}
	public NaplataGrKreditiRs procitajPovijestPolja(NaplataGrKreditiVo value)  {
		NaplataGrKreditiPovijestJdbc dao = new NaplataGrKreditiPovijestJdbc();
		return dao.daoProcitajPovijestPolja(value);
	}
	public NaplataGrKreditiRs izvjestaji(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		return dao.daoIzvjestaji(value);
	}
	public NaplataGrKreditiRs izvjestajReferat(NaplataGrKreditiVo value)  {
		NaplataGrKreditiJdbc dao = new NaplataGrKreditiJdbc();
		NaplataGrKreditiRs naplata_tekuci = dao.daoIzvjestajReferat(value);
		value.set("@@report_selected","krediti_plan_otplate"); 
		value.set("id_biljeske",value.get("id_biljeske_plana"));
		naplata_tekuci.addResultSet("krediti_plan_otplate_sub",dao.daoIzvjestaji(value));
		return naplata_tekuci;
	}
 }
