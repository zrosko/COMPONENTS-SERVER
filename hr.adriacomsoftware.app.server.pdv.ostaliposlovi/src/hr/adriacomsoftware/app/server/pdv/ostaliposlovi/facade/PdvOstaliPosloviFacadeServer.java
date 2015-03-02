package hr.adriacomsoftware.app.server.pdv.ostaliposlovi.facade;

import hr.adriacomsoftware.app.common.pdv.ostaliposlovi.dto.PdvOstaliPosloviRs;
import hr.adriacomsoftware.app.common.pdv.ostaliposlovi.dto.PdvOstaliPosloviVo;
import hr.adriacomsoftware.app.common.pdv.ostaliposlovi.facade.PdvOstaliPosloviFacade;
import hr.adriacomsoftware.app.server.pdv.ostaliposlovi.da.jdbc.PdvOstaliPosloviJdbc;
import hr.adriacomsoftware.app.server.pdv.po.da.jdbc.PdvProtukontoJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PdvOstaliPosloviFacadeServer extends AS2FacadeServerLayer
		implements PdvOstaliPosloviFacade {

	private static PdvOstaliPosloviFacadeServer _instance = null;
	public static PdvOstaliPosloviFacadeServer getInstance() {
		if (_instance == null){
			_instance = new PdvOstaliPosloviFacadeServer();
		}
		return _instance;
	}
	private PdvOstaliPosloviFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public PdvOstaliPosloviRs listajSvePdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        return dao.daoLoadAll(value);
    }
    public PdvOstaliPosloviRs pronadiPdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        return dao.daoFind(value);
    }
    public PdvOstaliPosloviVo citajPdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        return dao.daoLoad(value);
    }
    public PdvOstaliPosloviVo dodajPdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PdvOstaliPosloviVo azurirajPdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        dao.daoStore(value);
        return value;
    }
    public PdvOstaliPosloviVo brisiPdv(PdvOstaliPosloviVo value)  {
        PdvOstaliPosloviJdbc dao = new PdvOstaliPosloviJdbc();
        PdvOstaliPosloviVo vo = new PdvOstaliPosloviVo();
        vo.setId(value.getId());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public AS2RecordList listajSvaProtukonta()  {
        PdvProtukontoJdbc dao = new PdvProtukontoJdbc();
        AS2RecordList rs = dao.daoFind(new AS2Record());
        return rs;
    }
}
