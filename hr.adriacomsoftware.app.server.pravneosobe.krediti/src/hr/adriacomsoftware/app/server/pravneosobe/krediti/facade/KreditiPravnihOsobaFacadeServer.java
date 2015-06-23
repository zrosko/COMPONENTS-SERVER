package hr.adriacomsoftware.app.server.pravneosobe.krediti.facade;

import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.IzvodVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.KreditniProgramRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.KreditniProgramVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OpomenaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OpomenaVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OtplatniPlanRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OtplatniPlanVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.PartijaKreditaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.PartijaKreditaVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.RocnostRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.RocnostVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.facade.KreditiPravnihOsobaFacade;
import hr.adriacomsoftware.app.server.gk.da.jdbc.TemeljnicaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.as400.jdbc.IzvjestajAs400Jdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.IzvodiPravihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.KreditniProgramJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc.OpomenaPravnihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc.PartijaKreditaPravnihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc.RocnostKreditaPravihOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KreditiPravnihOsobaFacadeServer extends AS2FacadeServerLayer
		implements KreditiPravnihOsobaFacade {

	private static KreditiPravnihOsobaFacadeServer _instance = null;

	public static KreditiPravnihOsobaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new KreditiPravnihOsobaFacadeServer();
		}
		return _instance;
	}

	private KreditiPravnihOsobaFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public PravnaOsobaRs pronadiSvePravneOsobeKredite(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPronadiPravneOsobe(value, true);
	}

	public PravnaOsobaRs procitajSvePravneOsobeKredite() {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPronadiPravneOsobe(new OsnovniVo(), false);
	}

	public OpomenaRs izvjestajOpomeneKredita(OpomenaVo value) {
		OpomenaPravnihOsobaJdbc dao = new OpomenaPravnihOsobaJdbc();
		return new OpomenaRs(dao.daoListaOpomena(value));
	}

	public AS2RecordList temeljnica(AS2Record value) {
		TemeljnicaJdbc dao = new TemeljnicaJdbc();
		return dao.daoTemeljnica(value, value.get("@@TEMELJNICA"));
	}

	public RocnostRs izvjestajRocnostiOtplateKonto(RocnostVo value) {
		RocnostKreditaPravihOsobaJdbc dao = new RocnostKreditaPravihOsobaJdbc();
		// ONLINE
		if (value.getBrojPartije().length() > 0) {
			if (value.getBrojPartijeDo().length() == 0)
				value.setBrojPartijeDo(value.getBrojPartije());
			if (value.getValutaProtuvaluta().length() == 0)
				value.setValutaProtuvaluta("V");
			return new RocnostRs(dao.daoListaRocnostiKontoOnline(value));
		} else {
			if (value.getVrstaRocnostiOnline().equals(RocnostVo.OPTION_NE)) {
				return new RocnostRs(dao.daoListaRocnostiKonto(value));
			} else {
				return new RocnostRs(
						dao.daoListaRocnostiKontoMaticniBrojOnline(value));
			}
		}
	}

	public RocnostRs izvjestajRocnostiOtplateKomitent(RocnostVo value) {
		RocnostRs rs = new RocnostRs();
		RocnostKreditaPravihOsobaJdbc dao = new RocnostKreditaPravihOsobaJdbc();
		if(value.get("kontekst").equals("nadleznost"))
	    	rs = dao.daoListaRocnostiNadleznost(value);
	    else
	    	rs = dao.daoListaRocnostiKomitent(value);
		return rs;
	}

	public AS2RecordList izvjestajIzvodiKredita(IzvodVo value) {
		IzvodiPravihOsobaJdbc dao = new IzvodiPravihOsobaJdbc();
		return dao.daoListaIzvodaKrediti(value);

	}

	public KreditniProgramRs izvjestajKreditniProgram(KreditniProgramVo value) {
		/* Lista kredita obrtnika (Caritas,...)* */
		KreditniProgramJdbc dao = new KreditniProgramJdbc();
		return new KreditniProgramRs(dao.daoListaKreditniProgram(value));
	}

	public OsnovniRs izvjestajPrometNaDan26017(OsnovniVo value) {
		IzvjestajAs400Jdbc dao = new IzvjestajAs400Jdbc();
		OsnovniRs ret = new OsnovniRs(dao.daoListaPrometNaDan26017(value));
		return ret;
	}

	public IzvjestajRs izvjestajStanjeKreditaPoKomitentimaSuzeni(
			IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return new IzvjestajRs(dao.daoIzvjestajStanjaKreditaSuzeni(value));
	}

	public IzvjestajRs izvjestajStanjeKreditaPoKomitentima(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return new IzvjestajRs(dao.daoIzvjestajStanjaKredita(value));
	}

	public OsnovniRs izvjestajHamagKrediti(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoHamagIzvjesce(value);
	}

	public PartijaRs izvjestajKreditniProgramSvePartije(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoListaKreditniProgramSvePartije(value);
	}

	public IzvjestajRs izvjestajRevalorizacijaPoKontima(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return new IzvjestajRs(dao.daoRevalorizacijaPoKontima(value));
	}

	public IzvjestajRs izvjestajRevalorizacijaPoPartijama(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return new IzvjestajRs(dao.daoRevalorizacijaPoPartijama(value));
	}

	public OsnovniRs pregledUgovora(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPregledUgovora(value);
	}

	public OsnovniRs pregledPrometa(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPregledPrometa(value);
	}

	public OsnovniRs pregledPrometaZaPeriod(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPregledPrometaZaPeriod(value);
	}

	public OsnovniRs pregledBoniteta(OsnovniVo value) {
		return null;
	}

	public OsnovniRs pregledStanja(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPregledStanja(value);
	}

	public OsnovniRs izvjestajKamatniOstaliPrihodiZbirno(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKamatniOstaliPrihodi(value, true);
	}

	public OsnovniRs izvjestajKamatniOstaliPrihodiPoKontu(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKamatniOstaliPrihodi(value, false);
	}

	public OpomenaRs procitajSveObradeOpomena(OpomenaVo value) {
		OpomenaPravnihOsobaJdbc dao = new OpomenaPravnihOsobaJdbc();
		return new OpomenaRs(dao.daoListaObradaOpomena(value));
	}

	public OpomenaRs iskljuciOpomene(OpomenaVo value) {
		OpomenaPravnihOsobaJdbc dao = new OpomenaPravnihOsobaJdbc();
		dao.daoIskljuciUkljuciOpomene(value, OpomenaVo.VALID_IND_NE);
		return new OpomenaRs();
	}

	public OpomenaRs ukljuciOpomene(OpomenaVo value) {
		OpomenaPravnihOsobaJdbc dao = new OpomenaPravnihOsobaJdbc();
		dao.daoIskljuciUkljuciOpomene(value, OpomenaVo.VALID_IND_DA);
		return new OpomenaRs();
	}

	public OsnovniRs izvjestajNovoOdobreniKrediti(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoNovoodobreniKrediti(value);
	}

	public OsnovniRs izvjestajPromjenaFazeKredita(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPromjenaFazeKredita(value);
	}

	public OtplatniPlanRs izradiOtplatniPlan(OtplatniPlanVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoOtplatniPlan(value, "bi_po_krediti_otplatni_plan");
	}

	public IzvjestajRs izvjestajPregledDospjelihObveza(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return new IzvjestajRs(dao.daoDospjeleObveze(value));
	}

	public IzvjestajRs izvjestajPoveznicaPoPartiji(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPoveznicaPartija(value);
	}

	public IzvjestajRs izvjestajRacunDobitiGubitka(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoRacunDobitiGubitka(value);
	}

	public PartijaKreditaRs izvjestajAnalizaKredita(PartijaKreditaVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoAnalizaKredita(value);
	}

	public IzvjestajRs izvjestajKreditiFazaDva(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKreditiFazaDva(value);
	}

	public OsnovniRs izvjestajTudaKnjizenja(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoTudaKnjizenja(value);
	}

	public IzvjestajRs izvjestajKreditiFazaCetiri(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKreditiFazaCetiri(value);
	}

	public OsnovniRs izvjestajPriljeviPoKreditimaBuduci(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPriljeviPoKreditimaBuduci(value);
	}

	public OsnovniRs izvjestajPriljeviPoKreditima(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPriljeviPoKreditima(value);
	}

	public OsnovniRs izvjestajManipulativniTroskoviKnjizenja(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoManipulativniTroskoviKnjizenja(value);
	}

	public OsnovniRs izvjestajManipulativniTroskovi(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoManipulativniTroskovi(value);
	}

	public OsnovniRs izvjestajPretplata(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoPretplata(value);
	}

	public OsnovniRs izvjestajBrutoBilanca(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoBrutoBilanca(value);
	}

	public OsnovniRs izvjestajNovootvoreniKreditiZaRazdoblje(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoNovootvoreniKrediti(value);
	}

	public OsnovniRs izvjestajNovootvoreneGarancijeZaRazdoblje(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoNovootvoreneGarancije(value);
	}

	public OsnovniRs izvjestajSaldoKunskihRacunaKredita(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoSaldoKunskihRacuna(value);
	}

	public OsnovniRs izvjestajIzdaniKrediti(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoIzdaniKrediti(value, value.getAsInt("@@vrsta"),
				value.getAsInt("@@tip"));
	}

	public OsnovniRs izvjestajSaldaKontaKnjizenja(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoSaldaKontaKnjizenja(value);
	}

	public OsnovniRs izvjestajSaldaKontaSuspendiranihPrihoda(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoSaldaKontaSuspendiranihPrihoda(value);
	}

	public IzvjestajRs izvjestajKreditiFazaTri(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKreditiFazaTri(value);
	}

	public IzvjestajRs izvjestajGarancijeFazaTri(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoGarancijeFazaTri(value);
	}

	public OsnovniRs izvjestajRezervacije(OsnovniVo value) {
		return null;
	}

	public OsnovniRs izvjestajKreditiKnjizenjaNedospjeleGlavnice(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoKreditiKnjizenjaNedospjeleGlavnice(value);
	}

	public OsnovniRs izvjestajIOSObrazac(OsnovniVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.daoIOSObrazac(value);
	}

	public OsnovniRs izvjestajRocnostiOtplate(OsnovniVo value) {
		RocnostKreditaPravihOsobaJdbc dao = new RocnostKreditaPravihOsobaJdbc();
		RocnostVo vo = new RocnostVo(value);
		return new OsnovniRs(dao.daoListaRocnostiKontoMaticniBrojOnline(vo));
	}

	public IzvjestajRs izvjestajPregledIskljucenihPartija(IzvjestajVo value) {
		PartijaKreditaPravnihOsobaJdbc dao = new PartijaKreditaPravnihOsobaJdbc();
		return dao.izvjestajPregledIskljucenihPartija(value);
	}
}