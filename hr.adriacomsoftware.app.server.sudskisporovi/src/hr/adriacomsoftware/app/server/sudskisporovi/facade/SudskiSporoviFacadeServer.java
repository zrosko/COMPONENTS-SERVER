package hr.adriacomsoftware.app.server.sudskisporovi.facade;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.sudskisporovi.facade.SudskiSporoviFacade;
import hr.adriacomsoftware.app.server.sudskisporovi.da.jdbc.OdvjetnikJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

public final class SudskiSporoviFacadeServer extends AS2FacadeServerLayer
		implements SudskiSporoviFacade {
    private static SudskiSporoviFacadeServer _instance = null;
    public static SudskiSporoviFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new SudskiSporoviFacadeServer();
        }
        return _instance;
    }
    private SudskiSporoviFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }

    public OsnovniRs pronadiSveSudove(OsnovniVo value)  {
        OdvjetnikJdbc dao = new OdvjetnikJdbc();
        return dao.daoPronadiSudove(value);
    }
    public OsnovniRs pronadiSveBiljeznike(OsnovniVo value)  {
        OdvjetnikJdbc dao = new OdvjetnikJdbc();
        return dao.daoPronadiBiljeznike(value);
    }

	public OsnovniRs pronadiSveSudoveJavneBiljeznikeInstitucije(OsnovniVo value)  {
		OdvjetnikJdbc dao = new OdvjetnikJdbc();
        return dao.daoPronadiSudoveJavneBiljeznikeInstitucije(value);
	}
}