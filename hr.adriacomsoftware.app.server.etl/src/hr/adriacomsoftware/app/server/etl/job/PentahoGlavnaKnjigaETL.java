package hr.adriacomsoftware.app.server.etl.job;

import hr.as2.inf.common.types.AS2Date;

public class PentahoGlavnaKnjigaETL implements Runnable {
    private static PentahoGlavnaKnjigaETL _instance = null;
    
    public static PentahoGlavnaKnjigaETL getInstance() {
        if (_instance == null)
            _instance = new PentahoGlavnaKnjigaETL();
        return _instance;
    }
    private PentahoGlavnaKnjigaETL() {
        try {
            Thread etl_thread = new Thread(this);
            etl_thread.setPriority(Thread.MIN_PRIORITY);
            etl_thread.start();
        } catch (Exception e) {
            System.out.println("PentahoGlavnaKnjigaETL default constructor exception - "+ e);
        }
    }

    public void run() {
        Thread t = Thread.currentThread();
        long startTime=0, endTime=0;
        try {
	         System.out.println("PentahoGlavnaKnjigaETL START thread " + t.getName() + " started at: "+AS2Date.getCurrentDateAsString());
	         startTime = System.currentTimeMillis(); 
	         //String[] parmametri = {"-rep", "ETL_PROD", "-user", "admin", "-job=JOB Pravne Osobe Glavna Knjiga - ONLINE"}; 
             //J2EEKitchen.startJob(parmametri);
             System.out.println("PentahoGlavnaKnjigaETL STARTED - JOB Pravne Osobe Glavna Knjiga - ONLINE");
             endTime = System.currentTimeMillis();
             System.out.println("PentahoGlavnaKnjigaETL thread " + t.getName() + " finished for: " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            e.printStackTrace();                
        }             
    }
    //TEST
    public static void main(String[] args) {
        PentahoGlavnaKnjigaETL.getInstance();
    }
}