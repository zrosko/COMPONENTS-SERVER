package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.partija.facade;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogObavijestRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogObavijestVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogPartijeRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.dto.ZahNalogPartijeVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.partija.facade.NalogOtvaranjaPartijeFacade;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.partija.da.jdbc.PoZahNalogPartijeJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.partija.da.jdbc.PoZahObavijestNalogaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.validations.AS2ValidatorService;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class NalogOtvaranjaPartijeFacadeServer extends AS2FacadeServerLayer
		implements NalogOtvaranjaPartijeFacade {

	private static NalogOtvaranjaPartijeFacadeServer _instance = null;
	public static NalogOtvaranjaPartijeFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new NalogOtvaranjaPartijeFacadeServer();
		}
		return _instance;
	}
	private NalogOtvaranjaPartijeFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public ZahNalogPartijeRs procitajSveNaloge(ZahNalogPartijeVo value)  {
        PoZahNalogPartijeJdbc dao = new PoZahNalogPartijeJdbc();
        return dao.daoProcitajNaloge(value);
    }
    public ZahNalogPartijeRs pronadiNaloge(ZahNalogPartijeVo value)  {
        PoZahNalogPartijeJdbc dao = new PoZahNalogPartijeJdbc();
        return dao.daoPronadiNaloge(value, true);
    }
    public ZahNalogPartijeVo azurirajNalog(ZahNalogPartijeVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("NalogOtvaranjaPartijeFacadeServer.dodajNalog", value); 
        PoZahNalogPartijeJdbc dao = new PoZahNalogPartijeJdbc();
        dao.daoStore(value);
        return value;
    }
    public ZahNalogPartijeVo dodajNalog(ZahNalogPartijeVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("NalogOtvaranjaPartijeFacadeServer.dodajNalog", value); 
        PoZahNalogPartijeJdbc dao = new PoZahNalogPartijeJdbc();
        dao.daoCreate(value);
        return value;
    }
    public ZahNalogPartijeVo brisiNalog(ZahNalogPartijeVo value)  {
        PoZahNalogPartijeJdbc dao = new PoZahNalogPartijeJdbc();
        ZahNalogPartijeVo _vo = new ZahNalogPartijeVo();
        _vo.setIdNaloga(value.getIdNaloga());
        _vo.setIspravnoNE();
        dao.daoStore(_vo);
        return _vo;
    }
    public ZahNalogObavijestVo azurirajObavijestNaloga(ZahNalogObavijestVo value)  {
        PoZahObavijestNalogaJdbc dao = new PoZahObavijestNalogaJdbc();
        dao.daoStore(value);
        return value;
    }
    public ZahNalogObavijestRs procitajSveObavijestiNaloga(ZahNalogObavijestVo value)  {
        PoZahObavijestNalogaJdbc dao = new PoZahObavijestNalogaJdbc();
        return new ZahNalogObavijestRs(dao.daoFind(value));
    }
    public ZahNalogObavijestVo dodajObavijestNaloga(ZahNalogObavijestVo value)  {
        PoZahObavijestNalogaJdbc dao = new PoZahObavijestNalogaJdbc();
        dao.daoCreate(value);
        return value;
    }
    public ZahNalogObavijestVo brisiObavijestNaloga(ZahNalogObavijestVo value)  {
        PoZahObavijestNalogaJdbc dao = new PoZahObavijestNalogaJdbc();
        dao.daoRemove(value);
        return value;
    }
    public ZahNalogObavijestVo brisiViseObavijestiNaloga(ZahNalogObavijestRs value)  {
        PoZahObavijestNalogaJdbc dao = new PoZahObavijestNalogaJdbc();
        dao.daoRemoveMany(value);
        return new ZahNalogObavijestVo();
    }

}
