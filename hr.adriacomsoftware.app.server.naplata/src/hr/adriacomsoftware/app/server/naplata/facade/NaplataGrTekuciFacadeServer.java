package hr.adriacomsoftware.app.server.naplata.facade;


import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciBiljeskaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciBiljeskaVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciDopisRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciDopisVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciObradaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciObradaVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciPracenjeRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciPracenjeVo;
import hr.adriacomsoftware.app.common.naplata.facade.NaplataGrTekuciFacade;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrTekuciBiljeskaJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrTekuciDopisJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrTekuciJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrTekuciObradaJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrTekuciPracenjeJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.format.AS2Format;
import hr.as2.inf.common.reports.AS2ReportRenderer;
import hr.as2.inf.common.reports.AS2ReportRendererFactory;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


public final class NaplataGrTekuciFacadeServer extends AS2FacadeServerLayer 
		implements NaplataGrTekuciFacade {

	private static NaplataGrTekuciFacadeServer _instance = null;
	public static NaplataGrTekuciFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new NaplataGrTekuciFacadeServer();
		}
		return _instance;
	}
	private NaplataGrTekuciFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public NaplataGrTekuciPracenjeVo brisiNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrTekuciPracenjeVo dodajNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setDatumPrebacivanja(AS2Date.getTodayAsCalendar());
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		AS2Record res = dao.daoCreate(value);
		return new NaplataGrTekuciPracenjeVo(res);
	}
	public NaplataGrTekuciPracenjeVo azurirajNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		AS2Record res = dao.daoStore(value);
		return new NaplataGrTekuciPracenjeVo(res);
	}
	public NaplataGrTekuciPracenjeRs procitajSveNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		if(value.get("@@arhiva").equals("arhiva"))
			return new NaplataGrTekuciPracenjeRs(dao.daoFindAllArhiva(value));
		else
			return new NaplataGrTekuciPracenjeRs(dao.daoFindAll(value));
	}
	public AS2RecordList listajSveNaplataGrTekuciPracenje()  {
		return new AS2RecordList();
	}
	public NaplataGrTekuciPracenjeRs pretraziNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		if(value.get("@tip_pretrazivanja").equals("postojece"))
			return new NaplataGrTekuciPracenjeRs(dao.daoFindPostojece(value));
		else if(value.get("@tip_pretrazivanja").equals("novo"))
			return new NaplataGrTekuciPracenjeRs(dao.daoFindNovo(value));
		else
			return new NaplataGrTekuciPracenjeRs();
	}
	public NaplataGrTekuciPracenjeVo procitajiNaplataGrTekuciPracenje(NaplataGrTekuciPracenjeVo value)  {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		return dao.daoLoad(value);
	}
	public NaplataGrTekuciPracenjeVo dodajNoveKlijente(NaplataGrTekuciPracenjeRs value)	 {
		NaplataGrTekuciPracenjeJdbc dao = new NaplataGrTekuciPracenjeJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.addValue("referent",user.get("korisnik"));
        value.addValue("datum_prebacivanja",AS2Date.getTodayAsCalendar());
        value.addValue("vrijeme_zadnje_izmjene",AS2Date.getTodayAsCalendar());
        value.addValue("operater_zadnje_izmjene",user.get("korisnik"));
        value.addValue("status_pracenja","Čeka na obradu");
		dao.daoCreateMany(value);
		return new NaplataGrTekuciPracenjeVo();
	}
	public NaplataGrTekuciBiljeskaVo brisiNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrTekuciBiljeskaVo dodajNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		AS2Record res = dao.daoCreate(value);
		return new NaplataGrTekuciBiljeskaVo(res);
	}
	public NaplataGrTekuciBiljeskaVo azurirajNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		AS2Record res = dao.daoStore(value);
		return new NaplataGrTekuciBiljeskaVo(res);
	}
	public NaplataGrTekuciBiljeskaRs procitajSveNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		return new NaplataGrTekuciBiljeskaRs(dao.daoFindAll(value));
	}
	public AS2RecordList listajSveNaplataGrTekuciBiljeska()  {
		return new AS2RecordList();
	}
	public NaplataGrTekuciBiljeskaRs pretraziNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		return new NaplataGrTekuciBiljeskaRs(dao.daoFind(value));
	}

	public NaplataGrTekuciBiljeskaVo procitajiNaplataGrTekuciBiljeska(NaplataGrTekuciBiljeskaVo value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		return dao.daoLoad(value);
	}
	public NaplataGrTekuciBiljeskaVo brisiViseBiljeski(NaplataGrTekuciBiljeskaRs value)  {
		NaplataGrTekuciBiljeskaJdbc dao = new NaplataGrTekuciBiljeskaJdbc();
		dao.daoRemoveMany(value);
		return new NaplataGrTekuciBiljeskaVo();
	}
	public NaplataGrTekuciDopisVo brisiNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrTekuciDopisVo dodajNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
        if (value.get("@@privitak_dodaj").equals("true")) {
			value.set("@@privitak_dodaj", "false");
		}
		AS2Record res = dao.daoCreate(value);
		return new NaplataGrTekuciDopisVo(res);
	}
	public NaplataGrTekuciDopisVo azurirajNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
        value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		if(value.get("@@privitak_dodaj").equals("true")){
			value.set("@@privitak_dodaj","false");
		}else if(value.get("@@privitak_ukloni").equals("true")){
    		value.set("@@privitak_ukloni","false");
		}
		AS2Record res = dao.daoStore(value);
		return new NaplataGrTekuciDopisVo(res);
	}
	public NaplataGrTekuciDopisRs procitajSveNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		return new NaplataGrTekuciDopisRs(dao.daoFindAll(value));
	}
	public AS2RecordList listajSveNaplataGrTekuciDopis()  {
		return new AS2RecordList();
	}
	public NaplataGrTekuciDopisRs pretraziNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		return new NaplataGrTekuciDopisRs(dao.daoFind(value));
	}
	public NaplataGrTekuciDopisVo procitajiNaplataGrTekuciDopis(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		return dao.daoLoad(value);
	}
	public NaplataGrTekuciDopisVo brisiViseDopisa(NaplataGrTekuciDopisRs value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
		dao.daoRemoveMany(value);
		return new NaplataGrTekuciDopisVo();
	}
	public NaplataGrTekuciDopisVo citajPrivitak(NaplataGrTekuciDopisVo value)  {
		NaplataGrTekuciDopisJdbc dao = new NaplataGrTekuciDopisJdbc();
	    return new NaplataGrTekuciDopisVo(dao.daoLoad(value));
	}
	public NaplataGrTekuciObradaVo brisiNaplataGrTekuciObrada(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		dao.daoRemove(value);
		dao.daoRemoveDopis(value);
		return value;
	}
	public NaplataGrTekuciObradaVo dodajNaplataGrTekuciObrada(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new NaplataGrTekuciObradaVo(res);
	}
	public NaplataGrTekuciObradaVo azurirajNaplataGrTekuciObrada(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		AS2Record res = dao.daoStore(value);
		return new NaplataGrTekuciObradaVo(res);
	}
	public NaplataGrTekuciObradaRs procitajSveNaplataGrTekuciObrada(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		return new NaplataGrTekuciObradaRs(dao.daoFindAll(value));
	}
	public AS2RecordList listajSveNaplataGrTekuciObrada()  {
		return new AS2RecordList();
	}
	public NaplataGrTekuciObradaRs pretraziNaplataGrTekuciObrada(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		return new NaplataGrTekuciObradaRs(dao.daoFind(value));
	}
	public NaplataGrTekuciObradaVo citajObradu(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
	    return new NaplataGrTekuciObradaVo(dao.daoLoad(value));
	}
	public NaplataGrTekuciObradaRs citajStavkeObrade(NaplataGrTekuciObradaVo value)  {
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
	    return new NaplataGrTekuciObradaRs(dao.daoLoadStavke(value));
	}
	//SVAKI VO unutar RS ima (broj_partije, id_pracenja (id_predmeta), naziv) osim toga i (datum,vrsta_dokumenta) 
	public AS2Record dodajObradu(NaplataGrTekuciPracenjeRs value)  {
		AS2User user = (AS2User) value.getProperty(AS2Constants.USER_OBJ);
		String vrsta_dokumenta = value.get("vrsta_dokumenta")+".jasper";	
		Calendar datum = value.getAsCalendar("datum");
		String korisnik = user.get("korisnik");
		String DEFAULT_REPORTS_SERVICE_PATH = "hr/adriacomsoftware/resources/common/reports/tekuci/dopisi/"+vrsta_dokumenta;
		String DEFAULT_REPORTS_TYPE = ".jasper";
		String DEFAULT_REPORTS_FORMAT = "pdf";
		AS2RecordList grupa_rs = new AS2RecordList();
		NaplataGrTekuciDopisJdbc dao_dopis = new NaplataGrTekuciDopisJdbc();
		NaplataGrTekuciObradaJdbc dao_obrada = new NaplataGrTekuciObradaJdbc();
		NaplataGrTekuciObradaVo vo_ = new NaplataGrTekuciObradaVo();
		dao_obrada.daoCreate(vo_);
		String last_id_obrade = dao_obrada.daoLastIdObrade();

		Iterator<AS2Record> E = value.iteratorRows();
		while(E.hasNext()){
			AS2Record vo = E.next();
			OsnovniVo osnovni_vo = new OsnovniVo(vo);
			osnovni_vo.set("@@report_selected",value.get("vrsta_dokumenta"));
			osnovni_vo.setCalendarAsDateString(datum, "datum");
			AS2RecordList jedan_rs = izvjestaji(osnovni_vo);//value;//TODO zovi bazu - vo
			grupa_rs.appendRowsOnly(jedan_rs);
			//pripremi za slanje na server (pojedinačno)
			File reportFile = new File(DEFAULT_REPORTS_SERVICE_PATH);
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(DEFAULT_REPORTS_TYPE);
			byte[] byteArray =	renderer.renderReport(reportFile.getPath(),parameters,jedan_rs,DEFAULT_REPORTS_FORMAT);
			NaplataGrTekuciDopisVo vo_dopis = new NaplataGrTekuciDopisVo(vo);
			vo_dopis.setIdPracenja(osnovni_vo.get("id_pracenja"));
			vo_dopis.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"datum_dogadaja");
			vo_dopis.setReferent(korisnik);
			vo_dopis.setDopisNaziv(value.get("vrsta_dokumenta"));
			vo_dopis.setDatumIzdavanja(datum);
			vo_dopis.setKoristiAltAdresu("NE");
			vo_dopis.set("dokument", byteArray);
			vo_dopis.setNazivDokumenta(value.get("vrsta_dokumenta")+"_"+AS2Format.stringReplace(osnovni_vo.get("naziv"), " ", "_")+"."+DEFAULT_REPORTS_FORMAT);	
			vo_dopis.setIdObrade(last_id_obrade);
			vo_dopis.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"vrijeme_zadnje_izmjene");
			vo_dopis.setOperaterZadnjeIzmjene(korisnik);
			dao_dopis.daoCreate(vo_dopis);
		}
		//pripremi za spremanje grupnog izvještaja na server
		NaplataGrTekuciObradaVo vo_grupa = new NaplataGrTekuciObradaVo();
		File reportFile = new File(DEFAULT_REPORTS_SERVICE_PATH);
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		AS2ReportRenderer renderer = AS2ReportRendererFactory.getInstance().createRenderer(DEFAULT_REPORTS_TYPE);
		byte[] byteArray =	renderer.renderReport(reportFile.getPath(),parameters,grupa_rs,DEFAULT_REPORTS_FORMAT);
		vo_grupa.setIdObrade(last_id_obrade);
		vo_grupa.set("dokument", byteArray);//TODO dodati vo
		vo_grupa.setDatumObrade(datum);
		vo_grupa.setDatumPocetak(datum);
		vo_grupa.setDatumKraj(datum);
		vo_grupa.setVrstaDokumenta(value.get("vrsta_dokumenta"));
		vo_grupa.setNazivDokumenta(value.get("vrsta_dokumenta")+"."+DEFAULT_REPORTS_FORMAT);
		vo_grupa.setBrojStavki(""+grupa_rs.size());
		vo_grupa.setKorisnik(korisnik);		
		NaplataGrTekuciObradaJdbc dao = new NaplataGrTekuciObradaJdbc();
		dao.daoStore(vo_grupa);
		return value;
	}
	//Trenutno na klijentu imamo logiku za Jasper-TODO treba omogućiti rad na serveru
	public AS2Record dodajObraduBRISI(NaplataGrTekuciPracenjeRs value)  {
		NaplataGrTekuciDopisJdbc dao_dopis = new NaplataGrTekuciDopisJdbc();
		NaplataGrTekuciObradaJdbc dao_obrada = new NaplataGrTekuciObradaJdbc();
		NaplataGrTekuciObradaVo vo_ = new NaplataGrTekuciObradaVo();
		dao_obrada.daoCreate(vo_);
		String last_id_obrade = dao_obrada.daoLastIdObrade();

		Iterator<AS2Record> E = value.iteratorRows();
		while(E.hasNext()){
			AS2Record vo = E.next();
			if(vo.get("@tip").equalsIgnoreCase("single")){
				NaplataGrTekuciDopisVo vo_dopis = new NaplataGrTekuciDopisVo(vo);
				vo_dopis.setIdObrade(last_id_obrade);
				dao_dopis.daoCreate(vo_dopis);
			}
			else if(vo.get("@tip").equalsIgnoreCase("group")){
				NaplataGrTekuciObradaVo vo_grupa = new NaplataGrTekuciObradaVo(vo);
				vo_grupa.setIdObrade(last_id_obrade);
				dao_obrada.daoStore(vo_grupa);
			}
		}
		return value;
	}
	public OsnovniRs procitajSifre(OsnovniVo value) {
		NaplataGrTekuciJdbc dao = new NaplataGrTekuciJdbc();
		return dao.daoProcitajSifre(value);
	}
	public OsnovniRs izvjestaji(OsnovniVo value)  {
		NaplataGrTekuciJdbc dao = new NaplataGrTekuciJdbc();
		return dao.daoIzvjestaji(value);
	}
 }
