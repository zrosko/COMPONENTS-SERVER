package hr.adriacomsoftware.app.server.pdv.po.facade;

import hr.adriacomsoftware.app.common.pdv.po.dto.PdvRs;
import hr.adriacomsoftware.app.common.pdv.po.dto.PdvVo;
import hr.adriacomsoftware.app.common.pdv.po.facade.PdvFacade;
import hr.adriacomsoftware.app.server.pdv.po.da.jdbc.PdvJdbc;
import hr.adriacomsoftware.app.server.pdv.po.da.jdbc.PdvProtukontoJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PdvFacadeServer extends AS2FacadeServerLayer
		implements PdvFacade {

	private static PdvFacadeServer _instance = null;
	public static PdvFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new PdvFacadeServer();
		}
		return _instance;
	}
	private PdvFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public PdvRs listajSvePdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
        return dao.daoLoadAll(value);
    }
    public PdvRs pronadiPdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
        return dao.daoFind(value);
    }
    public PdvVo citajPdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
        return dao.daoLoad(value);
    }
    public PdvVo dodajPdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
   		value.setRedniBroj(dao.daoZadnjiRedniBroj(value));
        dao.daoCreate(value);
        return value;
    }
    public PdvVo azurirajPdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
        dao.daoStore(value);
        return value;
    }
    public PdvVo brisiPdv(PdvVo value)  {
        PdvJdbc dao = new PdvJdbc();
        PdvVo vo = new PdvVo();
        vo.setIdUnosa(value.getIdUnosa());
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
