package hr.adriacomsoftware.app.server.kolaterali.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPartijaRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPartijaVo;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPolicaRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPolicaVo;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralVo;
import hr.adriacomsoftware.app.common.kolaterali.facade.KolateraliFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeVo;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.OsobaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PartijaGradanaJdbc;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.BsaKolateralJdbc;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.KolateralJdbc;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.KolateralPartijaJdbc;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.KolateralPolicaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PartijaPravnihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PravnaOsobaJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.util.Iterator;


public final class KolateraliFacadeServer extends AS2FacadeServerLayer 
		implements KolateraliFacade {

	private static KolateraliFacadeServer _instance = null;
	public static KolateraliFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new KolateraliFacadeServer();
		}
		return _instance;
	}
	private KolateraliFacadeServer(){
	}
    public KolateralRs citajSveKolaterale(KolateralVo value)  {
        KolateralJdbc dao = new KolateralJdbc();
        return dao.daoListaKolaterala(value);
    }
    public KolateralRs pronadiSveKolaterale(KolateralVo value)  {
        KolateralJdbc dao = new KolateralJdbc();
        return dao.daoPronadiKolaterale(value);
    }
    public KolateralPolicaRs pronadiSvePolice(KolateralPolicaVo value)  {
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
        KolateralPolicaRs rs = new KolateralPolicaRs(dao.daoPronadiPolice(value));
		return rs;
    }
    public KolateralRs citajSvePolice(KolateralVo value)  {
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
        KolateralRs rs = new KolateralRs(dao.daoListaPolica(value));
		return rs;
    } 
    public KolateralRs citajKolateral(KolateralVo value)  {
        KolateralJdbc dao_kolateral = new KolateralJdbc();
        KolateralRs rs_kolatral =  new KolateralRs(dao_kolateral.daoLoad(value));
        rs_kolatral.addResultSet("kol_nekretnine", rs_kolatral);
        //sve partije za kolateral
        KolateralPartijaJdbc dao_partije = new KolateralPartijaJdbc();
        KolateralPartijaRs rs_partije = new KolateralPartijaRs(dao_partije.daoFind(value));
        rs_kolatral.addResultSet("kol_partije", rs_partije);
        //sve police za kolateral
        KolateralPolicaJdbc dao_polica = new KolateralPolicaJdbc();
        KolateralPolicaRs rs_police = new KolateralPolicaRs(dao_polica.daoFind(value));
        rs_kolatral.addResultSet("kol_police", rs_police);
		return rs_kolatral;
    }
    public KolateralVo azurirajKolateral(KolateralVo value)  {
        OsobaJdbc dao_osoba = new OsobaJdbc();
        OsobaVo osoba_vo = new OsobaVo();
        osoba_vo.setJmbg(value.getMaticniBrojJmbg());
        if(!dao_osoba.daoFindIfExists(osoba_vo)){
            PravnaOsobaJdbc dao_pravna_osoba = new PravnaOsobaJdbc();
            PravnaOsobaVo pravna_osoba_vo = new PravnaOsobaVo();
            pravna_osoba_vo.setMaticniBroj(value.getMaticniBrojJmbg());
            if(!dao_pravna_osoba.daoFindIfExists(pravna_osoba_vo)){
                throw new AS2Exception("10008");
            }
        }
        KolateralJdbc dao = new KolateralJdbc();
        value.setIspravno("1");
		dao.daoStore(value);
		return value;
    }
    public KolateralVo dodajKolateral(KolateralVo value)  {
        value.setIspravno("1");
        KolateralJdbc dao = new KolateralJdbc();
		dao.daoCreate(value);
		return value;
    }
    public KolateralVo brisiKolateral(KolateralVo value)  {
        value.setIspravno("0");
        KolateralJdbc dao = new KolateralJdbc();
		dao.daoStore(value);
		return value;
    }
    public KolateralPolicaVo azurirajPolicuZaKolateral(KolateralPolicaVo value)  {
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
        value.setIspravno("1");
		dao.daoStore(value);
		return value;
    }
    public KolateralPolicaRs procitajSvePoliceZaKolateral(KolateralPolicaVo value)  {
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
        KolateralPolicaRs rs = new KolateralPolicaRs(dao.daoFind(value));
		return rs;
    }
    public KolateralPolicaVo dodajPolicuZaKolateral(KolateralPolicaVo value)  {        
        if(value.getBrojPartije().length()>0){
            PartijaPravnihOsobaJdbc dao_partija = new PartijaPravnihOsobaJdbc();
	        if(!dao_partija.daoFindIfExists(new OsnovniVo(value))){
	            PartijaGradanaJdbc dao_gr_partija = new PartijaGradanaJdbc();
	            if(!dao_gr_partija.daoFindIfExists(new OsnovniVo(value)))
	                throw new AS2Exception("10018");
	        }
        }
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
        value.setIspravno("1");
		dao.daoCreate(value);
		return value;
    }
    public KolateralPolicaVo brisiPolicuZaKolateral(KolateralPolicaVo value)  {
        KolateralPolicaJdbc dao = new KolateralPolicaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public KolateralPartijaVo azurirajPartijuZaKolateral(KolateralPartijaVo value)  {
        KolateralPartijaJdbc dao = new KolateralPartijaJdbc();
        value.setIspravno("1");
		dao.daoStore(value);
		return value;
    }
    public KolateralPartijaRs procitajSvePartijeZaKolateral(KolateralPartijaVo value)  {
        KolateralPartijaJdbc dao = new KolateralPartijaJdbc();
        KolateralPartijaRs rs = new KolateralPartijaRs(dao.daoFind(value));
		return rs;
    }
    public KolateralPartijaVo dodajPartijuZaKolateral(KolateralPartijaVo value)  {
        //za terete drugih banaka ne treba partija
        if(value.getTeretDrugaBanka().equals("0")){
	        PartijaPravnihOsobaJdbc dao_partija = new PartijaPravnihOsobaJdbc();
	        if(!dao_partija.daoFindIfExists(new OsnovniVo(value))){
	            PartijaGradanaJdbc dao_gr_partija = new PartijaGradanaJdbc();
	            if(!dao_gr_partija.daoFindIfExists(new OsnovniVo(value)))
	                throw new AS2Exception("10018");
	        }
        }
        KolateralPartijaJdbc dao = new KolateralPartijaJdbc();
        if(dao.daoFindIfExists(value))
            throw new AS2Exception("11002");
        value.setIspravno("1");
		dao.daoCreate(value);
		return value;
    }
    public KolateralPartijaVo brisiPartijuZaKolateral(KolateralPartijaVo value)  {
        KolateralPartijaJdbc dao = new KolateralPartijaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public KolateralPartijaRs procitajSveZaduzenostiKodBankePOVIJEST(KolateralPartijaVo value)  {
    	PartijaPravnihOsobaJdbc dao_zaduzenost = new PartijaPravnihOsobaJdbc();
        KolateralPartijaRs rs = new KolateralPartijaRs(dao_zaduzenost.daoListaSvihZaduzenosti(new ZaduzenostKodBankeVo(value)));
        KolateralPartijaJdbc dao = new KolateralPartijaJdbc();   
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            KolateralPartijaVo vo = (KolateralPartijaVo)E.next();
            vo.setIdKolaterala(value.getIdKolaterala());
            vo.setUcitano("1");
            vo.setIspravno("1");
            vo.setDatumUpisa(vo.getAsCalendar("datum_odobravanja"));
            vo.setDatumSalda(AS2Date.getTodayAsCalendar());
            if(vo.get("valuta").equals("HRK"))
                vo.setValutaTereta("191");
            else if(vo.get("valuta").equals("EUR"))
                vo.setValutaTereta("978");
        }
        dao.daoCreateMany(rs);
        rs = new KolateralPartijaRs(dao.daoFind(value));
		return rs;
    }
    public AS2RecordList procitajVrsteKolaterala()  {
        KolateralJdbc dao = new KolateralJdbc();
        return dao.daoFinVrsteKolaterala();
    }  
    public KolateralRs procitajSveBsaKolateraleOsobe(KolateralVo value)  {
        BsaKolateralJdbc dao = new BsaKolateralJdbc();
        if(value.getMaticniBroj().length()>0)
        	value.setMaticniBrojJmbg(value.getMaticniBroj());
        else if(value.getJmbg().length()>0)
        	value.setMaticniBrojJmbg(value.getJmbg());
        return dao.daoListaKolateralaZaOsobu(value);
    }
}
