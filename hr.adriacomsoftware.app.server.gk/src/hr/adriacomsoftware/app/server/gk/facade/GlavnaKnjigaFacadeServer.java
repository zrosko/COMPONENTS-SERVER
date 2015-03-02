package hr.adriacomsoftware.app.server.gk.facade;

import hr.adriacomsoftware.app.common.gk.facade.GlavnaKnjigaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.gk.da.jdbc.GlavnaKnjigaJdbc;
import hr.adriacomsoftware.app.server.gk.da.jdbc.TemeljnicaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class GlavnaKnjigaFacadeServer extends AS2FacadeServerLayer
		implements GlavnaKnjigaFacade {
    
    private static GlavnaKnjigaFacadeServer _instance = null;

    public static GlavnaKnjigaFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new GlavnaKnjigaFacadeServer();
        }
        return _instance;
    }

    private GlavnaKnjigaFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }
 
    public OsnovniRs procitajSveStavke(OsnovniVo value)  {
        GlavnaKnjigaJdbc dao = new GlavnaKnjigaJdbc();
        return dao.daoPronadiStavke(value, false);
    }

    public OsnovniRs pronadiSveStavke(OsnovniVo value)  {
        GlavnaKnjigaJdbc dao = new GlavnaKnjigaJdbc(); 
        return dao.daoPronadiStavke(value, true);
    }

	public AS2RecordList temeljnica(AS2Record value) {
		 TemeljnicaJdbc dao = new TemeljnicaJdbc();
		 return dao.daoTemeljnica(value,value.get("@@TEMELJNICA"));
	}
 }