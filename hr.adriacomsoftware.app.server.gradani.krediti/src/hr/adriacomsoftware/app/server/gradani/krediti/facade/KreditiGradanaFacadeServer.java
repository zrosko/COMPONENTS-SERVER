package hr.adriacomsoftware.app.server.gradani.krediti.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.ObavijestRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.ObavijestVo;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaVo;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.RocnostRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.RocnostVo;
import hr.adriacomsoftware.app.common.gradani.krediti.facade.KreditiGradanaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.OsobaJdbc;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.ObavijestGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.OpomenaGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.PartijaKreditaGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.RocnostKreditaGradanaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KreditiGradanaFacadeServer extends AS2FacadeServerLayer
		implements KreditiGradanaFacade {

	private static KreditiGradanaFacadeServer _instance = null;
	public static KreditiGradanaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new KreditiGradanaFacadeServer();
		}
		return _instance;
	}
	private KreditiGradanaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public OsobaRs pronadiSveOsobe(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        if(value.exists("@izbor_a")){
            OsobaJdbc dao_osoba = new OsobaJdbc();
            return dao_osoba.daoPodaciKlijenta(value);
        }else
            return dao.daoPronadiOsobe(value, true);
    }
	public OsobaRs procitajSveOsobe()  {
	    PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoPronadiOsobe(new OsnovniVo(), false);
	 }
	public RocnostRs izvjestajRocnostiOtplate(RocnostVo value)  {
	    if(value.exists("@@ExcelSve")){ 
	        RocnostRs _rocnost = new RocnostRs();
	        RocnostKreditaGradanaJdbc dao = new RocnostKreditaGradanaJdbc();
	        _rocnost.addResultSet("Rekapitulacija",dao.daoRocnostSintetikaGlavnaKnjiga(value));
	        value.setGlavnicaKamate(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE);
		    _rocnost.addResultSet("Glavnica i kamate",dao.daoListaRocnosti(value));
		    value.setGlavnicaKamate(RocnostVo.BI_VRSTA_ROCNOSTI_KAMATE);
		    _rocnost.addResultSet("Kamate",dao.daoListaRocnosti(value));
	        value.setGlavnicaKamate(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA);
		    _rocnost.addResultSet("Glavnica",dao.daoListaRocnosti(value));
		    return _rocnost;
	    }else {
		    RocnostKreditaGradanaJdbc dao = new RocnostKreditaGradanaJdbc();
		    RocnostRs _rocnost = new RocnostRs(dao.daoListaRocnosti(value));
		    RocnostRs _glavna_knjiga = new RocnostRs(dao.daoRocnostSintetikaGlavnaKnjiga(value));
		    _rocnost.addResultSet("rocnost_lista_sub_gk", _glavna_knjiga); 
		    return _rocnost;
	    }
	 }
	public RocnostRs izvjestajRocnostiOtplateOib(RocnostVo value)  {
	    RocnostKreditaGradanaJdbc dao = new RocnostKreditaGradanaJdbc();
	    return dao.daoRocnostZaKomitenta(value);
	 }
	public OsnovniRs izvjestajHBORKredita(OsnovniVo value)  {
	    PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
        return dao.daoIzvjesceHBOR(value);
	 }

    public OsnovniRs izvjestajSviDospjeliKrediti(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
        return new OsnovniRs(dao.daoSviDospjeliKrediti(value));
    }
    public OsnovniRs izvjestajDospjeliKrediti(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
        return new OsnovniRs(dao.daoDospjeliKrediti(value));
    }
    public OsnovniRs izvjestajZatezneKamate(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoKamatePoKlasiRacuna(value, "6");
    }
    public OsnovniRs izvjestajUgovoreneKamate(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoKamatePoKlasiRacuna(value, "7");
    }
    public OsnovniRs izvjestajHamagKrediti(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoHamagIzvjesce(value);
    }
    public PartijaRs izvjestajIzdanihKreditaZaPeriod(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoListaIzdanihKreditaZaPeriod(value);
    }  
    public PartijaRs izvjestajStanjeKontaPartija(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoStanjeKontaPartija(value);
    }
    public OsnovniRs izvjestajInterkalarneKamate(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoKamatePoKlasiRacuna(value, "9");
    }
    public OsnovniRs izvjestajNaknade(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoKamatePoKlasiRacuna(value, "5");
    }
    public OsnovniRs izvjestajOpomene(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoKamatePoKlasiRacuna(value, "8");
    }
    public PartijaRs izvjestajPotvrdaPlacenihKamata(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoPotvrdaPlacenihKamata(value);
    }    
    public PartijaRs izvjestajUsporedbaPrometaSaGlavnomKnjigom(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoUsporedbaPrometaSaGlavnomKnjigom(value);
    }
    public OpomenaRs izvjestajOpomeneKredita(OpomenaVo value)  {
	    OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
	    return new OpomenaRs(dao.daoListaOpomena(value));
	 }
	public OpomenaRs iskljuciOpomene(OpomenaVo value)  {
	    OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
        dao.daoIskljuciUkljuciOpomene(value,OpomenaVo.VALID_IND_NE);
        return new OpomenaRs(); 
    }
    public OpomenaRs ukljuciOpomene(OpomenaVo value)  {
        OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
        dao.daoIskljuciUkljuciOpomene(value,OpomenaVo.VALID_IND_DA);
        return new OpomenaRs(); 
    }
    public OpomenaRs procitajSveObradeOpomena(OpomenaVo value)  {
        OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
        return new OpomenaRs(dao.daoListaObradaOpomena(value));
    }
    public ObavijestRs izvjestajObavijestiOtplataKredita(ObavijestVo value)  {
        ObavijestGradanaJdbc dao = new ObavijestGradanaJdbc();
        return dao.daoListaObavijesti(value);
    }
    public ObavijestRs iskljuciObavijesti(ObavijestVo value)  {
        ObavijestGradanaJdbc dao = new ObavijestGradanaJdbc();
        dao.daoIskljuciUkljuciObavijesti(value,ObavijestVo.VALID_IND_NE);
        return new ObavijestRs(); 
    }
    public ObavijestRs ukljuciObavijesti(ObavijestVo value)  {
        ObavijestGradanaJdbc dao = new ObavijestGradanaJdbc();
        dao.daoIskljuciUkljuciObavijesti(value,ObavijestVo.VALID_IND_DA);
        return new ObavijestRs(); 
    }
    public ObavijestRs procitajSveObradeObavijesti(ObavijestVo value)  {
        ObavijestGradanaJdbc dao = new ObavijestGradanaJdbc();
        return new ObavijestRs(dao.daoListaObradaObavijesti(value));
    }
    public PartijaRs izvjestajNaplataRezervacija(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
        dao.daoCallSpDatumOdDatumDo(value, "bi_gr_krediti_naplata_rezervacija");
        J2EESqlBuilder sb = new J2EESqlBuilder();
        sb.append("select * from bi_gr_krediti_izvjestaj_naplata_rezervacija");
        if(value.get("SSP").equals("SSP")){
            sb.append(" where broj_partije >= 7300000000");
        }else{
            sb.append(" where broj_partije < 7300000000");
        }
        sb.append(" order by broj_partije, rezerva");   
        return new PartijaRs(dao.daoExecuteQuery(sb.toString()));         	
    }
    public OsnovniRs pregledPrometaZaPeriod(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
        return dao.daoPregledPrometaZaPeriod(value);
    }
    public OsnovniRs izvjestajNaplataPotrazivanja(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoNaplataPotrazivanja(value);
    }
    public OsnovniRs izvjestajPoredakKredita(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoPoredakKredita(value);
    }
    public OsnovniRs izvjestajRasporedAnuiteta(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoRasporedAnuiteta(value);
    }
    public OsnovniRs izvjestajRasporedAnuitetaRekap(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoRasporedAnuitetaRekap(value);
    }
    public OsnovniRs izvjestajPretplata(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoPretplata(value);
    }
    public OsnovniRs izvjestajProlazniRacun(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoProlazniRacun(value);
    }
    public OsnovniRs izvjestajHNBKreditiZaKupnjuDionica(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoHNBKreditiZaKupnjuDionica(value);
    }
    public OsnovniRs izvjestajR1(OsnovniVo value)  {
        PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc(); 
        return dao.daoR1(value);
    }
}
