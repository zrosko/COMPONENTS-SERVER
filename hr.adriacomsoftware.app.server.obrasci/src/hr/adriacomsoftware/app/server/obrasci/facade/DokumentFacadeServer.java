package hr.adriacomsoftware.app.server.obrasci.facade;

import hr.adriacomsoftware.app.server.obrasci.da.jdbc.DokumentJdbc;
import hr.adriacomsoftware.app.server.obrasci.da.jdbc.DokumentPoljeJdbc;
import hr.adriacomsoftware.app.server.obrasci.da.jdbc.DokumentSifrarnikJdbc;
import hr.adriacomsoftware.app.server.obrasci.da.jdbc.PoljeJdbc;
import hr.adriacomsoftware.app.server.obrasci.da.jdbc.VrstaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javaxt.io.File;
import javaxt.utils.Base64;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;


public final class DokumentFacadeServer extends AS2FacadeServerLayer {

	private static DokumentFacadeServer _instance = new DokumentFacadeServer();
	private static final String PARAMETER_EXTENSION = "_param";
	private static final String FILEUPLOAD_ID = "@@FileUpload_ID";

	public static DokumentFacadeServer getInstance() {
		return _instance;
	}

	private DokumentFacadeServer() {
		AS2Context.setSingletonReference(this);
	}
	
	public AS2Record dodajDokument(AS2Record value)  {
		DokumentJdbc dokumentDao = new DokumentJdbc();
		AS2Record res = dokumentDao.daoLoadDokument(pripremiDokument(value,value.get("dokument_veznik")));
		return res;
	}
	
	public AS2Record azurirajDokument(AS2Record value)  {
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
//		value.set("privitak", value.getAsBytes("privitak"));
		DokumentJdbc dao = new DokumentJdbc();
		AS2Record res = dao.daoLoadDokument(dao.daoStore(value));
		res.set(FILEUPLOAD_ID,value.get("id_dokumenta"));
		return res;
	}
	
	public AS2Record brisiDokument(AS2Record value)  {
		DokumentJdbc dokumentDao = new DokumentJdbc();
		int count=0;
		AS2Record res = new AS2Record();
		while(value.getProperty("object"+count)!=null && value.get("object"+count).length()>0){
			value.set("id_dokumenta", value.get("object"+count));
			res.append(dokumentDao.daoBrisiDokumente(value));
			count++;
		}
		return res;
	}
	
	public AS2Record procitajDokument(AS2Record value)  {
		DokumentJdbc dao = new DokumentJdbc();
		return dao.daoLoadDokument(value);
	}
	
	public AS2RecordList procitajSveDokumenteKorisnika(AS2Record value)  {
		DokumentJdbc dao = new DokumentJdbc();
		return dao.daoFindByKorisnik(value);
	}
	
	public AS2RecordList procitajSveDokumentePoVrsti(AS2Record value)  {
		DokumentJdbc dao = new DokumentJdbc();
		return dao.daoFindByVrsta(value);
	}

	public AS2RecordList procitajVrsteDokumenata(AS2Record value)  {
		VrstaJdbc dao = new VrstaJdbc();
		return dao.daoFind(new AS2Record());
	}

	public AS2Record procitajVrstuDokumentaPremaNazivu(AS2Record value)  {
		VrstaJdbc dao = new VrstaJdbc();
		return dao.daoFindVrstuDokumentaPremaNazivu(value);
	}

	public AS2RecordList procitajPoljaForme(AS2Record value)  {
		//Dohvaćanje ostalih podataka
		DokumentJdbc dao = new DokumentJdbc();
		return dao.daoFindPoljaForme(value);
	}
	
	public AS2Record pripremiDokument(AS2Record value, String veznik)  {
		int odmak = veznik.length();
		DokumentJdbc dokumentDao = new DokumentJdbc();
		value.set("datum_kreiranja",AS2Date.getCurrentDate());
		value.append(dokumentDao.daoCreate(value));
		DokumentPoljeJdbc dokPoljeDao = new DokumentPoljeJdbc();
		List<String> dokPoljeColumns = dokPoljeDao.daoFindColumnNames();
		for(String key: value.getProperties().keySet()){
			if(key.contains(veznik) && !veznik.equals("")){
				AS2Record temp = new AS2Record();
				for(String column:dokPoljeColumns){
					if(!column.equals("id_dokument_polja")){
						if(column.equals("redni_broj"))
							temp.set(column, key.substring(key.lastIndexOf(veznik)+odmak,key.length()));
						else if(column.equals("vrijednost"))
							temp.set("vrijednost", value.get(key));
						else if(!value.get(column+veznik+temp.get("redni_broj")).equals(""))
							temp.set(column, value.get(column));
						else if(!value.get(column).equals(""))
							temp.set(column, value.get(column));
					}
				}
				value.append(dokPoljeDao.daoCreate(temp));
			}
		}
		return value;
	}
	
	public AS2RecordList procitajSifrarnik(AS2Record value)	 {
		DokumentSifrarnikJdbc dao = new DokumentSifrarnikJdbc();
		return dao.daoFindSifrarnik(value, false);
	}

	/*************************PRIVITAK********************************/
	
	public AS2Record dodajPrivitak(AS2Record value)  {
		value.append(procitajVrstuDokumentaPremaNazivu(value));
		DokumentJdbc dokumentDao = new DokumentJdbc();
		value.set("datum_kreiranja",AS2Date.getCurrentDate());
		AS2Record res = dokumentDao.daoCreate(value);
		res.set(FILEUPLOAD_ID,res.get("id_dokumenta"));
		return res;
	}
	
	public AS2Record citajPrivitak(AS2Record value)  {
		DokumentJdbc dao = new DokumentJdbc();
		value.append(dao.daoLoad(value));
		VrstaJdbc daoVrsta = new VrstaJdbc();
		value.append(daoVrsta.daoLoad(value));
		value.set("naziv_dokumenta", value.get("naziv"));
		return value;
	}
	
	
	/*********************************************OBRASCI **************************************************/
	public AS2RecordList procitajDropDownPolja(AS2Record value) {
		PoljeJdbc dao = new PoljeJdbc();
		return dao.daoFindDropDownPolja(value);
	}
	

	public AS2Record dodajFormu(AS2Record from)  {
		prepareHiddenParametersForDB(from);
		from.append(pripremiDokument(from,"-"));
        return from;
    }
//	
//	//Pretvoriti u brisiDokument
//	public AS2Record brisiFormu(AS2Record from) throws J2EEException {
//		DokumentJdbc dokumentDao = new DokumentJdbc();
//		int count=0;
//		AS2Record vo = new AS2Record();
//		while(from.getProperty("object"+count)!=null && from.get("object"+count).length()>0){
//			from.set("id_dokumenta", from.get("object"+count));
//			vo.append(dokumentDao.daoBrisiDokumente(from));
//			count++;
//		}
//		return vo;
//    }
	
	
	public AS2Record prepareHiddenParametersForDB(AS2Record value) {
//		List<String> to_delete = new ArrayList<String>();
		Iterator<?> I = value.iteratorKeys();
		while(I.hasNext()){
			String key = I.next().toString();
			if(key.contains("_param") && !value.get(key).equals("")){
				value.set(key.substring(0,key.lastIndexOf("_param")), value.get(key));
			}
			if(value.get(key).equals("")){
				value.delete(key);
			}
		}
		return value;
	}

	public AS2Record prikaziMojiObrasciFormu(AS2Record value) throws Exception {
		AS2Record res = new AS2Record();
		String filepath = value.get("lokacija") + "/"
				+ value.get("naziv") + ".pdf";
		if (value.getProperty("verzija")!=null && !value.get("verzija").equals(""))
			filepath = value.get("lokacija") + "/"
					+ value.get("naziv")+"_"+value.get("verzija") + ".pdf";
		File file = new File(filepath);
		if (!file.exists()) {
				filepath = value.get("lokacija") + "/arhiva/"+ value.get("naziv") + value.get("verzija")+ ".pdf";
		}
		file = new File(filepath);
		if (file.exists()) {
			// Reading pdf
			InputStream is = file.getInputStream();	
			PdfReader reader = new PdfReader(is, null);
//					reader.removeUsageRights();	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(reader, baos);
			//PdfStamper stamper =new PdfStamper(reader, baos, '\0', true);
			// Getting fields from pdf
			AcroFields afields = stamper.getAcroFields();
			// Dohvaćanje punog imena polja
			// HashMap fields = (HashMap) afields.getFields();
			// Set keys = fields.keySet(); 

			// Dohvaćanje botuna sa forme i mjenjanje svojstva istom
			PushbuttonField btn_submit = afields.getNewPushbuttonFromField("btn_submit");
			// btn_submit.setText("POST");
			// btn_submit.setBackgroundColor(new GrayColor(0.7f));
			// btn_submit.setVisibility(0);
			btn_submit.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			btn_submit.getField().setAction(PdfAction.createSubmitForm(
					"/book/request", null,
					PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
			afields.replacePushbuttonField("btn_submit", btn_submit.getField());
			//add the button
			//stamper.addAnnotation(submit1, 1);
			// Getting data from res
			//value.set("naziv", value.get("name"));
			//Postavljanje osnovnih paramatera, podatak i liste vrijednosti na polje forme
			postaviOsnovnePodatkeNaPoljaNaFormi(afields,value);
			//Popunjavanje polja forme
			popuniFormu(afields,value);
			// Extra customization -- If we want to update list option we need to get parameter
			//by his "real" name not only by the key name
			//Set<String> parameters = afields.getFields().keySet();
			//for (String parameter : parameters) {
			//if(parameter.contains("direkcija")){
			//	afields.setListOption(parameter, new String[] {
			//			"0", "1", "2" }, new String[] { "a", "b", "c" });
			//	}
			//}
						///stamper.setFormFlattening(true);  - TODO OVO JE ZGODNO KOD PRIKAZIVANJA FORME! Međutim imamo problema kod prikaza datuma
//			 Closing stamper
			stamper.close();
			reader.close();
			is.close();
			byte[] fileByteArray = new byte[0];
			fileByteArray = baos.toByteArray();
			baos.close();
			String encodedFile = Base64.encodeBytes(fileByteArray);
			res.set("fileByteArray", encodedFile);
			res.set("contentType", "application/pdf");
			res.set("encoded", true);
		}else
			res.set("error","Pogreška kod dohvaćanja form. Razlog:\n "+ res);
		return res;
	}
	
	
	private void popuniFormu(AcroFields afields,AS2Record value)  throws Exception {
		AS2RecordList j2eers = procitajPoljaForme(value);
		Iterator<AS2Record> rows = j2eers.iteratorRows();
		while (rows.hasNext()) {
			AS2Record row = (AS2Record) rows.next();
			// Writing data to pdf form
			afields.setField(row.get("naziv_pdf"),row.get("vrijednost"));
		}
	}

	public AS2Record prikaziPdfFormu(AS2Record value)  throws Exception {
		AS2Record res = new AS2Record();
		File file = new javaxt.io.File(value.get("rootUrl") + "/"+ value.get("filePath"));
		if (file.exists()) {
			InputStream is = file.getInputStream();
			PdfReader reader = new PdfReader(is, null);
			//reader.removeUsageRights();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			Date date= new Date();
//			File temp = new File(file.getPath(),new StringBuilder().append("prefix")
//					.append(date.getTime()).append(UUID.randomUUID())
//					.append(".").append(file.getExtension()).toString());
			PdfStamper stamper = new PdfStamper(reader, baos);
			 AcroFields afields = stamper.getAcroFields();
//			try {
//				readFieldnames(file.getPath()+file.getName(),"C:/IT/astrikom/PDF/"+getFileNameWithoutExtension(file)+"FN.txt");
//				readXfa(file.getPath()+file.getName(),"C:/IT/astrikom/PDF/"+getFileNameWithoutExtension(file)+".txt");
//				readData(file.getPath()+file.getName(),"C:/IT/astrikom/PDF/"+getFileNameWithoutExtension(file)+"DATA.txt");
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			} catch (SAXException e) {
//				e.printStackTrace();
//			} catch (TransformerFactoryConfigurationError e) {
//				e.printStackTrace();
//			} catch (TransformerException e) {
//				e.printStackTrace();
//			}
			 if(value.get("naziv").equals("") && !value.get("name").equals(""))
				 value.set("naziv", value.get("name"));
			//Postavljanje osnovnih paramatera, podatak i liste vrijednosti na polje forme
			postaviOsnovnePodatkeNaPoljaNaFormi(afields,value);
//			stamper.setFormFlattening(true);
			stamper.close();
			reader.close();
			is.close();
			byte[] fileByteArray = new byte[0];
			fileByteArray = baos.toByteArray();
			String encodedFile = Base64.encodeBytes(fileByteArray);
			res.set("fileByteArray", encodedFile);
			res.set("contentType", "application/pdf");
			res.set("encoded", true);
		}else
			res.set("error","Pogreška kod dohvaćanja form. Razlog:\n "+ res);
		return res;
	}

	public LinkedHashMap<String, String[]> procitajDropDownPoljaForme(AS2Record value)  {
		AS2RecordList temp = new AS2RecordList();
		LinkedHashMap<String,String[]> lista_vrijednosti = new LinkedHashMap<String,String[]>();
		// Dohvaćanje drop down polja (Combo box)
		AS2RecordList polja = procitajDropDownPolja(value);
		PoljeJdbc dao = new PoljeJdbc();
		Iterator<AS2Record> iterator1 = polja.iteratorRows();
		while (iterator1.hasNext()){
			AS2Record polje = iterator1.next();
			if(polje.get("izvor_podataka_vrsta").equalsIgnoreCase("SQL")){
					AS2RecordList vrijednosti = dao.daoExecuteQuery(polje.get("izvor_podataka"));
					Iterator<AS2Record> F = vrijednosti.iteratorRows();
					while (F.hasNext()){
						AS2Record vrijednost = F.next();
						vrijednost.append(polje);
					}
						temp.addRows(vrijednosti);					
			}else if(polje.get("izvor_podataka_vrsta").equalsIgnoreCase("DATA")){
					lista_vrijednosti.put(polje.get("naziv_polja"),polje.get("izvor_podataka").split(";"));
			}
		}
		//Dohvaćanje liste vrijednosti (List box) za drop down polja iz Result seta
		Iterator<AS2Record> iterator2 = temp.iteratorRows();	
		while (iterator2.hasNext()){
			AS2Record polje = iterator2.next();
			AS2RecordList list_rs = temp.doSearch("naziv_polja", "=", polje.get("naziv_polja"));
			List<String> vrijednosti_polja = new ArrayList<String>();
			String naziv_polja="";
			Iterator<AS2Record> C = list_rs.iteratorRows();
			while (C.hasNext()){
				AS2Record vrijednost = C.next();
				naziv_polja =  vrijednost.get("naziv_polja");
				vrijednosti_polja.add(vrijednost.get("vrijednost"));
			}
			lista_vrijednosti.put(naziv_polja,  vrijednosti_polja.toArray(new String[vrijednosti_polja.size()]));
		}
		return lista_vrijednosti;

	}

	public void postaviOsnovnePodatkeNaPoljaNaFormi(AcroFields afields, AS2Record value)   throws Exception {
		//Dohvaćanje Dropdown polja (Hidden fields)
		LinkedHashMap<String,String[]> lista_vrijednosti = procitajDropDownPoljaForme(value);
		//Dohvaćanje osnovnih parametara forme (Hidden fields)
		value.append(procitajVrstuDokumentaPremaNazivu(value));
		Set<String> polja_na_formi = afields.getFields().keySet();
		for (String polje_na_formi : polja_na_formi) {
			//Osnovni parametri forme imaju sufiks _param, a ostali imaju sufiks - zajedno sa rednim brojem polja
			if(polje_na_formi.contains(PARAMETER_EXTENSION)){
				String key = polje_na_formi.substring(polje_na_formi.lastIndexOf(".")+1,polje_na_formi.lastIndexOf(PARAMETER_EXTENSION));
				if(value.getProperty(key)!=null)
					afields.setField(polje_na_formi, value.get(key));
			}
			else{
				if(lista_vrijednosti!=null && lista_vrijednosti.size()!=0){
					for(String naziv_polja: lista_vrijednosti.keySet()){
						if(polje_na_formi.contains(naziv_polja)){
							afields.setListOption(polje_na_formi, lista_vrijednosti.get(naziv_polja), lista_vrijednosti.get(naziv_polja));
						}
					}
				}
			}
		}
	}
	/*************************** URA ******************************/
//	public AS2RecordList procitajSveDokumenteKorisnika(AS2Record value)  {
//		DokumentJdbc dao = new DokumentJdbc();
//		return dao.daoFindByKorisnik(value);
//	}
	
//	public AS2Record dodajUlazniRacun(AS2Record value)  {
//		value.append(procitajVrstuDokumentaPremaNazivu(value));
//		DokumentJdbc dokumentDao = new DokumentJdbc();
////		value.setAmountParsed("iznos", value.get("iznos"));
//		value.set("datum_kreiranja",J2EEDate.getCurrentDate());
//		value.append(dokumentDao.daoCreate(value));
//		return value;
//	}
////	   
//	   public AS2Record dodajPrivitak(AS2Record value) throws J2EEException {
//	        DokumentJdbc dao = new DokumentJdbc();
//	        dao.daoCreate(value);
//	        return value;
//	    }
	
	
		//DISPLAYS FORM FROM FILE
//	private JSONObject  serviceShowFileByteArray(AS2Record facade_request,
//			HttpServletRequest req, HttpServletResponse resp) throws JSONException {
//		JSONObject res_data = new JSONObject();
//		JSONArray recordsArray = new JSONArray();
//		JSONObject record = new JSONObject();	
//		JSONObject object = new JSONObject();	
////		if (facade_request.get("fileName").contains("Zapisnik")) {
////			facade_request.set("url", getZapisnikFolderPath(facade_request)
////					+ "\\" + facade_request.get("sjednicaFileName"));
////		}
////		if (facade_request.get("fileName").contains("Dnevni")) {
////			facade_request.set("url", getDnevniRedFolderPath(facade_request)
////					+ "\\" + facade_request.get("sjednicaFileName"));
////		}
////		File file = new javaxt.io.File(facade_request.get("url"));
//		 File file = new javaxt.io.File(facade_request.get("rootUrl")+"/"+facade_request.get("filePath"));
//		if (file.exists()) {
//			byte[] fileByteArray = new byte[0];
//			fileByteArray=file.getBytes().toByteArray();
//			String encodedFile = Base64.encodeBytes(fileByteArray);
//			object.put("contentType","application/pdf");
//			object.put("fileByteArray", encodedFile);
//			record.put("record",object);
//			recordsArray.put(record);
//			res_data.put("data", recordsArray);
//			res_data.put("status", "0");
//		 }else {
//			 res_data=getErrorResponseObject("Greška na serveru. Razlog:\n Tražena datoteka ne postoji","-1");
//		 }
//		return res_data;
//	}
}
