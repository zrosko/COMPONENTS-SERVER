package hr.adriacomsoftware.app.server.evidencijadolazaka.facade;

import hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc.PlaceEvidencijaJdbc;
import hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc.PlaceEvidencijaPotvrdaJdbc;
import hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc.PlaceOvlastiJdbc;
import hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc.PlaceSifrarnikJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;


public final class PlaceFacadeServer {

	private static PlaceFacadeServer _instance = new PlaceFacadeServer();

	public static PlaceFacadeServer getInstance() {
		return _instance;
	}

	private PlaceFacadeServer() {
		AS2Context.setSingletonReference(this);
	}
	private String getUserName(AS2Record value){
		AS2User user = (AS2User) value.getAsObject(AS2Constants.USER_OBJ);
		return user.getUserName();
	}
	public AS2Record dodajEvidenciju(AS2Record value)  {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));//TODO korisnik
		value.set("dodatni_unos", "1");
//		value.set("obracun_sati", value.get(key));
		if(!value.get("element_obracuna_id").equals("")){
			value.append(dao.daoCreate(value));
		}
		return value;
	}
	
	public AS2Record dodajAzurirajEvidenciju(AS2Record value)  {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));//TODO korisnik
		
		AS2Record novi_vo = new AS2Record();
		//default values
		novi_vo.set("radnik_id",value.get("_old_radnik_id"));
		novi_vo.set("element_obracuna_id",value.get("_old_element_obracuna_id"));
		novi_vo.set("datum",AS2Date.formatStringDateToAny(value.get("_old_datum"), "yyyy-MM-dd"));
		novi_vo.set("radno_vrijeme_od",AS2Date.formatStringDateToAny(value.get("_old_radno_vrijeme_od"), "HH:mm:ss"));
		novi_vo.set("radno_vrijeme_do",AS2Date.formatStringDateToAny(value.get("_old_radno_vrijeme_do"), "HH:mm:ss"));
		novi_vo.set("obracun_sati",value.get("_old_obracun_sati"));
		novi_vo.set("obracun_minuta",value.get("_old_obracun_minuta"));
		
		//new values
		novi_vo.append(value);
		/*if(value.get("element_obracuna_id").equals("") && value.get("id_dnevne_evidencije").length()!=0){
			dao.daoRemove(novi_vo);
			System.out.print(" daoRemove ");
			return novi_vo;
		}else*/ if(value.get("id_dnevne_evidencije").length()==0){
			novi_vo.append(dao.daoCreate(novi_vo));
			System.out.print(" daoCreate ");
		}else{
			novi_vo.append(dao.daoStore(novi_vo));
			System.out.print(" daoStore ");
		}
		
		novi_vo.set("radno_vrijeme_od",AS2Date.formatStringDateToAny(value.get("_old_radno_vrijeme_od"), "HH:mm:ss"));
		novi_vo.set("radno_vrijeme_do",AS2Date.formatStringDateToAny(value.get("_old_radno_vrijeme_do"), "HH:mm:ss"));
		return novi_vo;
	}
	
	public AS2Record brisiEvidenciju(AS2Record value)  {
		return value;
	}
	
	public AS2Record procitajEvidenciju(AS2Record value)  {
		return value;
	}
	
	public AS2RecordList procitajSveEvidencijePoOrgJed(AS2Record value)  {
		AS2RecordList novi_rs = new AS2RecordList();
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		//Multiple selection
		if(value.getProperty("id_spica_oj__0")!=null){
			int count=0;
			while(value.getProperty("id_spica_oj"+"__"+count)!=null){
				value.set("id_spica_oj", value.get("id_spica_oj"+"__"+count));
				if(count==0){
					novi_rs = dao.daoFindByOrgJed(value);
				}else{
					novi_rs.appendRowsOnly(dao.daoFindByOrgJed(value));
				}
				count++;
			}
		}else{
			novi_rs = dao.daoFindByOrgJed(value);
		}
		return novi_rs;
	}
	
	public AS2RecordList procitajSveEvidencije(AS2Record value)  {
		return new AS2RecordList();
	}
	//MJESECNA EVIDENCIJA
	public AS2RecordList procitajSveEvidencijeMjesecno(AS2Record value)  {
		AS2RecordList novi_rs = new AS2RecordList();
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		novi_rs = dao.daoFindMjecesno(value);
		return novi_rs;
	}
	//ŠIFRARNIK
	public AS2RecordList procitajSifrarnik(AS2Record value)  {
		PlaceSifrarnikJdbc dao = new PlaceSifrarnikJdbc();
		return dao.daoFindSifrarnik(value, false);
	}
	//POTVRDE
	public AS2RecordList dodajPotvrdu(AS2Record value)  {
		PlaceEvidencijaPotvrdaJdbc dao = new PlaceEvidencijaPotvrdaJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		
		value.set("korisnik", getUserName(value));//TODO korisnik
		value.set("datum",value.get("datum_od"));
		dao.daoCreate(value);
		return procitajSveEvidencijePoOrgJed(value);
	}
	public AS2RecordList procitajSvePotvrde(AS2Record value)  {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		return dao.daoFindPotvrde(value);
	}
//	public J2EEResultSet dodajPotvrduRacunovodstvo(J2EEValueObject value)  {
//		PlaceEvidencijaPotvrdaJdbc dao = new PlaceEvidencijaPotvrdaJdbc();
//		value.set("vrijeme_izmjene", J2EEDate.getCurrentDateTimeAsString());
//		value.set("korisnik", "admin");//TODO korisnik
//		value.set("datum",value.get("datum_od"));
//		
//		dao.daoCreate(value);
//		return procitajSveEvidencijePoOrgJed(value);
//	}
	//OVLASTI
	public AS2RecordList procitajSveOvlasti(AS2Record value)  {
		PlaceOvlastiJdbc dao = new PlaceOvlastiJdbc();
		return dao.daoFindOvlasti(value);
	}
	public AS2Record dodajOvlasti(AS2Record value)  {
		PlaceOvlastiJdbc dao = new PlaceOvlastiJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));//TODO korisnik
		return dao.daoLoadOvlasti(dao.daoCreate(value));
	}
	public AS2Record azurirajOvlasti(AS2Record value)  {
		PlaceOvlastiJdbc dao = new PlaceOvlastiJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));//TODO korisnik
		return dao.daoLoadOvlasti(dao.daoStore(value));
	}
	
	public AS2Record brisiOvlasti(AS2Record value)  {
		PlaceOvlastiJdbc dao = new PlaceOvlastiJdbc();
		dao.daoRemove(value);
		return value;
	}
	// GODIŠNJI ODMOR
	public AS2RecordList procitajSveGodisnjeOdmore(AS2Record value)  {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		return dao.daoFindGO(value);
	}
	public AS2RecordList procitajSveGodisnjeOdmoreRadnik(AS2Record value)  {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		return dao.daoFindGORadnik(value);
	}
	
	 //IZVJEŠTAJI
    public AS2RecordList izvjestaji(AS2Record value)  {
    	PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
        return dao.daoIzvjestaji(value);
    }
    //OBRADE
    public AS2RecordList pokreniObradu(AS2Record value)  {
    	PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
    	value.set("korisnik", getUserName(value));
        return dao.daoPokreniObradu(value);
    }
    //BUDUĆE RAZDOBLJE
	public AS2RecordList dodajViseEvidencija(AS2Record value) {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		value.set("korisnik", getUserName(value));
		return dao.daoAddBuduceEvidencije(value);
	}
    public AS2Record azurirajBuduceRazdoblje(AS2Record value) {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));
		return dao.daoStore(value);
	}
	
	public AS2RecordList procitajSveBuduceRazdoblje(AS2Record value) {
		PlaceEvidencijaJdbc dao = new PlaceEvidencijaJdbc();
		return dao.daoFindByRadnik(value);
	}

}
