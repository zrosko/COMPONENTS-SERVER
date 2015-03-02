package hr.adriacomsoftware.app.server.studija.facade;

import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraRs;
import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraVo;
import hr.adriacomsoftware.app.common.studija.dto.InvPlanRs;
import hr.adriacomsoftware.app.common.studija.dto.InvPlanVo;
import hr.adriacomsoftware.app.common.studija.dto.InvProizvodRs;
import hr.adriacomsoftware.app.common.studija.dto.InvProizvodVo;
import hr.adriacomsoftware.app.common.studija.dto.InvRadRs;
import hr.adriacomsoftware.app.common.studija.dto.InvRadVo;
import hr.adriacomsoftware.app.common.studija.facade.InvStudijaFacade;
import hr.adriacomsoftware.app.server.studija.da.jdbc.InvPlanJdbc;
import hr.adriacomsoftware.app.server.studija.da.jdbc.InvProizvodJdbc;
import hr.adriacomsoftware.app.server.studija.da.jdbc.InvRadJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class InvStudijaFacadeServer extends AS2FacadeServerLayer
		implements InvStudijaFacade {
	private static InvStudijaFacadeServer _instance = null;

	public static InvStudijaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new InvStudijaFacadeServer();
		}
		return _instance;
	}

	private InvStudijaFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	// RAD
	public InvRadRs listajSveRadnike(InvRadVo value)  {
		InvRadJdbc dao = new InvRadJdbc();
		InvRadRs rs = new InvRadRs(dao.daoFind(value));//Listu(InvRadJdbc.SQL_LISTA, null));
		return rs;
	}

	public InvRadVo azurirajRadnika(InvRadVo value)  {
		InvRadJdbc dao = new InvRadJdbc();
		dao.daoStore(value);
		return value;
	}

	public InvRadVo dodajRadnika(InvRadVo value)  {
		InvRadJdbc dao = new InvRadJdbc();
		dao.daoCreate(value);
		return value;
	}

	public InvRadVo brisiRadnika(InvRadVo value)  {
		InvRadJdbc dao = new InvRadJdbc();
		dao.daoRemove(value);// TODO logicko brisanje
		return value;
	}

	// PROIZVOD
	public InvProizvodVo dodajProizvod(InvProizvodVo value)  {
		InvProizvodJdbc dao = new InvProizvodJdbc();
		InvProizvodVo vo = new InvProizvodVo(value);
		vo.set("datum",
				AS2Date.convert2(new java.util.Date(value.getAsLong("datum"))));
		dao.daoCreate(vo);
		return value;
	}

	public InvProizvodVo azurirajProizvod(InvProizvodVo value)  {
		InvProizvodJdbc dao = new InvProizvodJdbc();
		InvProizvodVo vo = new InvProizvodVo(value);
		vo.set("datum",
				AS2Date.convert2(new java.util.Date(value.getAsLong("datum"))));
		dao.daoStore(vo);
		return value;
	}

	public void brisiProizvod(InvProizvodVo value)  {
		InvProizvodJdbc dao = new InvProizvodJdbc();
		dao.daoRemove(value);
	}

	public InvProizvodRs procitajProizvode(InvProizvodVo value)
			 {
		InvProizvodJdbc dao = new InvProizvodJdbc();
		InvProizvodRs rs = new InvProizvodRs(dao.daoFind(value));
		return rs;
	}

	/************* FACADE SERVER inv_plan ************/
	public InvPlanVo brisiInvPlan(InvPlanVo value)  {
		InvPlanJdbc dao = new InvPlanJdbc();
		dao.daoRemove(value);
		return value;
	}

	public InvPlanVo dodajInvPlan(InvPlanVo value)  {
		InvPlanJdbc dao = new InvPlanJdbc();
		AS2Record res = dao.daoCreate(value);
		return new InvPlanVo(res);
	}

	public InvPlanVo azurirajInvPlan(InvPlanVo value)  {
		InvPlanJdbc dao = new InvPlanJdbc();
		AS2Record res = dao.daoStore(value);
		return new InvPlanVo(res);
	}

	public InvPlanRs procitajSveInvPlan(InvPlanVo value)  {
		InvPlanJdbc dao = new InvPlanJdbc();
		return new InvPlanRs(dao.daoFind(value));
	}

	public AS2RecordList listajSveInvPlan()  {
		//InvPlanJdbc dao = new InvPlanJdbc();
		// return dao.daoFindListu(InvPlanJdbc.SQL_LISTA);
		return new AS2RecordList();
	}

	public InvPlanRs pretraziInvPlan(InvPlanVo value)  {
		InvPlanJdbc dao = new InvPlanJdbc();
		return new InvPlanRs(dao.daoFind(value));
	}

	public AS2RecordList listaValuta(AS2Record value)  {
		
		return new AS2RecordList();
	}

	public OrPoduzetaMjeraRs procitajSvePoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	public OrPoduzetaMjeraVo azurirajPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	public OrPoduzetaMjeraVo dodajPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	public OrPoduzetaMjeraVo brisiPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		// TODO Auto-generated method stub
		return null;
	}
}