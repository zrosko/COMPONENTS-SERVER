package hr.adriacomsoftware.app.server.rizik.izlozenost.facade;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaRs;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaVo;
import hr.adriacomsoftware.app.common.rizik.izlozenost.facade.IzlozenostFacade;
import hr.adriacomsoftware.app.server.gk.da.jdbc.GlavnaKnjigaJdbc;
import hr.adriacomsoftware.app.server.rizik.izlozenost.da.jdbc.KrTransakcijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class IzlozenostFacadeServer extends AS2FacadeServerLayer 
		implements IzlozenostFacade {

	private static IzlozenostFacadeServer _instance = null;
	public static IzlozenostFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new IzlozenostFacadeServer();
		}
		return _instance;
	}
	private IzlozenostFacadeServer(){
		AS2Context.setSingletonReference(this);
	}

    public KrTransakcijaRs procitajIzlozenosti(KrTransakcijaVo value)  {
        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
        if(value.exists("vrsta"))
        	return dao.daoListaTransakcijaPremaNadleznosti(value);
        else
        	return dao.daoListaTransakcija(value);
    }
    public KrTransakcijaRs pronadiIzlozenosti(KrTransakcijaVo value)  {
        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
        return dao.daoPronadiIzlozenosti(value, true);
    }
    public KrTransakcijaRs procitajZaduzenostiOsobe(KrTransakcijaVo value)  {
        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
        KrTransakcijaRs rs = new KrTransakcijaRs();
        rs = dao.daoZaduzenostKlijentaNaDan(value);
        if(value.get("jmbg").length()==0 && value.get("maticni_broj").length()>0){ //8.1.2014.
        //if(!value.get("vrsta").equals("GRADANIN")){ //samo za pravne osobe
	        //3.4.2013. zrosko dodatak ostalih zaduženja bez partije
	        GlavnaKnjigaJdbc dao_gk = new GlavnaKnjigaJdbc();
	        rs.appendRowsOnly(dao_gk.daoZaduzenostBezPartije(new OsnovniVo(value)));
			//rs.addResultSet("ukupno", dao.daoZaduzenostUkupnoNaDan(value));
			//if(value.get("@povezane_osobe").equals("DA")){
			//rs.addResultSet("ukupno", dao.daoZaduzenostUkupnoNaDan(value));
			//}
        }
        return rs;
    }
    /* Zaduzenosti rizične skupine B i C). */
    public KrTransakcijaRs procitajZaduzenostiNaplate(KrTransakcijaVo value)  {
        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
        KrTransakcijaRs rs = new KrTransakcijaRs();
        rs = dao.daoZaduzenostKlijentaNaplate(value);
        return rs;
    }
    public KrTransakcijaRs procitajPragMaterijalnostiPO(KrTransakcijaVo value)  {
        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
        KrTransakcijaRs rs = new KrTransakcijaRs();
        rs = dao.daoPragMaterijalnostiPO(value);
        return rs;
    }
	public OsnovniRs procitajJamstveniKapital(OsnovniVo value)  {
		KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
		return dao.daoJamstveniKapital(value);
	}
}
