package hr.adriacomsoftware.app.server.etl.facade;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.gk.da.as400.jdbc.GlavnaKnjigaAs400Jdbc;
import hr.adriacomsoftware.app.server.gk.da.jdbc.GlavnaKnjigaBsaJdbc;
import hr.adriacomsoftware.app.server.gk.da.jdbc.GlavnaKnjigaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.as400.jdbc.PrometAs400Jdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometKreditJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometMjesecniJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometStednjaDevizaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometStednjaKunaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometTekuciJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PrometZiroJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.annotations.AS2FacadeServer;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;

@AS2FacadeServer
public final class ETLFacadeServer extends AS2FacadeServerLayer {

	private static ETLFacadeServer _instance = null;
	public static ETLFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new ETLFacadeServer();
		}
		return _instance;
	}
	private ETLFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}

    public void jobGlavnaKnjigaETL(OsnovniVo value) {
        GlavnaKnjigaJdbc daoJdbc = new GlavnaKnjigaJdbc();
        StringBuffer sql = new StringBuffer();
        sql.append("IF EXISTS (SELECT * FROM sys.objects  WHERE object_id = OBJECT_ID(N'[dbo].[j2ee_etl]') AND type IN (N'U')) ");
        sql.append("SELECT * FROM  rbac_uloga " );
        if(daoJdbc.daoFindIfExists(sql.toString())){
            System.out.println("ETLJob @RUN izvodi se Pentaho ETL - nema Java ETL-a at: "+AS2Date.getCurrentDateAsString());
            return; //ako postoji slog u tabeli j2ee_etl to znači da je u tijeku ETL - Pentaho job
        }
        GlavnaKnjigaAs400Jdbc daoAs400Jdbc = new GlavnaKnjigaAs400Jdbc();
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //pronadi maksimalni broj stavke u BI bazi
        AS2RecordList bi_max_res = daoJdbc.daoExecuteSimpleSQL(GlavnaKnjigaJdbc.MAX_BROJ_STAVKE_SQL); 
        OsnovniVo bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        AS2RecordList bsa_stavke = daoAs400Jdbc.daoETLGlavnaKnjiga(bi_max);
        int max_brojac = 100; //ne radi vise od 100 puta tj. svaki put 100 u BSA (pstmt.setMaxRows(100))
        int brojac = 0;
        int nove_stavke = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            System.out.println("ETLJob @RUN ETLFacadeServer MAX broj stave u BI bazi = "+bi_max_res.get("max_broj_stavke"));
            System.out.println("ETLJob @RUN ETLFacadeServer SIZE (novi slogovi) u BSA bazi = "+bsa_stavke.size());
            daoJdbc.daoCreateMany(bsa_stavke);  
            nove_stavke = bsa_stavke.getRows().size();
            //pronadi maksimalni broj stavke u BI bazi
            bi_max_res = daoJdbc.daoExecuteSimpleSQL(GlavnaKnjigaJdbc.MAX_BROJ_STAVKE_SQL); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            System.out.println("ETLJob COMMIT");
            AS2Transaction.commit(); //COMMIT txn
            AS2Transaction.begin(); //BEGIN txn
            if(nove_stavke == 0)
	            break;
	        else {
	            nove_stavke = 0;
	        }
            bsa_stavke = daoAs400Jdbc.daoETLGlavnaKnjiga(bi_max);
         } 
        //TODO pripremi glavnu knjigu za vrstu knjizenja 333 tj. prebacivanje sa konta na konto
        //EXEC bi_po_krediti_rocnost_priprema
    }
    public void jobGlavnaKnjigaBsaETL(OsnovniVo value) {
        GlavnaKnjigaBsaJdbc daoJdbc = new GlavnaKnjigaBsaJdbc();
        StringBuffer sql = new StringBuffer();
        sql.append("IF EXISTS (SELECT * FROM sys.objects  WHERE object_id = OBJECT_ID(N'[dbo].[j2ee_etl]') AND type IN (N'U')) ");
        sql.append("SELECT * FROM  rbac_uloga " );
        if(daoJdbc.daoFindIfExists(sql.toString())){
            System.out.println("ETLJob @RUN izvodi se Pentaho ETL - nema Java ETL-a at: "+AS2Date.getCurrentDateAsString());
            return; //ako postoji slog u tabeli j2ee_etl to znači da je u tijeku ETL - Pentaho job
        }
        GlavnaKnjigaAs400Jdbc daoAs400Jdbc = new GlavnaKnjigaAs400Jdbc();
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //pronadi maksimalni broj stavke u BI bazi
        AS2RecordList bi_max_res = daoJdbc.daoExecuteSimpleSQL(GlavnaKnjigaBsaJdbc.MAX_BROJ_STAVKE_SQL); 
        OsnovniVo bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        AS2RecordList bsa_stavke = daoAs400Jdbc.daoETLGlavnaKnjiga(bi_max);
        int max_brojac = 1; //ne radi vise od 100 puta tj. svaki put 100 u BSA (pstmt.setMaxRows(100))
        int brojac = 0;
        int nove_stavke = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            System.out.println("ETLJob @RUN ETLFacadeServer bsa MAX broj stave u BI bazi = "+bi_max_res.get("max_broj_stavke"));
            System.out.println("ETLJob @RUN ETLFacadeServer bsa SIZE (novi slogovi) u BSA bazi = "+bsa_stavke.size());
            daoJdbc.daoCreateMany(bsa_stavke); 
            nove_stavke = bsa_stavke.getRows().size();
            //pronadi maksimalni broj stavke u BI bazi
            bi_max_res = daoJdbc.daoExecuteSimpleSQL(GlavnaKnjigaBsaJdbc.MAX_BROJ_STAVKE_SQL); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            System.out.println("ETLJob COMMIT");
            AS2Transaction.commit(); //COMMIT txn
            AS2Transaction.begin(); //BEGIN txn
            if(nove_stavke == 0)
	            break;
	        else {
	            nove_stavke = 0;
	        }
            bsa_stavke = daoAs400Jdbc.daoETLGlavnaKnjiga(bi_max);
        }     
        //TODO pripremi glavnu knjigu za vrstu knjizenja 333 tj. prebacivanje sa konta na konto
        //EXEC bi_po_krediti_rocnost_priprema
    }
    /* Prepis FINTRAN tabele.
     * Isti proces se radi u ETL (Spoon). Ako dodje do promjene baze onda je potrebno ažurirati ETL i ovu funkciju.
     * bi_gr_promet_kredit 49989517 - 50081509 
     * bi_gr_promet 49989614
     * bi_gr_promet_stednja_deviza 49989505
     * bi_gr_promet_ziro 50011261
     * bi_gr_promet_tekuci 50035595
     * bi_gr_promet_stednja_kuna 49989531
     */
    public void jobPrometETL(OsnovniVo value) {
        PrometAs400Jdbc daoAs400Jdbc = new PrometAs400Jdbc();
        PrometKreditJdbc dao_kredit = new PrometKreditJdbc();
        PrometStednjaDevizaJdbc dao_stednja_deviza = new PrometStednjaDevizaJdbc();
        PrometZiroJdbc dao_ziro = new PrometZiroJdbc();
        PrometTekuciJdbc dao_tekuci = new PrometTekuciJdbc();
        PrometJdbc dao_promet = new PrometJdbc();
        PrometStednjaKunaJdbc dao_stednja_kuna = new PrometStednjaKunaJdbc();
        //TODO stavoto sve i jednu petlju sa if brojac razlikovanjem
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_KREDITI_700000000_739999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet krediti pocetak");
        AS2RecordList bi_max_res = dao_kredit.daoExecuteSimpleSQL(PrometKreditJdbc.MAX_BROJ_STAVKE_SQL); 
        OsnovniVo bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        AS2RecordList bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_KREDITI_700000000_739999999);
        int max_brojac = 10; //ne radi vise od 10 puta tj. svaki put 5000 u BSA (pstmt.setMaxRows(100))
        int brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_kredit.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_kredit.daoExecuteSimpleSQL(PrometKreditJdbc.MAX_BROJ_STAVKE_SQL); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_KREDITI_700000000_739999999);
        }  
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_STEDNJA_DEVIZE_400000000_4499999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet devize 1 pocetak");
        bi_max_res = dao_stednja_deviza.daoExecuteSimpleSQL(PrometStednjaDevizaJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 4000000000 AND 4499999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_STEDNJA_DEVIZE_400000000_4499999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_stednja_deviza.daoCreateMany(bsa_stavke);
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_stednja_deviza.daoExecuteSimpleSQL(PrometStednjaDevizaJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 4000000000 AND 4499999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_STEDNJA_DEVIZE_400000000_4499999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_STEDNJA_DEVIZE_490000000_499999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet devize 2 pocetak");
        bi_max_res = dao_stednja_deviza.daoExecuteSimpleSQL(PrometStednjaDevizaJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 4900000000 AND 4999999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_STEDNJA_DEVIZE_490000000_499999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_stednja_deviza.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_stednja_deviza.daoExecuteSimpleSQL(PrometStednjaDevizaJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 4900000000 AND 4999999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_STEDNJA_DEVIZE_490000000_499999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_ZIRO_310000000_319999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet ziro 1 pocetak");
        bi_max_res = dao_ziro.daoExecuteSimpleSQL(PrometZiroJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 3100000000 AND 3199999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_ZIRO_310000000_319999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_ziro.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_ziro.daoExecuteSimpleSQL(PrometZiroJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 3100000000 AND 3199999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_ZIRO_310000000_319999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_TEKUCI_320000000_329999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet tekuci pocetak");
        bi_max_res = dao_tekuci.daoExecuteSimpleSQL(PrometTekuciJdbc.MAX_BROJ_STAVKE_SQL ); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_TEKUCI_320000000_329999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_tekuci.daoCreateMany(bsa_stavke);
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_tekuci.daoExecuteSimpleSQL(PrometTekuciJdbc.MAX_BROJ_STAVKE_SQL); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_TEKUCI_320000000_329999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_OSTALO_300000000 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet ostalo pocetak");
        bi_max_res = dao_promet.daoExecuteSimpleSQL(PrometJdbc.MAX_BROJ_STAVKE_SQL); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_OSTALO_300000000);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_promet.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_promet.daoExecuteSimpleSQL(PrometJdbc.MAX_BROJ_STAVKE_SQL); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_OSTALO_300000000);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_ZIRO_NEREZIDENTI_360000000_369999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet ziro 2 pocetak");
        bi_max_res = dao_ziro.daoExecuteSimpleSQL(PrometZiroJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 3600000000 AND 3699999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_ZIRO_NEREZIDENTI_360000000_369999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_ziro.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_ziro.daoExecuteSimpleSQL(PrometZiroJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 3600000000 AND 3699999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_ZIRO_NEREZIDENTI_360000000_369999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_STEDNJA_KUNE_300000000_309999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet stednja 1 pocetak");
        bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 3000000000 AND 3099999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_300000000_309999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_stednja_kuna.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 3000000000 AND 3099999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_300000000_309999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_STEDNJA_KUNE_330000000_349999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet stednja 2 pocetak");
        bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 3300000000 AND 3499999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_330000000_349999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_stednja_kuna.daoCreateMany(bsa_stavke);
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 3300000000 AND 3499999999"); 
            bi_max = new OsnovniVo();            
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_330000000_349999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //VRSTA_STEDNJA_KUNE_480000000_489999999 pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet stednja 3 pocetak");
        bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL +" WHERE broj_partije BETWEEN 4800000000 AND 4899999999"); 
        bi_max = new OsnovniVo();
        bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
        bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max, PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_480000000_489999999);
        brojac = 0;
        while(bsa_stavke.size()>0 && brojac <= max_brojac){
            brojac++;
            dao_stednja_kuna.daoCreateMany(bsa_stavke); 
            System.out.println("@@ ETL promet petlja: "+ brojac);
            bi_max_res = dao_stednja_kuna.daoExecuteSimpleSQL(PrometStednjaKunaJdbc.MAX_BROJ_STAVKE_SQL+" WHERE broj_partije BETWEEN 4800000000 AND 4899999999"); 
            bi_max = new OsnovniVo();             
            bi_max.set("max_broj_stavke",bi_max_res.get("max_broj_stavke"));
            bsa_stavke = daoAs400Jdbc.daoETLPromet(bi_max,PrometAs400Jdbc.VRSTA_STEDNJA_KUNE_480000000_489999999);
        }
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
    }
    public void jobPrometMjesecniETLStaro(OsnovniVo value)  {
        PrometAs400Jdbc daoAs400Jdbc = new PrometAs400Jdbc();
        PrometMjesecniJdbc dao = new PrometMjesecniJdbc();
        dao.daoBrisiStarePodatke();
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet mjesecni pocetak");
        AS2RecordList bi_max_res = dao.daoExecuteSimpleSQL(PrometMjesecniJdbc.MAX_BROJ_STAVKE_SQL); 
        AS2RecordList bsa_max_res = daoAs400Jdbc.daoExecuteSimpleSQL(PrometAs400Jdbc.MAX_BROJ_STAVKE_SQL);
        int bi_prod_broj_stavke = bi_max_res.getAsInt("max_broj_stavke");
        if(bi_prod_broj_stavke == 0)
        	bi_prod_broj_stavke=50314958;
        int bsa_broj_stavke = bsa_max_res.getAsInt("max_broj_stavke");
        if(bsa_broj_stavke==0)
        	bsa_broj_stavke = 50314958;
        int bi_prod_broj_stavke_A = 50314958;
        int bi_prod_broj_stavke_B = 50314958;
        int bi_prod_broj_stavke_C = 50314958;
        int nove_stavke = 0;
        while(bi_prod_broj_stavke < bsa_broj_stavke){
            bi_prod_broj_stavke_A = dao.daoMaxBrojStavke(PrometMjesecniJdbc.VRSTA_A);
	        AS2RecordList bsa_stavke = daoAs400Jdbc.daoETLPrometMjesecniSTARO(bi_prod_broj_stavke_A, PrometAs400Jdbc.VRSTA_A);
	        dao.daoCreateMany(bsa_stavke); 
	        nove_stavke = bsa_stavke.getRows().size();
	        AS2Transaction.commit(); //COMMIT txn
	        AS2Transaction.begin(); //BEGIN txn
	        bi_prod_broj_stavke_B = dao.daoMaxBrojStavke(PrometMjesecniJdbc.VRSTA_B);
	        bsa_stavke = daoAs400Jdbc.daoETLPrometMjesecniSTARO(bi_prod_broj_stavke_B, PrometAs400Jdbc.VRSTA_B);
	        dao.daoCreateMany(bsa_stavke); 
	        nove_stavke = nove_stavke+bsa_stavke.getRows().size();
	        AS2Transaction.commit(); //COMMIT txn
	        AS2Transaction.begin(); //BEGIN txn
	        bi_prod_broj_stavke_C = dao.daoMaxBrojStavke(PrometMjesecniJdbc.VRSTA_C);
	        bsa_stavke = daoAs400Jdbc.daoETLPrometMjesecniSTARO(bi_prod_broj_stavke_C, PrometAs400Jdbc.VRSTA_C);
	        dao.daoCreateMany(bsa_stavke); 
	        nove_stavke = nove_stavke+bsa_stavke.getRows().size();
	        AS2Transaction.commit(); //COMMIT txn
	        AS2Transaction.begin(); //BEGIN txn
	        bi_max_res = dao.daoExecuteSimpleSQL(PrometMjesecniJdbc.MAX_BROJ_STAVKE_SQL); 
	        bsa_max_res = daoAs400Jdbc.daoExecuteSimpleSQL(PrometAs400Jdbc.MAX_BROJ_STAVKE_SQL);
	        bi_prod_broj_stavke = bi_max_res.getAsInt("max_broj_stavke");
	        if(bi_prod_broj_stavke == 0)
	        	bi_prod_broj_stavke=50314958;
	        bsa_broj_stavke = bsa_max_res.getAsInt("max_broj_stavke");
	        if(bsa_broj_stavke==0)
	        	bsa_broj_stavke = 50314958;
	        bi_prod_broj_stavke = bi_prod_broj_stavke<bi_prod_broj_stavke_A ? bi_prod_broj_stavke:bi_prod_broj_stavke_A;
	        bi_prod_broj_stavke = bi_prod_broj_stavke<bi_prod_broj_stavke_B ? bi_prod_broj_stavke:bi_prod_broj_stavke_B;
	        bi_prod_broj_stavke = bi_prod_broj_stavke<bi_prod_broj_stavke_C ? bi_prod_broj_stavke:bi_prod_broj_stavke_C;
	        if(nove_stavke == 0)
	            break;
	        else
	            nove_stavke = 0;
        }
    }
    public void jobPrometMjesecniETL(OsnovniVo value) {
        PrometAs400Jdbc daoAs400Jdbc = new PrometAs400Jdbc();
        PrometMjesecniJdbc dao = new PrometMjesecniJdbc();
        dao.daoBrisiStarePodatke();
        AS2Transaction.commit(); //COMMIT txn
        AS2Transaction.begin(); //BEGIN txn
        //pronadi maksimalni broj stavke u BI bazi
        System.out.println("@@ ETL promet mjesecni pocetak");
        AS2RecordList bi_max_res = dao.daoExecuteSimpleSQL(PrometMjesecniJdbc.MAX_BROJ_STAVKE_SQL); 
        AS2RecordList bsa_max_res = daoAs400Jdbc.daoExecuteSimpleSQL(PrometAs400Jdbc.MAX_BROJ_STAVKE_SQL);
        int bi_prod_broj_stavke = bi_max_res.getAsInt("max_broj_stavke");
        if(bi_prod_broj_stavke == 0)
        	bi_prod_broj_stavke=50314958;
        int bsa_broj_stavke = bsa_max_res.getAsInt("max_broj_stavke");
        if(bsa_broj_stavke==0)
        	bsa_broj_stavke = 50314958;
        int nove_stavke = 0;
        while(bi_prod_broj_stavke < bsa_broj_stavke){
	        AS2RecordList bsa_stavke = daoAs400Jdbc.daoETLPrometMjesecni(bi_prod_broj_stavke);
	        dao.daoCreateMany(bsa_stavke); 
	        nove_stavke = bsa_stavke.getRows().size();
	        AS2Transaction.commit(); //COMMIT txn
	        AS2Transaction.begin(); //BEGIN txn	        
	        if(nove_stavke == 0)
	            break;
	        else {
	            nove_stavke = 0;
	            bi_prod_broj_stavke = dao.daoMaxBrojStavke(0);
	        }
        }
    }
 }
