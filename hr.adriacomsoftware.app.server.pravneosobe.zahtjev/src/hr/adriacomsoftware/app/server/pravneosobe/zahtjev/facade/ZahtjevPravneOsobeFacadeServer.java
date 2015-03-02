package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.facade;

import hr.adriacomsoftware.app.common.cmdb.dto.EmailVo;
import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.ZahtjevPrivitakRs;
import hr.adriacomsoftware.app.common.jb.dto.ZahtjevPrivitakVo;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.BankarskiProizvodRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.BankarskiProizvodVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.Bon2Rs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.Bon2Vo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KolateralPonudeniRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KolateralPonudeniVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KratkorocniPlanRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KratkorocniPlanVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KupacRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KupacVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.OsiguranjeVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PodaciKlijentaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PoslovniOdnosBankaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PoslovniOdnosBankaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PovezanaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikKomentarRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikKomentarVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.StatusZahtjevaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.UrednostVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.VikrRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.VikrVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.facade.ZahtjevPravneOsobeFacade;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaVo;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.BsaKolateralJdbc;
import hr.adriacomsoftware.app.server.kolaterali.da.jdbc.KolateralJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc.BonitetJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.JrrJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PartijaPravnihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktSpecifikacijaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahBankarskiProizvodJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahBon2Jdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahBonitetJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahDobavljacJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahKolateralPonudeniJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahKratkorocniPlanJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahKupacJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahOcjenaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahOsiguranjeJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahPodaciKlijentaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahPoslovniOdnosBankaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahPovezanaOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahPrivitakJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahRizikJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahStatusZahtjevaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahUrednostJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahVikrJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahZaduzenostJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahZaduzenostKodBankeJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahZahtjevJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.RizikKomentarJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.email.AS2EmailConstants;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.common.types.AS2String;
import hr.as2.inf.common.validations.AS2ValidatorService;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.util.Iterator;


public final class ZahtjevPravneOsobeFacadeServer extends AS2FacadeServerLayer 
		implements ZahtjevPravneOsobeFacade {

	private static ZahtjevPravneOsobeFacadeServer _instance = null;
	public static ZahtjevPravneOsobeFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new ZahtjevPravneOsobeFacadeServer();
		}
		return _instance;
	}
	private ZahtjevPravneOsobeFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	//provjera autorizacije za metode unutar ove klase
	public void checkServiceSecurity(AS2RecordList value) {
	    AS2Record vo = new AS2Record(value.getProperties());
	    checkServiceSecurity(vo);
	}
	public void checkServiceSecurity(AS2Record value) {
	    AS2User _user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
	    String _aplication = _user.getApplication();
	    //String _component = value.getRemoteObject();
	    String _service = value.getRemoteMethod();
	    String _role = _user.get("role_id");
	    String _function = _user.get("function_id");
	    String _id_osobe = _user.get("id_osobe");
	    //String _korisnik = _user.get("korisnik");	   
	    if(!_role.equals("1")&&!_role.equals("2")&&!_role.equals("7")){//not administrator
	        if(_aplication.equals("zah")){		        
		        if(_function.equals("2")){//PO Zahtjev            
		            if(_service.startsWith("dodaj")||
		               _service.startsWith("azuriraj")||
		               _service.startsWith("brisi")||
		               _service.equals("ponavljanjeObradeNaDatumObrade")){
		                    PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
		                    String _zaprimatelj = dao.daoZaprimateljZahtjeva(value.get("broj_zahtjeva"));
	                	    if(_zaprimatelj!=null){
		                	   if(!_id_osobe.equals(_zaprimatelj)){//changes not allowed
		                	       throw new AS2Exception("12804");
		                	 }
	                	} 
		            }			                
			    }
	        }
	    }
	}
    public ZahtjevPravnaOsobaRs pronadiSveZahtjeve(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        return dao.daoPronadiZahtjeve(value, true);
    }
    public ZahtjevPravnaOsobaRs citajSveZahtjeve(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        return dao.daoPronadiZahtjeve(value, false);
    }
   public ZahtjevPravnaOsobaVo dodajZahtjev(ZahtjevPravnaOsobaVo value)  {
        //PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        return new ZahtjevPravnaOsobaVo();
    }
    public ZahtjevPravnaOsobaVo citajZahtjev(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo vo = dao.daoCitajOsnovnePodatkeZahtjeva(value);
        return vo;
    }
    public ZahtjevPravnaOsobaVo brisiZahtjev(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo();
        za_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        za_vo.setIspravnoNE();
        dao.daoStore(za_vo);
        return za_vo;
    }
    public ZahtjevPravnaOsobaVo ponavljanjeObradeNaDatumObrade(ZahtjevPravnaOsobaVo value)  { 
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo();
        za_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        za_vo.setDatumObrade(value.getDatumObrade());
        dao.daoStore(za_vo);
        //obrada poslovnog odnosa sa bankom, zaduženosti, povezanih osoba i njihovih zaduzenosti
        if(AS2String.ifExists(value.get("@@IzborObrade"), "ZKB")){
            procitajSveZaduzenostiKodBankePOVIJEST(new ZaduzenostKodBankeVo(value));
        }
        if(AS2String.ifExists(value.get("@@IzborObrade"), "POZ")){
            procitajZaduzenostPovezanihOsobaPOVIJEST(new PovezanaOsobaVo(value));
        } 
        if(AS2String.ifExists(value.get("@@IzborObrade"), "POB")){
	        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
	        PoslovniOdnosBankaVo posl_odnos =  dao_po.daoCitajPOVIJEST(value);
	        posl_odnos.setBrojZahtjeva(value.getBrojZahtjeva());
	
	        PoslovniOdnosBankaVo for_read = new PoslovniOdnosBankaVo();
	        for_read.setBrojZahtjeva(value.getBrojZahtjeva());
	        for_read = dao_po.daoLoad(for_read);
	        //07.02.2014. zrosko greška kod provjere da li postoji odnos
	        if(for_read.getBrojZahtjeva().length()>0)
	            dao_po.daoStore(posl_odnos);
	        else
	            dao_po.daoCreate(posl_odnos);
        }

        return za_vo;
    }
    public ZahtjevPravnaOsobaVo azurirajOdobrenjeZahtjeva(ZahtjevPravnaOsobaVo value)  {        
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo();
        za_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        za_vo.setStatusZahtjeva(value.getStatusZahtjeva());
        za_vo.setObrazlozenje(value.getObrazlozenje());
        dao.daoStore(za_vo);
        return za_vo;
    }
    public ZahtjevPravnaOsobaVo azurirajStatusZahtjeva(ZahtjevPravnaOsobaVo value)  {        
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo();
        za_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        za_vo.setHitnost(value.getHitnost());
        za_vo.setStatusZahtjeva(value.getStatusZahtjeva());
        za_vo.setDatumZaprimanja(value.getDatumZaprimanja());
        za_vo.setDatumObrade(value.getDatumObrade());
        dao.daoStore(za_vo);
        return za_vo;
    }
    public ZahtjevPravnaOsobaVo obradaZahtjeva(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        dao.daoStore(value);
        return value;
    }
    public EmailVo proslijediZahtjev(EmailVo value)  {
        PoZahStatusZahtjevaJdbc dao = new PoZahStatusZahtjevaJdbc();
        StatusZahtjevaVo vo = new StatusZahtjevaVo();
        vo.setEmail(value.getTo());
        vo.setBrojZahtjeva(value.getIdObjekta());
        vo.setDatumPromjene(AS2Date.getTodayAsCalendar());
        vo.setOperater(value.get("zadnja_promjena_user_id"));
        vo.setStatusZahtjeva("0"); 
        vo.setPoruka(value.getBody());
        //TODO status
        dao.daoCreate(vo);
        //šalji email poruku
        StringBuffer sb = new StringBuffer();
        sb.append("Poštovani,\n");
        sb.append("Vama je proslijeđen zahtjev br: "+value.getIdObjekta()+" na obradu.\n");
        sb.append("Prijavite se u aplikaciju za obradu zahtjeva (pritiskom na link ispod).\n");
        sb.append("http://portal:8080/adriacom/client/zahtjev.jnlp\n");
        sb.append("Srdačan pozdrav,\n"); 
        sb.append(value.getFrom());
        sb.append("\nP.S.\n"); 
        sb.append(value.getBody()); 
        sb.append("\nInterni portal Jadranske banke d.d. Šibenik");
        //ažuriraj zahtjev
        PoZahZahtjevJdbc dao_zahtjev = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo email = new ZahtjevPravnaOsobaVo();
        email.setBrojZahtjeva(value.getIdObjekta());
        email.setEmailDjelatnika(value.getTo());
        if(!value.get("@tip_prosljedivanja").equals("samo_proslijedi")){
            email.setZaprimatelj(value.getIdOsobe());//spremi osobu kao zaprimatelja
        }       
        dao_zahtjev.daoStore(email);
        //send email procedura
        email.set(AS2EmailConstants.RECIPIENTS, value.getTo());
        email.set(AS2EmailConstants.BODY, sb.toString());
        email.set(AS2EmailConstants.SUBJECT, "Zahtjev br:"+value.getIdObjekta());
        email.set(AS2EmailConstants.PROFILE_NAME,"Portal");
        email.set(AS2EmailConstants.IZBOR,value.getIzbor());
        dao_zahtjev.daoSendEmail(email);
        return value;
    }
    public ZahtjevPravnaOsobaVo pripremiZahtjev(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        return dao.daoPripremiZahtjev(value);
    }
    public ZahtjevPravnaOsobaVo pripremiOsnovnePodatke(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        return dao.daoPripremiOsnovnePodatke(value);
    }
    public PoslovniOdnosBankaVo citajPoslovniOdnosBankaPOVIJEST(ZahtjevPravnaOsobaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        return dao_po.daoCitajPOVIJEST(value);
    }
    public PoslovniOdnosBankaRs citajStednjuPovezanihOsoba(ZahtjevPravnaOsobaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        return dao_po.daoCitajStednjuPovezanihOsoba(value);
    }
    public PodaciKlijentaVo dodajOsnovnePodatke(PodaciKlijentaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo(value);
        AS2RecordList br_zah = dao.daoSlijedeciBrojZahtjeva();
        za_vo.setBrojZahtjeva(br_zah.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
        za_vo.setDatumObrade(za_vo.getDatumZaprimanja()); //9.11.2009. Datum zaprimanja isti kao i datum obrade.
        za_vo.setDatumProcjeneRizika(za_vo.getDatumZaprimanja()); //29.1.2010. Datum zaprimanja isti kao i datum obrade.
        dao.daoCreate(za_vo);
        //podaci klijenta
        PoZahPodaciKlijentaJdbc dao_klijent = new PoZahPodaciKlijentaJdbc();
        value.setBrojZahtjeva(za_vo.getBrojZahtjeva());
        dao_klijent.daoCreate(value);
        if(value.exists("@@povezi_specifikaciju")){//dodaj broj zahtjeva u specifikaciju
            PoFaktSpecifikacijaJdbc dao_spec = new PoFaktSpecifikacijaJdbc();             
            value.set("id_spec",value.get("@@povezi_specifikaciju"));
            dao_spec.daoStore(value);            
        }
        return value;
    }
    public PodaciKlijentaVo azurirajOsnovnePodatke(PodaciKlijentaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo(value);
        dao.daoStore(za_vo);
        PoZahPodaciKlijentaJdbc dao_klijent = new PoZahPodaciKlijentaJdbc();
        dao_klijent.daoStore(value);
        return value;
    }
    public PoslovniOdnosBankaVo dodajPoslovniOdnosBanka(PoslovniOdnosBankaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        dao_po.daoCreate(value);
        return value;
    }
    public PoslovniOdnosBankaVo azurirajPoslovniOdnosBanka(PoslovniOdnosBankaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        dao_po.daoRemove(value);
        dao_po.daoCreate(value);
        return value;
    }
    public PoslovniOdnosBankaVo citajPoslovniOdnosBanka(ZahtjevPravnaOsobaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        PoslovniOdnosBankaVo for_read = new PoslovniOdnosBankaVo();
        for_read.setBrojZahtjeva(value.getBrojZahtjeva());
        return new PoslovniOdnosBankaVo(dao_po.daoLoad(for_read));
    }
    public Bon2Vo azurirajBon2(Bon2Vo value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
		dao.daoStore(value);
		return value;
    }
    public Bon2Rs procitajSveBon2(Bon2Vo value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
        Bon2Rs rs = new Bon2Rs(dao.daoFind(value));
		return rs;
    }
    public Bon2Vo dodajBon2(Bon2Vo value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
		dao.daoCreate(value);
		return value;
    }
    public Bon2Vo brisiBon2(Bon2Vo value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
		dao.daoRemove(value);
		return value;
    }
    public Bon2Rs procitajSveBon2POVIJEST(Bon2Vo value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
        value.setDatum(value.getRazdobljeDo());
        Bon2Rs j2eers = new Bon2Rs(dao.daoListaSvihBon2Podataka((new Bon2Vo(value))));
        Iterator<AS2Record> E = j2eers.iteratorRows();
        while(E.hasNext()){
            Bon2Vo vo = (Bon2Vo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
        }        
        dao.daoCreateMany(j2eers);
        Bon2Rs rs = new Bon2Rs(dao.daoFind(value));
		return rs;
    }
    public KupacVo azurirajKupca(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
		dao.daoStore(value);
		return value;
    }
    public KupacRs procitajSveKupce(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
        KupacRs rs = new KupacRs(dao.daoFind(value));
		return rs;
    }
    public KupacVo dodajKupca(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
		dao.daoCreate(value);
		return value;
    }
    public KupacVo brisiKupca(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
		dao.daoRemove(value);
		return value;
    }
    public KupacVo brisiSveKupce(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
		dao.daoRemoveSveKupceZaZahtjev(value);
        return value;
    }
    public DobavljacVo azurirajDobavljaca(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
		dao.daoStore(value);
		return value;
    }
    public DobavljacRs procitajSveDobavljace(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
        DobavljacRs rs = new DobavljacRs(dao.daoFind(value));
		return rs;
    }
    public DobavljacVo dodajDobavljaca(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
		dao.daoCreate(value);
		return value;
    }
    public DobavljacVo brisiDobavljaca(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
		dao.daoRemove(value);
		return value;
    }
    public DobavljacVo brisiSveDobavljace(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
		dao.daoRemoveSveDobavljaceZaZahtjev(value);
        return value;
    }
    public PovezanaOsobaVo azurirajPovezanuOsobu(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
		dao.daoStore(value);
		return value;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobe(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        PovezanaOsobaRs rs;
        if(value.exists(ZAHDataDictionary.PO_ZAH_POVEZANA_OSOBA__SLOG)){
            rs = new PovezanaOsobaRs(dao.daoFindPoVrstiSloga(value));
        }else{
            rs = new PovezanaOsobaRs(dao.daoFind(value));
        }
		return rs;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobeVlasnike(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFind(value,true));
		return rs;
    }
    public PovezanaOsobaVo dodajPovezanuOsobu(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public PovezanaOsobaVo brisiPovezanuOsobu(PovezanaOsobaVo value)  {
        //prvo bisi zaduzenosti osobe
        PoZahZaduzenostKodBankeJdbc dao_zaduzenost = new PoZahZaduzenostKodBankeJdbc();
        ZaduzenostKodBankeVo vo_zaduzenost = new ZaduzenostKodBankeVo();
        vo_zaduzenost.setBrojZahtjeva(value.getBrojZahtjeva());
        vo_zaduzenost.setJmbgMb(value.get(ZAHDataDictionary.PO_ZAH_ZADUZENOST_KOD_BANKE__JMBG_MB));
        dao_zaduzenost.daoRemoveSveZaduzenostiPovezaneOsobe(vo_zaduzenost);
        //sada brisi povezanu osobu
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobePOVIJEST(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        dao.daoRemoveUcitanePovezaneOsobe(value,false);
        PoZahZaduzenostKodBankeJdbc dao_zad = new PoZahZaduzenostKodBankeJdbc();
        ZaduzenostKodBankeVo zad_vo = new ZaduzenostKodBankeVo();
        zad_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        dao_zad.daoRemoveSveZaduzenostiPovezanihOsoba(zad_vo);//pa i vlasnike
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindPostojecePovezaneOsobe(value));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new PovezanaOsobaRs(dao.daoFind(value));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            ZaduzenostKodBankeVo poziv_vo = new ZaduzenostKodBankeVo(vo);
            poziv_vo.setDatumObrade(value.getDatumObrade());
            poziv_vo.setDatumZaprimanja(value.getDatumZaprimanja());
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(poziv_vo);
        }
		return rs;
    }
    public PovezanaOsobaRs procitajZaduzenostPovezanihOsobaPOVIJEST(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        PovezanaOsobaRs rs;
        rs = new PovezanaOsobaRs(dao.daoFind(value));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            ZaduzenostKodBankeVo poziv_vo = new ZaduzenostKodBankeVo(vo);
            poziv_vo.setDatumObrade(value.getDatumObrade());
            poziv_vo.setDatumZaprimanja(value.getDatumZaprimanja());
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(poziv_vo);
        }
		return rs;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobeVlasnikePOVIJEST(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        value.setUcitano("1");
        dao.daoRemoveUcitanePovezaneOsobe(value,true);
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindPostojecePovezaneOsobeVlasnici(value));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setSlog("100");//vlasnici
            vo.setVlasnik("DA");
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new PovezanaOsobaRs(dao.daoFind(value,true));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(new ZaduzenostKodBankeVo(vo));
        }
		return rs;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobePOVIJESTZahtjev(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        dao.daoRemoveUcitanePovezaneOsobe(value,false);
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindPostojecePovezaneOsobeZahtjev(value));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new PovezanaOsobaRs(dao.daoFind(value));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            ZaduzenostKodBankeVo poziv_vo = new ZaduzenostKodBankeVo(vo);
            poziv_vo.setDatumObrade(value.getDatumObrade());
            poziv_vo.setDatumZaprimanja(value.getDatumZaprimanja());
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(poziv_vo);
        }
		return rs;
    }
    public PovezanaOsobaRs procitajSvePovezaneOsobeVlasnikePOVIJESTZahtjev(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        value.setUcitano("1");
        dao.daoRemoveUcitanePovezaneOsobe(value,true);
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindPostojecePovezaneOsobeVlasniciZahtjev(value));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setVlasnik("DA");
            vo.setSlog("100");
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new PovezanaOsobaRs(dao.daoFind(value,true));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(new ZaduzenostKodBankeVo(vo));
        }
		return rs;
    }

    public PovezanaOsobaRs listaPovezanihOsoba(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindListaVezanihOsoba(value));
		return rs;
    }
    public KratkorocniPlanVo dodajKratkorocniPlan(KratkorocniPlanVo value)  {
        PoZahKratkorocniPlanJdbc dao = new PoZahKratkorocniPlanJdbc();
        if(value.exists("@@import")){
            KratkorocniPlanVo plan_vo =  new KratkorocniPlanVo(dao.daoLoad(value));
            if(plan_vo.exists("broj_zahtjeva"))
                return value;
        }
        dao.daoCreate(value);
        return value;
    }
    public KratkorocniPlanVo azurirajKratkorocniPlan(KratkorocniPlanVo value)  {
        PoZahKratkorocniPlanJdbc dao = new PoZahKratkorocniPlanJdbc();
        try{
            dao.daoRemove(value);
        }catch(AS2Exception e){
            if(e.getErrorCode().equals("162") && (e.getSeverity()!=0)){//brojac je nula kad nema plana
               throw new AS2Exception("14001"); 
            }
        }
        dao.daoCreate(value);
        return value;
    }
    public KratkorocniPlanVo citajKratkorocniPlan(ZahtjevPravnaOsobaVo value)  {
        PoZahKratkorocniPlanJdbc dao = new PoZahKratkorocniPlanJdbc();
        KratkorocniPlanVo plan_vo =  new KratkorocniPlanVo(dao.daoLoad(value));
        return plan_vo;
    }
    public KratkorocniPlanVo citajKratkorocniPlanPOVIJEST(ZahtjevPravnaOsobaVo value)  {
        PoZahKratkorocniPlanJdbc dao = new PoZahKratkorocniPlanJdbc();
        KratkorocniPlanVo plan_vo =  dao.daoFindPovijest(value);
    	return plan_vo;
    }
    public KratkorocniPlanVo brisiKratkorocniPlan(KratkorocniPlanVo value)  {
        PoZahKratkorocniPlanJdbc dao = new PoZahKratkorocniPlanJdbc();
        try{
            dao.daoRemove(value);
        }catch(AS2Exception e){
            if(e.getErrorCode().equals("162")){
               throw new AS2Exception("14001"); 
            }
        }
        return value;
    }
    public ZaduzenostVo azurirajZaduzenost(ZaduzenostVo value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
		dao.daoStore(value);
		return value;
    }
    public ZaduzenostRs procitajSveZaduzenosti(ZaduzenostVo value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
        ZaduzenostRs rs = new ZaduzenostRs(dao.daoFind(value));
		return rs;
    }
    public ZaduzenostVo dodajZaduzenost(ZaduzenostVo value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
		dao.daoCreate(value);
		return value;
    }
    public ZaduzenostVo brisiZaduzenost(ZaduzenostVo value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
		dao.daoRemove(value);
		return value;
    }
    public ZaduzenostKodBankeVo azurirajZaduzenostKodBanke(ZaduzenostKodBankeVo value)  {
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
		dao.daoStore(value);
		return value;
    }
    public ZaduzenostKodBankeRs procitajSveZaduzenostiKodBanke(ZaduzenostKodBankeVo value)  {
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
        ZaduzenostKodBankeRs rs = new ZaduzenostKodBankeRs(dao.daoFind(value));
		return rs;
    }
    public ZaduzenostKodBankeVo dodajZaduzenostKodBanke(ZaduzenostKodBankeVo value)  {
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
		dao.daoCreate(value);
		return value;
    }
    public ZaduzenostKodBankeVo brisiZaduzenostKodBanke(ZaduzenostKodBankeVo value)  {
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
		dao.daoRemove(value);
		return value;
    }
    public ZaduzenostKodBankeRs procitajSveZaduzenostiKodBankePOVIJEST(ZaduzenostKodBankeVo value)  {
    	PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
    	PoZahZaduzenostKodBankeJdbc dao_zah = new PoZahZaduzenostKodBankeJdbc();
    	dao_zah.daoRemoveUcitaneZaduzenosti(value);
        ZaduzenostKodBankeRs rs = new ZaduzenostKodBankeRs(dao.daoListaSvihZaduzenosti(value));
        //zr.22.3.2010
        //ZaduzenostKodBankeRs rs = new ZaduzenostKodBankeRs(dao.daoListaSvihKredita(value));
        //ZaduzenostKodBankeRs rs_mjenice = new ZaduzenostKodBankeRs(dao.daoListaSvihMjenica(value));
        //ZaduzenostKodBankeRs rs_faktoring = new ZaduzenostKodBankeRs(dao.daoListaSvihFaktoringa(value));
        //ZaduzenostKodBankeRs rs_akreditivi = new ZaduzenostKodBankeRs(dao.daoListaSvihAkreditivi(value));
        ZaduzenostKodBankeRs rs_ostalo = new ZaduzenostKodBankeRs(dao.daoListaSvihOstalihZaduzenja(value));
        rs.addRows(rs_ostalo.getRows());
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            ZaduzenostKodBankeVo vo = (ZaduzenostKodBankeVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
            if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
                vo.setDatumAnalize(value.getDatumZaprimanja());
            else
                vo.setDatumAnalize(value.getDatumObrade());
         }
        dao_zah.daoCreateMany(rs);         
        rs = new ZaduzenostKodBankeRs(dao_zah.daoFind(value));
		return rs;
    }
    public ZaduzenostKodBankeRs procitajSveZaduzenostiKodBankePovezanaOsoba(ZaduzenostKodBankeVo value)  {
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
        ZaduzenostKodBankeRs rs = new ZaduzenostKodBankeRs(dao.daoFindPovezanaOsoba(value));
		return rs;
    }
    public ZaduzenostKodBankeRs procitajSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(ZaduzenostKodBankeVo value)  {
        return obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(value);
    }
    private ZaduzenostKodBankeRs obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(ZaduzenostKodBankeVo value)  {
        //matični broj povezane osobe ubaci u maticni_broj
        value.setMaticniBroj(value.getJmbgMb());
        //uzmi ipak danasnji datum i citaj zaduzenosti u ovom trenutku (opcija datuma zahtjeva je za sada neupotrebljiva)
        value.set(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA, AS2Date.getCurrentDateString());
        //TODO ako je pravna osoba citaj jedno a ako je fizička čitaj drugo
        //citaj po redu kredite, mjenice, faktoring
        ZaduzenostKodBankeRs rs_sve = new ZaduzenostKodBankeRs();        
        //pravne osobe ********************
        PoZahZaduzenostKodBankeJdbc dao = new PoZahZaduzenostKodBankeJdbc();
        dao.daoRemoveUcitaneZaduzenostiPovezaneOsobe(value);
        PartijaPravnihOsobaJdbc dao_partija = new PartijaPravnihOsobaJdbc();
        ZaduzenostKodBankeRs rs_po = new ZaduzenostKodBankeRs(dao_partija.daoListaSvihZaduzenosti(value));
        //zr.22.3.2010
//        ZaduzenostKodBankeRs rs_po = new ZaduzenostKodBankeRs(dao.daoListaSvihKredita(value));
//        ZaduzenostKodBankeRs rs_mjenice = new ZaduzenostKodBankeRs(dao.daoListaSvihMjenica(value));
//        ZaduzenostKodBankeRs rs_faktoring = new ZaduzenostKodBankeRs(dao.daoListaSvihFaktoringa(value));
//        ZaduzenostKodBankeRs rs_akreditivi = new ZaduzenostKodBankeRs(dao.daoListaSvihAkreditivi(value));
        ZaduzenostKodBankeRs rs_ostalo = new ZaduzenostKodBankeRs(dao_partija.daoListaSvihOstalihZaduzenja(value));
        //dodaj u isti result set
//        rs_po.addRows(rs_mjenice.getRows());
//        rs_po.addRows(rs_faktoring.getRows());
//        rs_po.addRows(rs_akreditivi.getRows());
        rs_po.addRows(rs_ostalo.getRows());
        Iterator<AS2Record> E = rs_po.iteratorRows();
        while(E.hasNext()){
            ZaduzenostKodBankeVo vo = (ZaduzenostKodBankeVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setJmbgMb(value.getJmbgMb());
            vo.setUcitano("1");
            if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
                vo.setDatumAnalize(value.getDatumZaprimanja());
            else
                vo.setDatumAnalize(value.getDatumObrade());
        }
        dao.daoCreateMany(rs_po);
        //fizičke osobe ********************  
        ZaduzenostKodBankeRs rs_gr = new ZaduzenostKodBankeRs(dao.daoListaSvihKreditaGradana(value));
        ZaduzenostKodBankeRs rs_gr_tekuci = new ZaduzenostKodBankeRs(dao.daoListaSvihTekucihGradana(value));
        //dodaj u isti result set
        rs_gr.addRows(rs_gr_tekuci.getRows());
        Iterator<AS2Record> EN = rs_gr.iteratorRows();
        while(EN.hasNext()){
            ZaduzenostKodBankeVo vo = (ZaduzenostKodBankeVo)EN.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setJmbgMb(value.getJmbgMb());
            vo.setUcitano("1");
            if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
                vo.setDatumAnalize(value.getDatumZaprimanja());
            else
                vo.setDatumAnalize(value.getDatumObrade());
        }
        dao.daoCreateMany(rs_gr);
        //************
        rs_sve = new ZaduzenostKodBankeRs(dao.daoFindPovezanaOsoba(value));
		return rs_sve;
    }
    public BankarskiProizvodVo azurirajTrazeniProizvod(BankarskiProizvodVo value)  {
        PoZahBankarskiProizvodJdbc dao = new PoZahBankarskiProizvodJdbc();
		dao.daoStore(value);
		return value;
    }
    public BankarskiProizvodRs procitajSveTrazeneProizvode(BankarskiProizvodVo value)  {
        PoZahBankarskiProizvodJdbc dao = new PoZahBankarskiProizvodJdbc();
        BankarskiProizvodRs rs = new BankarskiProizvodRs(dao.daoFind(value));
		return rs;
    }
    public BankarskiProizvodVo dodajTrazeniProizvod(BankarskiProizvodVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajTrazeniProizvod", value); //$NON-NLS-1$
        PoZahBankarskiProizvodJdbc dao = new PoZahBankarskiProizvodJdbc();
		dao.daoCreate(value);
		return value;
    }
    public BankarskiProizvodVo brisiTrazeniProizvod(BankarskiProizvodVo value)  {
        //prvo bisi kolateral ponuđeni
        PoZahKolateralPonudeniJdbc dao_kolateral = new PoZahKolateralPonudeniJdbc(); 
        KolateralPonudeniVo vo_kolateral = new KolateralPonudeniVo();
        vo_kolateral.setBrojZahtjeva(value.getBrojZahtjeva());
        vo_kolateral.setIdBankarskogProizvoda(value.get(ZAHDataDictionary.PO_ZAH_BANKARSKI_PROIZVOD__ID_BANKARSKOG_PROIZVODA));
        dao_kolateral.daoRemoveSveKolateraleZaProizvod(vo_kolateral);
        //sada brisi povezanu osobu
        PoZahBankarskiProizvodJdbc dao = new PoZahBankarskiProizvodJdbc();
		dao.daoRemove(value);
		return value;
    }
    public KolateralPonudeniVo azurirajPonudeniKolateral(KolateralPonudeniVo value)  {
        //obavezna polja
    	if(value.getVrsta().equals("940")||
    		value.getVrsta().equals("930")||
    		value.getVrsta().equals("920")||
    		value.getVrsta().equals("910"))
    		AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajPonudeniKolateralKratko", value); //$NON-NLS-1$
    	else
            AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajPonudeniKolateral", value); //$NON-NLS-1$
        PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
		dao.daoStore(value);
		return value;
    }
    public KolateralPonudeniRs procitajSvePonudeneKolaterale(KolateralPonudeniVo value)  {
        PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
        KolateralPonudeniRs rs = new KolateralPonudeniRs(dao.daoFind(value));
		return rs;
    }
    public KolateralPonudeniRs procitajSvePonudeneKolateralePOVIJEST(KolateralPonudeniVo value)  {
        KolateralJdbc dao_kolateral = new KolateralJdbc();
        BsaKolateralJdbc dao_bsa_kolateral = new BsaKolateralJdbc();
        PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
        int _ucitano = 1;
        KolateralPonudeniRs rs = new KolateralPonudeniRs();
        if(value.get("@@izvor").equals("KOL")){
            _ucitano = 1;
            dao.daoRemoveUcitaneKolateraleZaProizvod(value,_ucitano);
        	rs = new KolateralPonudeniRs(dao_kolateral.daoListaKolateralaZaOsobu(new KolateralVo(value)));
        }else if(value.get("@@izvor").equals("ZAH")){
            _ucitano = 2;
            dao.daoRemoveUcitaneKolateraleZaProizvod(value,_ucitano);
            KolateralPonudeniVo zadnji_zahtjev = dao.daoFindZadnjiBrojZahtjeva(value);
            rs =dao.daoFindForZahtjev(zadnji_zahtjev);
        }else if(value.get("@@izvor").equals("BSA")){
            _ucitano = 3;
            dao.daoRemoveUcitaneKolateraleZaProizvod(value,_ucitano);
            rs = new KolateralPonudeniRs(dao_bsa_kolateral.daoListaKolateralaZaOsobu(new KolateralVo(value)));
        }else {
            dao.daoRemoveUcitaneKolateraleZaProizvod(value,_ucitano);
            rs = new KolateralPonudeniRs(dao_kolateral.daoListaKolateralaZaOsobu(new KolateralVo(value)));
        }
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            KolateralPonudeniVo vo = (KolateralPonudeniVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setIdBankarskogProizvoda(value.getIdBankarskogProizvoda());
            vo.setIdKolateralaVeza(vo.getIdKolaterala());
            vo.set("ucitano",_ucitano);
        }
        dao.daoCreateMany(rs);
        rs = new KolateralPonudeniRs(dao.daoFind(value));
		return rs;
    }
    public KolateralPonudeniVo dodajPonudeniKolateral(KolateralPonudeniVo value)  {
        //obavezna polja
    	if(value.getVrsta().equals("940")||
    		value.getVrsta().equals("930")||
    		value.getVrsta().equals("920")||
    		value.getVrsta().equals("910"))
    		AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajPonudeniKolateralKratko", value); //$NON-NLS-1$
    	else
            AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajPonudeniKolateral", value); //$NON-NLS-1$
    	PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
		dao.daoCreate(value);
		return value;
    }
    public KolateralPonudeniVo brisiPonudeniKolateral(KolateralPonudeniVo value)  {
        PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
		dao.daoRemove(value);
		return value;
    }
    public PoslovniOdnosBankaVo azurirajKomentarPoslovnogOdnosaBanka(PoslovniOdnosBankaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        dao_po.daoStore(value);
        return value;
    } 
    public VikrVo azurirajVikr(VikrVo value)  {
        PoZahVikrJdbc dao = new PoZahVikrJdbc();
		dao.daoStore(value);
		return value;
    }
    public VikrRs procitajSveVikr(VikrVo value)  {
        PoZahVikrJdbc dao = new PoZahVikrJdbc();
        VikrRs rs = new VikrRs(dao.daoFind(value));
		return rs;
    }
    public VikrVo dodajVikr(VikrVo value)  {
        //obavezna polja
        AS2ValidatorService.getInstance().checkMandatory("ZahtjevPravneOsobeFacadeServer.dodajVikr", value); //$NON-NLS-1$
        PoZahVikrJdbc dao = new PoZahVikrJdbc();
        if(dao.daoFindIfExists(value.getBrojZahtjeva(), value.getSifraValute()))
            throw new AS2Exception("14003");
		dao.daoCreate(value);
		return value;
    }
    public VikrVo brisiVikr(VikrVo value)  {
        PoZahVikrJdbc dao = new PoZahVikrJdbc();
		dao.daoRemove(value);
		return value;
    }
    public ZahtjevPrivitakRs procitajSvePrivitke(ZahtjevPrivitakVo value)  {
        PoZahPrivitakJdbc dao = new PoZahPrivitakJdbc();
        ZahtjevPrivitakRs rs = new ZahtjevPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public ZahtjevPrivitakVo dodajPrivitak(ZahtjevPrivitakVo value)  {
        PoZahPrivitakJdbc dao = new PoZahPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public ZahtjevPrivitakVo citajPrivitak(ZahtjevPrivitakVo value)  {
        PoZahPrivitakJdbc dao = new PoZahPrivitakJdbc();
        return new ZahtjevPrivitakVo(dao.daoLoad(value));
    }
    public ZahtjevPrivitakVo brisiPrivitak(ZahtjevPrivitakVo value)  {
        PoZahPrivitakJdbc dao = new PoZahPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }

    public ZahtjevPravnaOsobaRs procitajSvePodatkeZahtjeva(ZahtjevPravnaOsobaVo value)  {
        ZahtjevPravnaOsobaRs rsZahtjevPravnaOsobaRs = new ZahtjevPravnaOsobaRs(); //povrat više res. setova, u rs se nalaze osnovi podaci (1 red)       
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        if(!value.exists("opcije_ispisa"))//u slučaju kratkog referata
            value.setOpcijeIspisa("referat_bonitet,referat_bonitet_statisticko,referat_poslovni_plan,referat_kupac,referat_dobavljac,kreditna_sposobnost_dugi");
        ZahtjevPravnaOsobaVo pravna_osoba_vo = dao.daoCitajOsnovnePodatkeZahtjeva(value);
        rsZahtjevPravnaOsobaRs.addRow(pravna_osoba_vo);//i u glavni rs i dolje kao rs posebno
        rsZahtjevPravnaOsobaRs.addResultSet("referat", rsZahtjevPravnaOsobaRs);
        
        BankarskiProizvodRs rsBankarskiProizvodRs = new BankarskiProizvodRs(new PoZahBankarskiProizvodJdbc().daoFind(value));
        Iterator<AS2Record> E = rsBankarskiProizvodRs.iteratorRows();
        while (E.hasNext()){
            BankarskiProizvodVo vo_bp = (BankarskiProizvodVo)E.next();
            KolateralPonudeniVo kol_vo = new KolateralPonudeniVo();
            kol_vo.setBrojZahtjeva(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
            kol_vo.setIdBankarskogProizvoda(vo_bp.getIdBankarskogProizvoda());
            KolateralPonudeniRs rsKolateralPonudeniRs = new KolateralPonudeniRs(new PoZahKolateralPonudeniJdbc().daoFind(kol_vo));
            vo_bp.set("referat_ponudjeno_osig", rsKolateralPonudeniRs);
        }
        rsZahtjevPravnaOsobaRs.addResultSet("referat_proizvod", rsBankarskiProizvodRs);
        //podaci o klijentu
        rsZahtjevPravnaOsobaRs.addResultSet("referat_podaci_o_klijentu", rsZahtjevPravnaOsobaRs);
		//povezane osobe
        //zr.25.8.2010.
        //PovezanaOsobaRs rsPovezanaOsobaRs = new PovezanaOsobaRs(new PoZahPovezanaOsobaJdbc().daoFind(value)); 
        value.set("@print","print");
        PovezanaOsobaRs rsPovezanaOsobaRs = new PovezanaOsobaRs(new PoZahPovezanaOsobaJdbc().daoFindPoVrstiSloga(value)); 
        //za sada ne radimo sub sub upit za povezane osobe, vec sve prikazujemo odjednom
//        Enumeration EN = rsPovezanaOsobaRs.getEnumeration();
//        while (EN.hasMoreElements()){
//            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.nextElement();
//            PovezanaOsobaVo vo_pov = new PovezanaOsobaVo();
//            vo_pov.setBrojZahtjeva(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
//            vo_pov.setJmbgMb(value.get(ZAHDataDictionary.PO_ZAH_POVEZANA_OSOBA__JMBG_MB));
//            ZaduzenostKodBankeRs rsZaduzenostKodBankePovezaneOsobeRs = new ZaduzenostKodBankeRs(new PoZahZaduzenostKodBankeJdbc().daoFindPovezanaOsoba(vo_pov));
//            vo.setProperty("referat_zaduzenost_povezane_osobe", rsZaduzenostKodBankePovezaneOsobeRs);
//        }
        rsZahtjevPravnaOsobaRs.addResultSet("referat_povezane_osobe", rsPovezanaOsobaRs);
        rsZahtjevPravnaOsobaRs.addResultSet("referat_povezane_osobe_zaduzenost", rsPovezanaOsobaRs);
        //zaduženost povezanih osoba
        ZaduzenostKodBankeRs rsZaduzenostKodBankePovezaneOsobeRs = new ZaduzenostKodBankeRs(new PoZahZaduzenostKodBankeJdbc().daoFindPovezanaOsobaUkupno(value));
        rsZahtjevPravnaOsobaRs.set("referat_zaduzenost_povezane_osobe", rsZaduzenostKodBankePovezaneOsobeRs);
        //poslovni odnos banka 
        PoslovniOdnosBankaRs rsPoslovniOdnosBankaRs = new PoslovniOdnosBankaRs();       
        PoslovniOdnosBankaVo po_banka = new PoZahPoslovniOdnosBankaJdbc().daoLoad(value);
        rsPoslovniOdnosBankaRs.addRow(po_banka);
        rsZahtjevPravnaOsobaRs.addResultSet("referat_poslovni_odnos_banka", rsPoslovniOdnosBankaRs); 
        //JRR       
        PravnaOsobaVo po_vo = new PravnaOsobaVo();
        po_vo.set("broj_jrr_partija", value.getProperty("broj_jrr_partija"));
        po_vo.setMaticniBroj(value.getMaticniBroj());
        po_vo.setJmbg(value.getMaticniBroj());
        po_vo.set(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
        po_vo.set(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
        //JRR izbor
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_poslovni_racuni")){
        	PravnaOsobaRs rsPravnaOsobaRs = new PravnaOsobaRs(new JrrJdbc().daoPronadiPartijePravneOsobe(po_vo));
        	rsZahtjevPravnaOsobaRs.addResultSet("referat_poslovni_racuni", rsPravnaOsobaRs);  
        }
        //bonitet 6.11.2009. zamjena datum zaprimanja za datum obrade
        po_vo.setDatum(value.getDatumObrade());
        po_vo.set(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA, value.getBrojZahtjeva());
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_bonitet")){
            AS2RecordList rsJ2EEResultSet =  new BonitetJdbc().daoCitajStanjaZaReferat(po_vo);
            if(rsJ2EEResultSet.size()==0){
                //new PoZahBonitetJdbc().daoCreateBonitetGfiPorezna(value);
                rsJ2EEResultSet =  new BonitetJdbc().daoCitajStanjaZaReferatBONITET(po_vo);
            }
            rsZahtjevPravnaOsobaRs.addResultSet("referat_bonitet", rsJ2EEResultSet); 
        }
        //zaduženost kod drugih
        ZaduzenostRs rsZaduzenostRs = new ZaduzenostRs(new PoZahZaduzenostJdbc().daoFind(value));
        rsZahtjevPravnaOsobaRs.addResultSet("referat_zaduzenost_kod_drugih", rsZaduzenostRs);
        //zaduženost kod jbš
        ZaduzenostKodBankeRs rsZaduzenostKodBankeRs = new ZaduzenostKodBankeRs(new PoZahZaduzenostKodBankeJdbc().daoFind(value));
        rsZahtjevPravnaOsobaRs.addResultSet("referat_zaduzenost_kod_jbs", rsZaduzenostKodBankeRs);
        //stav direkcije
        rsZahtjevPravnaOsobaRs.addResultSet("referat_stav_direkcije", rsZahtjevPravnaOsobaRs); 
        //bonitet statisticko 6.11.2009. zamjena datum zaprimanja za datum obrade
        po_vo.setDatum(value.getDatumObrade());
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_bonitet_statisticko")){
            AS2RecordList rsJ2EEResultSetStatisticko =  new BonitetJdbc().daoCitajStatistickiZaReferat(po_vo);
            if(rsJ2EEResultSetStatisticko.size()==0){
                //new PoZahBonitetJdbc().daoCreateBonitetStatisticko(value);
                rsJ2EEResultSetStatisticko =  new BonitetJdbc().daoCitajStatistickiZaReferatBONITET(po_vo);
            }
            rsZahtjevPravnaOsobaRs.addResultSet("referat_bonitet_statisticko", rsJ2EEResultSetStatisticko); 
        }
        //BON 2
        PoZahBon2Jdbc dao_bon2 = new PoZahBon2Jdbc();
        Bon2Rs rs_bon2 = new Bon2Rs(dao_bon2.daoFind(value));
        rsZahtjevPravnaOsobaRs.addResultSet("referat_bon2", rs_bon2); 
        //dobavljači
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_dobavljac")){
            PoZahDobavljacJdbc dao_dobavljac = new PoZahDobavljacJdbc();
            DobavljacRs rs_dobavljaci = new DobavljacRs(dao_dobavljac.daoFind(value));
            rsZahtjevPravnaOsobaRs.addResultSet("referat_dobavljac", rs_dobavljaci);
        }
        //kupci
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_kupac")){
            PoZahKupacJdbc dao_kupac = new PoZahKupacJdbc();
            KupacRs rs_kupci = new KupacRs(dao_kupac.daoFind(value));
            rsZahtjevPravnaOsobaRs.addResultSet("referat_kupac", rs_kupci); 
        }
        //poslovni plan 
        if(AS2String.ifExists(value.getOpcijeIspisa(), "referat_poslovni_plan")){
            PoZahKratkorocniPlanJdbc dao_plan = new PoZahKratkorocniPlanJdbc();
            KratkorocniPlanRs rs_plan =  new KratkorocniPlanRs(dao_plan.daoFind(value));
            rsZahtjevPravnaOsobaRs.addResultSet("referat_poslovni_plan", rs_plan); 
        }
        //pokazatelji
        if(AS2String.ifExists(value.getOpcijeIspisa(), "kreditna_sposobnost_dugi")){
            PoZahOcjenaJdbc dao_ocjena = new PoZahOcjenaJdbc();
            value.set("vrsta", "0");//da vrati sve pokazatelje (1,2,3,4)
            ZahOcjenaRs rs_ocjena = new ZahOcjenaRs(dao_ocjena.daoFind(value));
            rsZahtjevPravnaOsobaRs.addResultSet("kreditna_sposobnost_dugi", rs_ocjena);
        }
        /********** vrati skupni rs nazad *************/
        return rsZahtjevPravnaOsobaRs;
    }
    public ZahtjevPravnaOsobaVo pripremiPrijedlogOdluke(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo ret = new ZahtjevPravnaOsobaVo();
        if(value.get("@@vrsta").equals("odbijanje")){
            ret = dao.daoPripremiPrijedlogOdluke(value);//TODO zvati drugačije ako je odbijanje
        }else {
            ret = dao.daoPripremiPrijedlogOdluke(value);
        }
        return ret;
    }
    public KupacRs procitajSveKupcePOVIJEST(KupacVo value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
        dao.daoRemoveUcitaneKupce(value);
        KupacVo zadnji_zahtjev = dao.daoFindZadnjiBrojZahtjeva(value);
        KupacRs rs = new KupacRs(dao.daoFind(zadnji_zahtjev));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            KupacVo vo = (KupacVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new KupacRs(dao.daoFind(value));
		return rs;
    }
    public DobavljacRs procitajSveDobavljacePOVIJEST(DobavljacVo value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
        dao.daoRemoveUcitaneDobavljace(value);
        DobavljacVo zadnji_zahtjev = dao.daoFindZadnjiBrojZahtjeva(value);
        DobavljacRs rs = new DobavljacRs(dao.daoFind(zadnji_zahtjev));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            DobavljacVo vo = (DobavljacVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new DobavljacRs(dao.daoFind(value));
		return rs;
    }
    public ZahtjevPravnaOsobaVo duplicirajZahtjev(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
		value = dao.daoDupliciranjeZahtjeva(value);
		return value;
    }
    public BankarskiProizvodRs procitajSveOdobreneProizvode(BankarskiProizvodVo value)  {
        PoZahBankarskiProizvodJdbc dao = new PoZahBankarskiProizvodJdbc();
        BankarskiProizvodRs rs = new BankarskiProizvodRs(dao.daoFindProizvodeZaRizik(value));
		return rs;
    }
    public ZahtjevPravnaOsobaVo citajZahtjevZaProcjenuRizika(RizikVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo vo = dao.daoCitajZahtjevZaProcjenuRizika(value);
        ZahtjevPravnaOsobaVo vo_upit = new ZahtjevPravnaOsobaVo(value);
        vo.set("@@dosadasnji_zahtjevi", dao.daoPronadiZahtjeveZaKlijenta(vo_upit));        
        return vo;
    }


    public ZahOcjenaVo azurirajOcjenu(ZahOcjenaVo value)  {
        PoZahOcjenaJdbc dao = new PoZahOcjenaJdbc();
        if(value.get("@@NOVO").equals("vrijednost_pokazatelja"))
            value.setOcjena(dao.daoFnPripremJednuOcjenu(value));//izračunaj novu ocjenu
		dao.daoStore(value);
		return value;
    }
    public ZahOcjenaRs procitajSveOcjene(ZahOcjenaVo value)  {
        PoZahOcjenaJdbc dao = new PoZahOcjenaJdbc();
        ZahOcjenaRs rs = new ZahOcjenaRs(dao.daoFind(value));
		return rs;
    }
    public ZahOcjenaVo brisiOcjenu(ZahOcjenaVo value)  {
        PoZahOcjenaJdbc dao = new PoZahOcjenaJdbc();
		dao.daoRemove(value);
		if(value.getBrojZahtjeva().trim().length()>0)
		    dao.daoSpAzurirajPondere(value);
		else
		    dao.daoSpAzurirajPonderePravnaOsoba(value);
		return value;
    }
    public ZahOcjenaRs pripremaOcjena(ZahOcjenaVo value)  {
        PoZahOcjenaJdbc dao = new PoZahOcjenaJdbc();
        dao.daoSpPripremiOcjene(value);
        ZahOcjenaRs rs = new ZahOcjenaRs(dao.daoFind(value));
        return rs;
    }
 
    public PovezanaOsobaVo dodajPovezaneOsobe(PovezanaOsobaRs value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
		dao.daoCreateMany(value);
		return new PovezanaOsobaVo();
    }
    public PovezanaOsobaRs dodajPovezaneOsobeIzPO(PovezanaOsobaVo value)  {
        PoZahPovezanaOsobaJdbc dao = new PoZahPovezanaOsobaJdbc();
        dao.daoRemoveUcitanePovezaneOsobe(value,false);
        PovezanaOsobaRs rs = new PovezanaOsobaRs(dao.daoFindPostojecePovezaneOsobePO(value));
        Iterator<AS2Record> E = rs.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            vo.setBrojZahtjeva(value.getBrojZahtjeva());
            vo.setUcitano("1");
        }
        dao.daoCreateMany(rs);
        rs = new PovezanaOsobaRs(dao.daoFind(value));
        //učitaj i njihova zaduženja
        Iterator<AS2Record> EN = rs.iteratorRows();
        while(EN.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)EN.next();
            ZaduzenostKodBankeVo poziv_vo = new ZaduzenostKodBankeVo(vo);
            poziv_vo.setDatumObrade(value.getDatumObrade());
            poziv_vo.setDatumZaprimanja(value.getDatumZaprimanja());
            //poziv_vo.setDatumProcjeneRizika(value.getDatumProcjeneRizika());
            obradiSveZaduzenostiKodBankePovezanaOsobaPOVIJEST(poziv_vo);
        }
		return rs;       
    }
    public PovezanaOsobaVo brisiVisePovezanihOsoba(PovezanaOsobaRs value)  {
    	Iterator<AS2Record> E = value.iteratorRows();
        while(E.hasNext()){
            PovezanaOsobaVo vo = (PovezanaOsobaVo)E.next();
            brisiPovezanuOsobu(vo);
        }
        return new PovezanaOsobaVo();
    }
    public KupacVo brisiViseKupaca(KupacRs value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
		dao.daoRemoveMany(value);
		return new KupacVo();
    }
    public DobavljacVo brisiViseDobavljaca(DobavljacRs value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
		dao.daoRemoveMany(value);
		return new DobavljacVo();
    }
    public Bon2Vo brisiViseBon2(Bon2Rs value)  {
        PoZahBon2Jdbc dao = new PoZahBon2Jdbc();
		dao.daoRemoveMany(value);
		return new Bon2Vo();
    }
    public ZaduzenostVo brisiViseZaduzenosti(ZaduzenostRs value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
		dao.daoRemoveMany(value);
		return new ZaduzenostVo();
    }
    public KolateralPonudeniVo brisiVisePonudenihKolaterala(KolateralPonudeniRs value)  {
        PoZahKolateralPonudeniJdbc dao = new PoZahKolateralPonudeniJdbc();
		dao.daoRemoveMany(value);
		return new KolateralPonudeniVo();
    }
    public ZahOcjenaVo brisiViseOcjena(ZahOcjenaRs value)  {
        PoZahOcjenaJdbc dao = new PoZahOcjenaJdbc();
		dao.daoRemoveMany(value);
		return new ZahOcjenaVo();
    }
     public ZahtjevPravnaOsobaVo dodajBonitet(ZahtjevPravnaOsobaVo value)  {
        PoZahBonitetJdbc dao = new PoZahBonitetJdbc();
        dao.daoCreateBonitetGfiPorezna(value);
        dao.daoCreateBonitetStatisticko(value);
        return value;
    }
   public ZahtjevPravnaOsobaVo citajPrijedlogOdobrenja(ZahtjevPravnaOsobaVo value)  {
       PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
       return dao.daoCitajPrijedlogOdobrenja(value);
   }
   //UREDNOST
   public UrednostVo dodajUrednostZahtjeva(ZahtjevPravnaOsobaVo value)  {
       PoZahUrednostJdbc dao = new PoZahUrednostJdbc();
       dao.daoUrednostZahtjeva(value);
       return dao.daoCitajUrednost(value);
   }
    public UrednostVo citajUrednostZahtjeva(ZahtjevPravnaOsobaVo value)  {
        PoZahUrednostJdbc dao = new PoZahUrednostJdbc();
        return dao.daoCitajUrednost(value);
    }
    public UrednostVo azurirajUrednostZahtjeva(UrednostVo value)  {
        PoZahUrednostJdbc dao = new PoZahUrednostJdbc();
        dao.daoStore(value);
        return value;
    }
    //OSIGURANJE
    public OsiguranjeVo dodajOsiguranjeZahtjeva(OsiguranjeVo value)  {
        PoZahOsiguranjeJdbc dao = new PoZahOsiguranjeJdbc();
        dao.daoCreate(value);
        return value;
    }
    public OsiguranjeVo azurirajOsiguranjeZahtjeva(OsiguranjeVo value)  {
        PoZahOsiguranjeJdbc dao = new PoZahOsiguranjeJdbc();
        dao.daoStore(value);
        return value;
    }
    public OsiguranjeVo citajOsiguranjeZahtjeva(ZahtjevPravnaOsobaVo value)  {
        PoZahOsiguranjeJdbc dao = new PoZahOsiguranjeJdbc();
        return dao.daoCitajOsiguranje(value);
    }
    //21.4.2011. Excel učitavanje
    public KupacVo dodajViseKupaca(KupacRs value)  {
        PoZahKupacJdbc dao = new PoZahKupacJdbc();
        dao.daoCreateMany(value);
        return new KupacVo();
    }
    public DobavljacVo dodajViseDobavljaca(DobavljacRs value)  {
        PoZahDobavljacJdbc dao = new PoZahDobavljacJdbc();
        dao.daoCreateMany(value);
        return new DobavljacVo();
    }
    public ZaduzenostVo dodajViseZaduzenosti(ZaduzenostRs value)  {
        PoZahZaduzenostJdbc dao = new PoZahZaduzenostJdbc();
        dao.daoCreateMany(value);
        return new ZaduzenostVo();
    }
    public KolateralPonudeniRs procitajSvePostojeceKolaterale(ZaduzenostKodBankeVo value)  {
        BsaKolateralJdbc dao_bsa_kolateral = new BsaKolateralJdbc();
        KolateralPonudeniRs rs = new KolateralPonudeniRs();
        rs = new KolateralPonudeniRs(dao_bsa_kolateral.daoListaKolateralaZaPartiju(value));
        return rs;
    }
    public RizikKomentarVo dodajKomentar(RizikKomentarVo value)  {
        RizikKomentarJdbc dao = new RizikKomentarJdbc();
        dao.daoCreate(value);
        return value;
    }
    public RizikKomentarVo azurirajKomentar(RizikKomentarVo value)  {
        RizikKomentarJdbc dao = new RizikKomentarJdbc();
        dao.daoStore(value);
        return value;
    }
    public RizikKomentarVo brisiKomentar(RizikKomentarVo value)  {
        RizikKomentarJdbc dao = new RizikKomentarJdbc();
        dao.daoRemove(value);
        return value;
    }
    public RizikKomentarRs procitajSveKomentare(RizikKomentarVo value)  {
        RizikKomentarJdbc dao = new RizikKomentarJdbc();
        RizikKomentarRs rs = new RizikKomentarRs(dao.daoFind(value));
        return rs;
    }
    /***** PROCJENA RIZIKA BEGIN ******/
    public RizikRs procitajSveProcjeneRizika(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        return dao.daoFindProcjenaRizikaLista(value,false);
    }
    public RizikRs pronadiProcjeneRizika(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        return dao.daoFindProcjenaRizikaLista(value,true);
    }
    public RizikVo brisiProcjenuRizika(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        RizikVo ri_vo = new RizikVo();
        ri_vo.setIdRizika(value.getIdRizika());
        ri_vo.setIspravnoNE();
        dao.daoStore(ri_vo);
        return ri_vo;
    }
    public RizikRs pripremaMisljenja(RizikVo value)  {
        RizikRs rs = new RizikRs();
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        RizikVo procjena_vo = dao.daoFindProcjenaRizika(value);
        rs.addRow(procjena_vo);
        rs.addResultSet("misljenje", rs);
        BankarskiProizvodRs rsBankarskiProizvodRs = new BankarskiProizvodRs(new PoZahBankarskiProizvodJdbc().daoFind(value));
        rs.addResultSet("referat_proizvod", rsBankarskiProizvodRs);
        return  rs;
    }
    public RizikRs citajMisljenje(RizikVo value)  {
        RizikRs rs = new RizikRs();
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        String id_rizika = dao.daoFindIdRizika(value.getBrojZahtjeva());
        value.setIdRizika(id_rizika);
        RizikVo procjena_vo = dao.daoFindProcjenaRizika(value);
        rs.addRow(procjena_vo);
        rs.addResultSet("misljenje", rs);
        BankarskiProizvodRs rsBankarskiProizvodRs = new BankarskiProizvodRs(new PoZahBankarskiProizvodJdbc().daoFind(value));
        rs.addResultSet("referat_proizvod", rsBankarskiProizvodRs);
        return  rs;
    }
    public RizikRs pripremaMisljenjaBACKUP(RizikVo value)  {
        RizikRs rs = new RizikRs();
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        RizikVo procjena_vo = dao.daoFindProcjenaRizika(value);
        rs.addRow(procjena_vo);
        if(value.exists("broj_zahtjeva") && !value.getBrojZahtjeva().equals("0")){
            PoZahOcjenaJdbc dao_ocjena = new PoZahOcjenaJdbc();
            ZahOcjenaRs ocjene_rs = new ZahOcjenaRs(dao_ocjena.daoFind(value));
            rs.addResultSet("misljenje", rs);
            rs.addResultSet("pokazatelji", ocjene_rs);
            BankarskiProizvodRs rsBankarskiProizvodRs = new BankarskiProizvodRs(new PoZahBankarskiProizvodJdbc().daoFind(value));
            rs.addResultSet("referat_proizvod", rsBankarskiProizvodRs);
        }else{
            rs.addResultSet("misljenje", rs);//samo rizik
        }
        return  rs;
    }
    public ZahtjevPravnaOsobaVo promjenaDatumaProcjeneRizika(ZahtjevPravnaOsobaVo value)  {
        return value;
    }
    public ZahtjevPravnaOsobaVo promjenaDatumaProcjeneRizikaBACKUP(ZahtjevPravnaOsobaVo value)  {
        if(value.exists("@@datum_rizika")){
            PoZahRizikJdbc dao_rizik = new PoZahRizikJdbc();
            RizikVo _vo = new RizikVo();
            _vo.setIdRizika(value.get("id_rizika"));
            _vo.setDatumRizika(value.getDatumProcjeneRizika());
            dao_rizik.daoStore(_vo);
            return value;
        }
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        ZahtjevPravnaOsobaVo za_vo = new ZahtjevPravnaOsobaVo();
        za_vo.setBrojZahtjeva(value.getBrojZahtjeva());
        za_vo.setDatumProcjeneRizika(value.getDatumProcjeneRizika());
        za_vo.setDatumZaprimanjaProcjeneRizika(value.getDatumZaprimanjaProcjeneRizika());
        dao.daoStore(za_vo);
        return value;
    }
    public RizikVo dodajProcjenuRizika(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        if(value.exists("broj_zahtjeva") && value.getBrojZahtjeva().length()>0){
            PoZahZahtjevJdbc dao_zahtjev = new PoZahZahtjevJdbc();
            if(!dao_zahtjev.daoFindIfExistsZaKlijenta(value.getBrojZahtjeva(), value.getJmbgMb(), value.getOib()))
                throw new AS2Exception("14501");
        }
        dao.daoCreate(value);
        String _zadnji_id_rizika = dao.daoZadnjiBrojRizika(value);
        RizikVo ret = new RizikVo();
        ret.setIdRizika(_zadnji_id_rizika);
        return ret;
    }
    public RizikVo dodajProcjenuRizikaBACKUP(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        if(value.exists("broj_zahtjeva") && value.getBrojZahtjeva().length()>0){
            PoZahZahtjevJdbc dao_zah = new PoZahZahtjevJdbc();
            if(!dao_zah.daoFindIfExists(value.getBrojZahtjeva()))
                throw new AS2Exception("12801");
        }
        dao.daoCreate(value);
        PoZahZahtjevJdbc dao_zahtjev = new PoZahZahtjevJdbc();
        if(!dao_zahtjev.daoFindIfExists(value.getBrojZahtjeva()))
                return value;
        value.setIdRizika(dao.daoZadnjiBrojRizika(value));      
        //azuriraj podatke o klijentu
        PoZahPodaciKlijentaJdbc dao_klijent = new PoZahPodaciKlijentaJdbc();
        PodaciKlijentaVo podaci_klijenta = new PodaciKlijentaVo();
        podaci_klijenta.setBrojZahtjeva(value.getBrojZahtjeva());
        podaci_klijenta.set("broj_zaposlenih",value.getProperty("broj_zaposlenih"));
        podaci_klijenta.set("godina_pocetka_poslovanja",value.getProperty("godina_pocetka_poslovanja"));
        podaci_klijenta.set("velicina_poduzeca",value.getProperty("velicina_poduzeca"));
        if(podaci_klijenta.getProperties().size()>1)//unesen ne samo broj zahtjeva
            dao_klijent.daoStore(podaci_klijenta);        
        //azuriraj poslovni odnos banka
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        PoslovniOdnosBankaVo posl_odnos =  new PoslovniOdnosBankaVo();
        posl_odnos.setBrojZahtjeva(value.getBrojZahtjeva());
        posl_odnos.set("poseban_odnos",value.getProperty("poseban_odnos"));
        posl_odnos.set("velika_izlozenost",value.getProperty("velika_izlozenost"));
        if(posl_odnos.getProperties().size()>1)//unesen ne samo broj zahtjeva
            dao_po.daoStore(posl_odnos);        
        return value;
    }
    public RizikVo azurirajProcjenuRizika(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        if(value.exists("broj_zahtjeva") && value.getBrojZahtjeva().length()>0 && !value.getBrojZahtjeva().equals("0")){
            PoZahZahtjevJdbc dao_zahtjev = new PoZahZahtjevJdbc();
            if(!dao_zahtjev.daoFindIfExistsZaKlijenta(value.getBrojZahtjeva(), value.getJmbgMb(), value.getOib()))
                throw new AS2Exception("14501");
        }
        dao.daoStore(value);        
        return value;
    }
    public RizikVo azurirajProcjenuRizikaBACKUP(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        dao.daoStore(value);        
        //azuriraj podatke o klijentu
        PoZahPodaciKlijentaJdbc dao_klijent = new PoZahPodaciKlijentaJdbc();
        PodaciKlijentaVo podaci_klijenta = new PodaciKlijentaVo();
        podaci_klijenta.setBrojZahtjeva(value.getBrojZahtjeva());
        podaci_klijenta.set("broj_zaposlenih",value.getProperty("broj_zaposlenih"));
        podaci_klijenta.set("godina_pocetka_poslovanja",value.getProperty("godina_pocetka_poslovanja"));
        podaci_klijenta.set("velicina_poduzeca",value.getProperty("velicina_poduzeca"));
        if(podaci_klijenta.getProperties().size()>1)//unesen ne samo broj zahtjeva
            dao_klijent.daoStore(podaci_klijenta);
        
        //azuriraj poslovni odnos banka
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        PoslovniOdnosBankaVo posl_odnos =  new PoslovniOdnosBankaVo();
        posl_odnos.setBrojZahtjeva(value.getBrojZahtjeva());
        posl_odnos.set("poseban_odnos",value.getProperty("poseban_odnos"));
        posl_odnos.set("velika_izlozenost",value.getProperty("velika_izlozenost"));
        if(posl_odnos.getProperties().size()>1){//uneseno, ne samo broj zahtjeva
            if(!posl_odnos.getBrojZahtjeva().equalsIgnoreCase("0"))
            dao_po.daoStore(posl_odnos);
        }
        return value;
    }
    public RizikVo azurirajOpceKomentare(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        dao.daoStore(value);
        return value;
    }
    public RizikVo citajRizik(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        //zr.22.8.2011. Samo kroz appl. rizika
//        if(value.exists("broj_zahtjeva") && !value.getBrojZahtjeva().equals("0") && !value.getBrojZahtjeva().equals(""))
//            return dao.daoLoad(value);
//        else 
            return dao.daoLoadRizik(value);
    }
    public RizikVo dodajOpceKomentare(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        dao.daoCreate(value);
        value.setIdRizika(dao.daoZadnjiBrojRizika(value));
        return value;
    }
    public RizikVo osvjeziFinancijskePodatkeZaProcjenu(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        RizikVo vo = dao.daoFindFinancijskePodatke(value);
        return vo;
    }
    public RizikVo osvjeziPoslovniOdnosZaProcjenu(RizikVo value)  {
        PoZahRizikJdbc dao = new PoZahRizikJdbc();
        RizikVo vo = dao.daoFindPoslovniOdnosZaProcjenu(value);
        return vo;
    }
	public RizikVo duplicirajMisljenje(RizikVo value)  {
		PoZahRizikJdbc dao = new PoZahRizikJdbc();
		value = dao.daoDupliciranjeMisljenja(value);
		return value;
	}
    /***** PROCJENA RIZIKA END ******/
    public PoslovniOdnosBankaRs citajDionicePovezanihOsoba(ZahtjevPravnaOsobaVo value)  {
        PoZahPoslovniOdnosBankaJdbc dao_po = new PoZahPoslovniOdnosBankaJdbc();
        return dao_po.daoCitajDionicePovezanihOsoba(value);
    }
 }
