package hr.adriacomsoftware.app.server.pravneosobe.bonitet.facade;

import hr.adriacomsoftware.app.common.pravneosobe.bonitet.facade.BonitetFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc.BonitetJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class BonitetFacadeServer extends AS2FacadeServerLayer
		implements BonitetFacade {

	private static BonitetFacadeServer _instance = null;
	public static BonitetFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new BonitetFacadeServer();
		}
		return _instance;
	}
	private BonitetFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public AS2RecordList citajBilancu(PravnaOsobaVo value)  {
        BonitetJdbc dao = new BonitetJdbc();
        return dao.daoCitajBilancu(value);
    }
    public AS2RecordList citajRacunDobitiGubitka(PravnaOsobaVo value)  {
        BonitetJdbc dao = new BonitetJdbc();
        return dao.daoCitajRacunDobitiGubitka(value);
    }
    public AS2RecordList citajStanjaZaReferat(PravnaOsobaVo value)  {
        BonitetJdbc dao = new BonitetJdbc();
        return dao.daoCitajStanjaZaReferat(value);
    }
    public AS2RecordList citajBonitetZaBSA(AS2Record value)  {
        BonitetJdbc dao = new BonitetJdbc();
        return dao.daoCitajBonitetZaBSA(value);
    }
}
