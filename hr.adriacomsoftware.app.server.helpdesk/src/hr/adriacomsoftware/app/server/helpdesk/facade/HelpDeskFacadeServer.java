package hr.adriacomsoftware.app.server.helpdesk.facade;

import hr.adriacomsoftware.app.common.cmdb.dto.EmailVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskKontaktRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskKontaktVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivAktivnostRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivKomentarRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivKomentarVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivPrivitakRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivPrivitakVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivStatusVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogAktivnostVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogPrivitakRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogPrivitakVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogRs;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogStatusVo;
import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskRadniNalogVo;
import hr.adriacomsoftware.app.common.helpdesk.facade.HelpDeskFacade;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbDjelatnikJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdDopusteniZahtjevJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdKontaktJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdPozivAktivnostJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdPozivJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdPozivKomentarJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdPozivPrivitakJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdPozivStatusJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdRadniNalogJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdRadniNalogPrivitakJdbc;
import hr.adriacomsoftware.app.server.helpdesk.da.jdbc.HdRadniNalogStatusJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.email.AS2EmailConstants;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class HelpDeskFacadeServer extends AS2FacadeServerLayer
		implements HelpDeskFacade {

	private static HelpDeskFacadeServer _instance = null; 
	public static HelpDeskFacadeServer getInstance() {
		if (_instance == null){
			_instance = new HelpDeskFacadeServer();
		}
		return _instance;
	}
	private HelpDeskFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public HelpDeskPozivRs procitajSvePozive(HelpDeskPozivVo value)  {
        HdPozivJdbc dao = new HdPozivJdbc();
        HelpDeskPozivRs rs = new HelpDeskPozivRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskPozivRs pronadiPozive(HelpDeskPozivVo value)  {
        HdPozivJdbc dao = new HdPozivJdbc();
        HelpDeskPozivRs rs = new HelpDeskPozivRs();
        //donjih 6 linija koda dodano da bi se ignorirao "blank praznih polja" pri pretraživanju
        value.set("id_poziva",value.get("id_poziva").trim());
        value.set("naziv",value.get("naziv").trim());
        value.set("ime_prezime",value.get("ime_prezime").trim());
        value.set("osoba_rasporeda_",value.get("osoba_rasporeda_").trim());
        value.set("opis_sto",value.get("opis_sto").trim());
        value.set("organizacijska_jedinica",value.get("organizacijska_jedinica").trim()); 
        value.set("vas_znak",value.get("vas_znak").trim());
        if (value.get("@@filter").length()>0)
            rs = new HelpDeskPozivRs(dao.daoProcitajVlastitePozive(value));
        else{
        	if (value.get("@@formatedExcel").matches("1")){
        	    value.setRemoteMethod("abc");
        	    value.setRemoteObject("abc");
        	    rs = new HelpDeskPozivRs(dao.daoSearch1(value));
        	}else
        	    rs = new HelpDeskPozivRs(dao.daoSearch(value));
        }
		return rs;
    }
    public HelpDeskPozivVo azurirajPoziv(HelpDeskPozivVo value)  {
        HdPozivJdbc dao = new HdPozivJdbc();
        CmdbDjelatnikJdbc dao_djelatnik = new CmdbDjelatnikJdbc();
        value.setOrganizacijskaJedinica(dao_djelatnik.daoGetOrgJedinicaRada(value.getOsobaPrijave()));
		dao.daoStore(value);
		if (!value.exists("datum_rjesenja")){
			value.set("datum_naziv","datum_rjesenja");
			dao.daoUpdateDateAsDefault(value);
		}
		return value;
    }
    public HelpDeskPozivVo dodajPoziv(HelpDeskPozivVo value)  {
        AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        if(value.getOsobaPrijave().length()<=0){            
            value.setOsobaPrijave(user.get("id_osobe")); 
        }
        CmdbDjelatnikJdbc dao_djelatnik = new CmdbDjelatnikJdbc();
        value.setOrganizacijskaJedinica(dao_djelatnik.daoGetOrgJedinicaRada(value.getOsobaPrijave()));
        HdPozivJdbc dao = new HdPozivJdbc();
		dao.daoCreate(value);
		if (!value.exists("datum_rjesenja")){
			value.set("datum_naziv","datum_rjesenja");
			dao.daoUpdateDateAsDefault(value);
		}
		if(value.getVrsta().equals("100")){
			HelpDeskPozivVo email = new HelpDeskPozivVo();
			email.set(AS2EmailConstants.IZBOR,"1");
			email.set(AS2EmailConstants.RECIPIENTS, "rmaricic@jaba.hr; zrosko@jaba.hr; msekso@jaba.hr");
			email.set(AS2EmailConstants.SUBJECT, "Štetni događaj: "+value.getNaziv()); 
			StringBuffer sb = new StringBuffer();
			sb.append("Poštovani,\n");
			sb.append("Prijavljen je štetni događaj pod nazivom "+value.getNaziv()+".\n");
			sb.append("Potrebno je događaj urediti i unijeti ga u evidenciju štetnih događaja operativnog rizika.\n");
			sb.append("Prijavite se u aplikaciju Help Desk (pritiskom na link ispod).\n");
			sb.append("http://portal:8080/adriacom/client/hd.jnlp\n");
			sb.append("Srdačan pozdrav,\n Help desk."); 
			email.set(AS2EmailConstants.BODY, sb.toString());
			email.set(AS2EmailConstants.PROFILE_NAME,"Help Desk"); 
			dao.daoSendEmail(email);
		}
		return value;
    }
    public HelpDeskPozivVo brisiPoziv(HelpDeskPozivVo value)  {
        HdPozivJdbc dao = new HdPozivJdbc();
        HelpDeskPozivVo vo = new HelpDeskPozivVo();
        vo.setIdPoziva(value.getIdPoziva());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public HelpDeskPozivVo procitajZadnjiZahtjev(HelpDeskPozivVo value)  {
    	HdPozivJdbc dao = new HdPozivJdbc();
    	//da bi se u Excel povukao i broj našeg zahtjeva u svrhu BSA Batch i aplikacija zahtjeva
        HelpDeskPozivVo vo = new HelpDeskPozivVo(dao.daoFindLast(value));
		return vo;
    }
    public HelpDeskPozivRs procitajDopusteneZahtjeve(HelpDeskPozivVo value)  {
        HdDopusteniZahtjevJdbc dao = new HdDopusteniZahtjevJdbc();
        HelpDeskPozivRs rs = new HelpDeskPozivRs(dao.daoDopusteniZahtjevi(value));
		return rs;
    }
    public HelpDeskPozivVo dodajDopusteniZahtjev(HelpDeskPozivVo value)  {
        HdDopusteniZahtjevJdbc dao = new HdDopusteniZahtjevJdbc();
        HelpDeskPozivVo novi_vo = new HelpDeskPozivVo();
        novi_vo.set("aplikacija","HD");
        novi_vo.set("id_zahtjeva",value.get("broj_"));
        novi_vo.set("dopustena_osoba",value.get("dopustena_osoba"));
        boolean provjera = dao.daoFindIfExistsDopusteniZahtjev(novi_vo);
		if (novi_vo.get("dopustena_osoba").length()>10 && provjera == false)
		    dao.daoCreate(novi_vo);
		return value;
    }
    public HelpDeskPozivVo brisiDopusteniZahtjev(HelpDeskPozivVo value)  {
        HdDopusteniZahtjevJdbc dao = new HdDopusteniZahtjevJdbc();
		dao.daoRemove(value);
		return value;
    }
    public HelpDeskRadniNalogRs procitajSveRadneNaloge(HelpDeskRadniNalogVo value)  {
        HdRadniNalogJdbc dao = new HdRadniNalogJdbc();
        HelpDeskRadniNalogRs rs = new HelpDeskRadniNalogRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskRadniNalogRs pronadiRadneNaloge(HelpDeskRadniNalogVo value)  {
        HdRadniNalogJdbc dao = new HdRadniNalogJdbc();
        HelpDeskRadniNalogRs rs = new HelpDeskRadniNalogRs(dao.daoFind(value));
		return rs; 
    }
    public HelpDeskRadniNalogVo azurirajRadniNalog(HelpDeskRadniNalogVo value)  {
        HdRadniNalogJdbc dao = new HdRadniNalogJdbc();
		dao.daoStore(value);
		return value;
    }
    public HelpDeskRadniNalogVo dodajRadniNalog(HelpDeskRadniNalogVo value)  {
        HdRadniNalogJdbc dao = new HdRadniNalogJdbc();
		dao.daoCreate(value);
		return value;
    }
    public HelpDeskRadniNalogVo brisiRadniNalog(HelpDeskRadniNalogVo value)  {
        HdRadniNalogJdbc dao = new HdRadniNalogJdbc();
        HelpDeskRadniNalogVo vo = new HelpDeskRadniNalogVo();
        vo.setIdRadnogNaloga(value.getIdRadnogNaloga());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public HelpDeskPozivKomentarRs procitajSveKomentare(HelpDeskPozivKomentarVo value)  {
        HdPozivKomentarJdbc dao = new HdPozivKomentarJdbc();
        HelpDeskPozivKomentarRs rs = new HelpDeskPozivKomentarRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskPozivKomentarVo azurirajKomentar(HelpDeskPozivKomentarVo value)  {
        HdPozivKomentarJdbc dao = new HdPozivKomentarJdbc();
		dao.daoStore(value);
		return value;
    }
    public HelpDeskPozivKomentarVo dodajKomentar(HelpDeskPozivKomentarVo value)  {
        HdPozivKomentarJdbc dao = new HdPozivKomentarJdbc();
		dao.daoCreate(value);
		return value;
    }
    public HelpDeskPozivKomentarVo brisiKomentar(HelpDeskPozivKomentarVo value)  {
        HdPozivKomentarJdbc dao = new HdPozivKomentarJdbc();
		dao.daoRemove(value);
		return value;
    }
    public HelpDeskPozivAktivnostRs procitajSveAktivnosti(HelpDeskRadniNalogAktivnostVo value)  {
        HdPozivAktivnostJdbc dao = new HdPozivAktivnostJdbc();
        HelpDeskPozivAktivnostRs rs = new HelpDeskPozivAktivnostRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskRadniNalogAktivnostVo azurirajAktivnost(HelpDeskRadniNalogAktivnostVo value)  {
        HdPozivAktivnostJdbc dao = new HdPozivAktivnostJdbc();
		dao.daoStore(value);
		return value;
    }
    public HelpDeskRadniNalogAktivnostVo dodajAktivnost(HelpDeskRadniNalogAktivnostVo value)  {
        HdPozivAktivnostJdbc dao = new HdPozivAktivnostJdbc();
		dao.daoCreate(value);
		return value;
    }
    public HelpDeskRadniNalogAktivnostVo brisiAktivnost(HelpDeskRadniNalogAktivnostVo value)  {
        HdPozivAktivnostJdbc dao = new HdPozivAktivnostJdbc();
		dao.daoRemove(value);
		return value;
    }
    public EmailVo proslijediRadniNalogEmailom(EmailVo value)  {
        HdRadniNalogStatusJdbc dao = new HdRadniNalogStatusJdbc();
        HelpDeskRadniNalogStatusVo vo = new HelpDeskRadniNalogStatusVo();
        vo.setEmailFrom(value.getFrom());
        vo.setEmailTo(value.getTo());
        vo.setIdRadnogNaloga(value.getIdObjekta());
        vo.setDatum(AS2Date.getTodayAsCalendar());
        vo.setOperater(value.get("zadnja_promjena_user_id")); 
        vo.setPoruka(value.getBody());
        vo.set("@@tijelo_poruke",value.get("@@tijelo_poruke"));  
        dao.daoCreate(vo);
        //šalji email poruku
        StringBuffer sb = new StringBuffer();
        sb.append("Poštovani,\n");
        sb.append("Vama je proslijeđen radni nalog br: "+value.getIdObjekta()+" na obradu.\n");
        sb.append("Prijavite se u aplikaciju Help Desk (pritiskom na link ispod).\n");
        sb.append("http://portal:8080/adriacom/client/hd.jnlp\n");
        sb.append("Srdačan pozdrav,\n"); 
        sb.append(value.getFrom());
        sb.append("\nP.S.\n"); 
        sb.append(value.getBody()); 
        sb.append("\nInterni portal Jadranske banke d.d. Šibenik");
        //ažuriraj poziv
        HdRadniNalogJdbc dao_poziv = new HdRadniNalogJdbc();
        HelpDeskRadniNalogVo email = new HelpDeskRadniNalogVo();
        email.setIdRadnogNaloga(value.getIdObjekta());
        email.setOsobaRada(value.getIdOsobe());
        dao_poziv.daoStore(email);
        //send email procedura
        email.set(AS2EmailConstants.RECIPIENTS, value.getTo());
        if(vo.get("@@tijelo_poruke").length()>0)
            email.set(AS2EmailConstants.BODY, vo.get("@@tijelo_poruke"));  
        else 
            email.set(AS2EmailConstants.BODY, sb.toString());
        email.set(AS2EmailConstants.SUBJECT, "Radni nalog br: "+value.getIdObjekta());
        email.set(AS2EmailConstants.PROFILE_NAME,"Help Desk");
        email.set(AS2EmailConstants.IZBOR,value.getIzbor());
        email.set(AS2EmailConstants.GRUPA,value.getGrupa());
        email.set(AS2EmailConstants.ORG_JEDINICA_RADA,value.getOrgJedinicaRada());
        dao_poziv.daoSendEmail(email);
        return value;
    }
    public EmailVo proslijediHelpDeskPozivEmailom(EmailVo value)  {
           HdPozivStatusJdbc dao = new HdPozivStatusJdbc();
           HelpDeskPozivStatusVo vo = new HelpDeskPozivStatusVo();
           vo.setEmailFrom(value.getFrom());
           vo.setEmailTo(value.getTo());
           if(vo.getEmailTo().equals(""))  vo.setEmailTo(dao.daoJmbgEmail(value.get("obavjestena_osoba")));
           vo.setIdPoziva(value.getIdObjekta());
           vo.setDatum(AS2Date.getTodayAsCalendar());
           vo.setOperater(value.get("zadnja_promjena_user_id"));
           vo.setPoruka(value.getBody());
           vo.set("@@tijelo_poruke",value.get("@@tijelo_poruke"));
           vo.set("@tip_prosljedivanja",value.get("@tip_prosljedivanja"));
           dao.daoCreate(vo);
           //šalji email poruku
           String body = "";
           StringBuffer sb = new StringBuffer();
           if(value.get("@tip_prosljedivanja").equals("samo_proslijedi")){
               sb.append("Poštovani,\n");
               sb.append("Proslijeđen Vam je zahtjev br: "+value.getIdObjekta()+".\n");
               sb.append("Prijavite se u aplikaciju Help Desk (pritiskom na link ispod).\n");
               sb.append("http://portal:8080/adriacom/client/hd.jnlp\n");
               sb.append("Srdačan pozdrav,\n"); 
               sb.append(value.getFrom());
           }else{
	           sb.append("Poštovani,\n");
	           sb.append("Vama je proslijeđen zahtjev br: "+value.getIdObjekta()+" na obradu.\n");
	           sb.append("Prijavite se u aplikaciju Help Desk (pritiskom na link ispod).\n");
	           sb.append("http://portal:8080/adriacom/client/hd.jnlp\n");
	           sb.append("Srdačan pozdrav,\n"); 
	           sb.append(value.getFrom());
           }
           if(value.getBody().length()>0){
	           sb.append("\nP.S.\n"); 
	           sb.append(value.getBody()); 
	           body = "\n\nP.S.\n"+value.getBody()+"\n\nInterni portal Jadranske banke d.d. Šibenik";
           }
           sb.append("\nInterni portal Jadranske banke d.d. Šibenik");
           //ažuriraj poziv
           HdPozivJdbc dao_poziv = new HdPozivJdbc();
           HelpDeskPozivVo email = new HelpDeskPozivVo();
           email.setIdPoziva(value.getIdObjekta());
           if(!value.get("@tip_prosljedivanja").equals("samo_proslijedi")){
	           if(value.getIdOsobe().length()>0){
	               email.setOsobaRasporeda(value.getIdOsobe());
	               dao_poziv.daoStore(email);
	           }
           }
           //send email procedura
           email.set(AS2EmailConstants.RECIPIENTS, value.getTo());
           if(vo.get("@@tijelo_poruke").length()>0) 
               email.set(AS2EmailConstants.BODY, vo.get("@@tijelo_poruke")+body.toString());   
           else email.set(AS2EmailConstants.BODY, sb.toString());
           if(value.get("@@vrsta").equals("9"))  
               email.set(AS2EmailConstants.SUBJECT, "Obavijest br: "+value.getIdObjekta()); 
           else {
               if(value.getIdTabele().equals("isms_incident")) 
                   email.set(AS2EmailConstants.SUBJECT, "Incident br: "+value.getIdObjekta()); 
               else email.set(AS2EmailConstants.SUBJECT, "Zahtjev br: "+value.getIdObjekta()); 
           }
           email.set(AS2EmailConstants.PROFILE_NAME,"Help Desk"); 
           email.set(AS2EmailConstants.IZBOR,value.getIzbor());
           email.set(AS2EmailConstants.GRUPA,value.getGrupa());
           email.set(AS2EmailConstants.ORG_JEDINICA_RADA,value.getOrgJedinicaRada());
           email.set("@@to_jmbg",value.get("obavjestena_osoba"));  
           dao_poziv.daoSendEmail(email);
           // dodaj dopuštene osobe u zahtjev
           if(!value.getIdOsobe().equals("")){
	           HelpDeskPozivVo value_vo = new HelpDeskPozivVo();
	           value_vo.set("broj_",value.getIdObjekta());
	           value_vo.set("dopustena_osoba",value.getIdOsobe());
	           HelpDeskFacadeServer.getInstance().dodajDopusteniZahtjev(value_vo);
           }
           return value;
    }
    //privitak poziv
    public HelpDeskPozivPrivitakRs procitajSvePrivitke(HelpDeskPozivPrivitakVo value)  {
        HdPozivPrivitakJdbc dao = new HdPozivPrivitakJdbc();
        HelpDeskPozivPrivitakRs rs = new HelpDeskPozivPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskPozivPrivitakVo dodajPrivitak(HelpDeskPozivPrivitakVo value)  {
        HdPozivPrivitakJdbc dao = new HdPozivPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public HelpDeskPozivPrivitakVo citajPrivitak(HelpDeskPozivPrivitakVo value)  {
        HdPozivPrivitakJdbc dao = new HdPozivPrivitakJdbc();
        return new HelpDeskPozivPrivitakVo(dao.daoLoad(value));
    }
    public HelpDeskPozivPrivitakVo brisiPrivitak(HelpDeskPozivPrivitakVo value)  {
        HdPozivPrivitakJdbc dao = new HdPozivPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    public HelpDeskRadniNalogPrivitakRs procitajSvePrivitkeRn(HelpDeskRadniNalogPrivitakVo value)  {
        HdRadniNalogPrivitakJdbc dao = new HdRadniNalogPrivitakJdbc();
        HelpDeskRadniNalogPrivitakRs rs = new HelpDeskRadniNalogPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskRadniNalogPrivitakVo dodajPrivitakRn(HelpDeskRadniNalogPrivitakVo value)  {
        HdRadniNalogPrivitakJdbc dao = new HdRadniNalogPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public HelpDeskRadniNalogPrivitakVo citajPrivitakRn(HelpDeskRadniNalogPrivitakVo value)  {
        HdRadniNalogPrivitakJdbc dao = new HdRadniNalogPrivitakJdbc();
        return new HelpDeskRadniNalogPrivitakVo(dao.daoLoad(value));
    }
    public HelpDeskRadniNalogPrivitakVo brisiPrivitakRn(HelpDeskRadniNalogPrivitakVo value)  {
        HdRadniNalogPrivitakJdbc dao = new HdRadniNalogPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    public HelpDeskKontaktRs procitajSveKontakte(HelpDeskKontaktVo value)  {
        HdKontaktJdbc dao = new HdKontaktJdbc();
        HelpDeskKontaktRs rs = new HelpDeskKontaktRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskKontaktRs pronadiKontakte(HelpDeskKontaktVo value)  {
        HdKontaktJdbc dao = new HdKontaktJdbc();
        HelpDeskKontaktRs rs = new HelpDeskKontaktRs(dao.daoFind(value));
		return rs;
    }
    public HelpDeskKontaktVo azurirajKontakt(HelpDeskKontaktVo value)  {
        HdKontaktJdbc dao = new HdKontaktJdbc();
        dao.daoStore(value);
        return value;
    }
    public HelpDeskKontaktVo dodajKontakt(HelpDeskKontaktVo value)  {
        HdKontaktJdbc dao = new HdKontaktJdbc();
        dao.daoCreate(value);
        return value;
    }
    public HelpDeskKontaktVo brisiKontakt(HelpDeskKontaktVo value)  {
        HdKontaktJdbc dao = new HdKontaktJdbc();
        dao.daoRemove(value);
        return value;
    }
}
