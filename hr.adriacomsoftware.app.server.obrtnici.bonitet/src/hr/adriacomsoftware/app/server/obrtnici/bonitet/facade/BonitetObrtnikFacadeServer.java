/* (c) Adriacom Software d.o.o.
 * 22211 Vodice, Croatia
 * Created by Z.Rosko (zrosko@yahoo.com)
 * Date 2010.05.04 
 * Time: 11:52:48
 */
package hr.adriacomsoftware.app.server.obrtnici.bonitet.facade;

import hr.adriacomsoftware.app.common.obrtnici.bonitet.dto.BonitetObrtnikRs;
import hr.adriacomsoftware.app.common.obrtnici.bonitet.dto.BonitetObrtnikVo;
import hr.adriacomsoftware.app.common.obrtnici.bonitet.facade.BonitetObrtnikFacade;
import hr.adriacomsoftware.app.common.pravneosobe.obrasci.dto.VikrStavkaRs;
import hr.adriacomsoftware.app.common.pravneosobe.obrasci.dto.VikrStavkaVo;
import hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc.BonitetObrtnikJdbc;
import hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc.VikrKlijentJdbc;
import hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc.VikrKlijentStavkaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public class BonitetObrtnikFacadeServer extends AS2FacadeServerLayer 
		implements BonitetObrtnikFacade {

	private static BonitetObrtnikFacadeServer _instance = null;
	public static BonitetObrtnikFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new BonitetObrtnikFacadeServer();
		}
		return _instance;
	}
	private BonitetObrtnikFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public BonitetObrtnikRs citajSveBonitete(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
        return dao.daoPronadiBonitete(value,BonitetObrtnikJdbc.VRSTA_SVE);
    }
    public BonitetObrtnikRs pronadiSveBonitete(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
        return dao.daoPronadiBonitete(value,BonitetObrtnikJdbc.VRSTA_TRAZI_SVE);
    }
    public BonitetObrtnikRs citajSveBoniteteZaObrtnika(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
        if(value.getMaticniBroj().length()>0 || value.getOib().length()>0){
            return dao.daoPronadiBonitete(value,BonitetObrtnikJdbc.VRSTA_TRAZI_OBRTNIKA);
        }else{
            return new BonitetObrtnikRs();            
        }
    }
    public BonitetObrtnikVo dodajBonitet(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
        if(dao.daoFindIfExists(value))
            throw new AS2Exception("12501");
        dao.daoCreate(value);
        return value;
    }
    public BonitetObrtnikVo azurirajBonitet(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
        if(dao.daoFindIfExists(value))
            throw new AS2Exception("12501");
		dao.daoStore(value);
		return value;
    }
    public BonitetObrtnikVo brisiBonitet(BonitetObrtnikVo value)  {
        BonitetObrtnikJdbc dao = new BonitetObrtnikJdbc();
		dao.daoRemove(value);
		return value;
    }
    public VikrStavkaRs citajSveVikrZaKlijenta(VikrStavkaVo value)  {
        VikrKlijentStavkaJdbc dao = new VikrKlijentStavkaJdbc();
        return dao.daoCitajSveVikrZaKlijenta(value);
    }
    public VikrStavkaVo dodajVikrZaKlijenta(VikrStavkaVo value)  {
        VikrKlijentJdbc dao_vikr = new VikrKlijentJdbc();
        VikrKlijentStavkaJdbc dao_vikr_stavka = new VikrKlijentStavkaJdbc();
        if(!dao_vikr.daoFindIfExists(value)){
            dao_vikr.daoCreate(value);
            String id_vikr_klijenta = dao_vikr.daoZadnjiIdVikrKlijenta(value);//TODO find last used
            value.set("id_vikr_klijenta", id_vikr_klijenta);
        }        
        dao_vikr_stavka.daoCreate(value);
        return value;
    }
    public VikrStavkaVo azurirajVikrZaKlijenta(VikrStavkaVo value)  {
        VikrKlijentJdbc dao_vikr = new VikrKlijentJdbc();
        dao_vikr.daoStore(value);
        VikrKlijentStavkaJdbc dao = new VikrKlijentStavkaJdbc();
        dao.daoStore(value);
        return value;
    }
    public VikrStavkaVo brisiVikrZaKlijenta(VikrStavkaVo value)  {
        VikrKlijentStavkaJdbc dao = new VikrKlijentStavkaJdbc();
        dao.daoRemove(value);
        return value;
    }
}
