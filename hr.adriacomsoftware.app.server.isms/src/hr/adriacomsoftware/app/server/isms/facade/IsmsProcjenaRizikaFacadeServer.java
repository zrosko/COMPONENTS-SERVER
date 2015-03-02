package hr.adriacomsoftware.app.server.isms.facade;

import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKopijaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKopijaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKorisnikRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKorisnikVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikKontrolaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikKontrolaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikVo;
import hr.adriacomsoftware.app.common.isms.facade.IsmsProcjenaRizikaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsImovinaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsImovinaKopijaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsImovinaKorisnikJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsRizikJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsRizikKontrolaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.util.Calendar;
import java.util.Iterator;


public final class IsmsProcjenaRizikaFacadeServer extends AS2FacadeServerLayer
		implements IsmsProcjenaRizikaFacade {

	private static IsmsProcjenaRizikaFacadeServer _instance = null;
	public static IsmsProcjenaRizikaFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new IsmsProcjenaRizikaFacadeServer();
		}
		return _instance;
	}
	private IsmsProcjenaRizikaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	//imovina
    public IsmsImovinaRs citajSvuImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
        IsmsImovinaRs rs = new IsmsImovinaRs(dao.daoListaImovine(value));
		return rs;
    }
    public IsmsImovinaRs pronadiSvuImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
        IsmsImovinaRs rs = new IsmsImovinaRs(dao.daoPronadiImovinu(value));
		return rs;
    }
    public IsmsImovinaVo citajImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
		dao.daoLoad(value);
		return value;
    }
    public IsmsImovinaVo azurirajImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsImovinaVo dodajImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsImovinaVo brisiImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
        IsmsImovinaVo vo = new IsmsImovinaVo();
        vo.setIdImovine(value.getIdImovine());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public IsmsImovinaVo duplicirajImovinu(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
        value.setIdImovine(null);
		dao.daoCreate(value);
		return value;
    }
    //kopije imovine
    public IsmsImovinaKopijaRs procitajSveKopijeImovine(IsmsImovinaKopijaVo value)  {
        IsmsImovinaKopijaJdbc dao = new IsmsImovinaKopijaJdbc();
        IsmsImovinaKopijaRs rs = new IsmsImovinaKopijaRs(dao.daoFind(value));
		return rs;
    }
    public IsmsImovinaKopijaVo azurirajKopijuImovine(IsmsImovinaKopijaVo value)  {
        IsmsImovinaKopijaJdbc dao = new IsmsImovinaKopijaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsImovinaKopijaVo dodajKopijuImovine(IsmsImovinaKopijaVo value)  {
        IsmsImovinaKopijaJdbc dao = new IsmsImovinaKopijaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsImovinaKopijaVo brisiKopijuImovine(IsmsImovinaKopijaVo value)  {
        IsmsImovinaKopijaJdbc dao = new IsmsImovinaKopijaJdbc();
		dao.daoRemove(value);
		return value;
    }
    //korisnik imovine
    public IsmsImovinaKorisnikRs procitajSveKorisnikeImovine(IsmsImovinaKorisnikVo value)  {
        IsmsImovinaKorisnikJdbc dao = new IsmsImovinaKorisnikJdbc();
        IsmsImovinaKorisnikRs rs = new IsmsImovinaKorisnikRs(dao.daoFind(value));
		return rs;
    }
    public IsmsImovinaKorisnikVo azurirajKorisnikaImovine(IsmsImovinaKorisnikVo value)  {
        IsmsImovinaKorisnikJdbc dao = new IsmsImovinaKorisnikJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsImovinaKorisnikVo dodajKorisnikaImovine(IsmsImovinaKorisnikVo value)  {
        IsmsImovinaKorisnikJdbc dao = new IsmsImovinaKorisnikJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsImovinaKorisnikVo brisiKorisnikaImovine(IsmsImovinaKorisnikVo value)  {
        IsmsImovinaKorisnikJdbc dao = new IsmsImovinaKorisnikJdbc();
		dao.daoRemove(value);
		return value;
    }    
    public IsmsRizikVo azurirajRizik(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsRizikVo dodajRizik(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsRizikVo brisiRizik(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
		dao.daoRemove(value);
		return value;
    }
    public IsmsRizikVo duplicirajRizik(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        value.setIdRizika(null);
		dao.daoCreate(value);
		return value;
    }
    public IsmsRizikRs pronadiSveRizike(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        IsmsRizikRs rs = new IsmsRizikRs(dao.daoPronadiRizike(value));
		return rs;
    }
    public IsmsRizikRs procitajSveRizikeZaImovinu(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        IsmsRizikRs rs = new IsmsRizikRs(dao.daoFind(value));
		return rs;
    }
    public IsmsRizikRs procitajSveRizike(IsmsRizikVo value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        IsmsRizikRs rs = new IsmsRizikRs(dao.daoListaRizika(value));
		return rs;
    }
    public void potvrdiProcjenuVrijednostiImovine(IsmsImovinaRs value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
        String korisnik = AS2SessionFactory.getInstance().getCurrentUser().getUserName();
        Calendar datum = AS2Date.getTodayAsCalendar();
		Iterator<AS2Record> E = value.getRows().iterator();
		while(E.hasNext()){
		    AS2Record vo = E.next();
			IsmsImovinaVo vo_imovina = new IsmsImovinaVo();
			vo_imovina.setIdImovine(vo.get(IsmsImovinaVo.ISMS_IMOVINA__ID_IMOVINE));
			vo_imovina.setPotvrdaVrijednosti(value.get(IsmsImovinaVo.ISMS_IMOVINA__POTVRDA_VRIJEDNOSTI));
			vo_imovina.setPotvrdaVrijednostiDatum(datum);
			vo_imovina.setPotvrdaVrijednostiKorisnik(korisnik);
			dao.daoStore(vo_imovina);
		}
        
    }
    public void potvrdiProcjenuRazineRizika(IsmsRizikRs value)  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        String korisnik = AS2SessionFactory.getInstance().getCurrentUser().getUserName();
        Calendar datum = AS2Date.getTodayAsCalendar();
        Iterator<AS2Record> E = value.getRows().iterator();
		while(E.hasNext()){
		    AS2Record vo = E.next();
			IsmsRizikVo vo_rizik = new IsmsRizikVo();
			vo_rizik.setIdRizika(vo.get(IsmsRizikVo.ISMS_RIZIK__ID_RIZIKA));
			vo_rizik.setPotvrdaRazineRizika(value.get(IsmsRizikVo.ISMS_RIZIK__POTVRDA_RAZINE_RIZIKA));
			vo_rizik.setPotvrdaRazineRizikaDatum(datum);
			vo_rizik.setPotvrdaRazineRizikaKorisnik(korisnik);
			dao.daoStore(vo_rizik);
		}        
    }
    public void arhivirajProcjenuRazineRizika()  {
        IsmsRizikJdbc dao = new IsmsRizikJdbc();
        OsnovniVo vo_poziv = new OsnovniVo();
        vo_poziv.setDatum(AS2Date.getTodayAsCalendar());
        dao.daoCallSpDatum(vo_poziv, "isms_rizik_procjena");
        
    }
    public IsmsRizikKontrolaRs procitajSvePredlozeneKontroleZaRizik(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
        IsmsRizikKontrolaRs rs = new IsmsRizikKontrolaRs(dao.daoFind(value));
		return rs;
    }
    public IsmsRizikKontrolaVo azurirajKontroluZaRizik(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsRizikKontrolaVo dodajKontroluZaRizik(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsRizikKontrolaVo brisiKontroluZaRizik(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public IsmsRizikKontrolaRs procitajSveKontroleZaSmanjivanjeRizika(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
        IsmsRizikKontrolaRs rs = new IsmsRizikKontrolaRs(dao.daoListaMjera(new IsmsRizikVo(value)));
		return rs;
    }
    public IsmsRizikKontrolaRs pronadiSveKontroleZaSmanjivanjeRizika(IsmsRizikKontrolaVo value)  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
        IsmsRizikKontrolaRs rs = new IsmsRizikKontrolaRs(dao.daoPronadiMjere(new IsmsRizikVo(value)));
		return rs;
    }
    public void arhivirajSmanjenjeRizika()  {
        IsmsRizikKontrolaJdbc dao = new IsmsRizikKontrolaJdbc();
        OsnovniVo vo = new OsnovniVo();
        vo.setDatum(AS2Date.getTodayAsCalendar());
        dao.daoCallSpDatum(vo, "isms_rizik_smanjenje");
    }
    public void azurirajVlasnikaImovine(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
		//dao.daoStoreVlasnika(value);
        dao.daoStoreOrgJedinicu(value);
    }
    public void azurirajKategorijuImovine(IsmsImovinaVo value)  {
        IsmsImovinaJdbc dao = new IsmsImovinaJdbc();
     	dao.daoStoreKategoriju(value);        
    }
}
