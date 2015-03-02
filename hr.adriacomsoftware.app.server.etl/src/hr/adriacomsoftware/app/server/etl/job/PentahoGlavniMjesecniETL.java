package hr.adriacomsoftware.app.server.etl.job;

import hr.as2.inf.common.types.AS2Date;

public class PentahoGlavniMjesecniETL implements Runnable {
    private static PentahoGlavniMjesecniETL _instance = null;
    
    public static PentahoGlavniMjesecniETL getInstance() {
        if (_instance == null)
            _instance = new PentahoGlavniMjesecniETL();
        return _instance;
    }
    private PentahoGlavniMjesecniETL() {
        try {
            Thread etl_thread = new Thread(this);
            etl_thread.setPriority(Thread.MIN_PRIORITY);
            etl_thread.start();
        } catch (Exception e) {
            System.out.println("PentahoGlavniMjesecniETL default constructor exception - "+ e);
        }
    }
    public void run() {
        Thread t = Thread.currentThread();
        long startTime=0, endTime=0;
        try {
	         System.out.println("PentahoGlavniMjesecniETL START thread " + t.getName() + " started at: "+AS2Date.getCurrentDateAsString());
	         startTime = System.currentTimeMillis(); 
	         //String[] parmametri = {"-rep", "ETL_PROD", "-user", "admin", "-job=JOB Glavni ETL - MJESECNI"}; 
             //J2EEKitchen.startJob(parmametri);
             System.out.println("PentahoGlavniMjesecniETL STARTED - JOB Glavni ETL - MJESECNI");
             endTime = System.currentTimeMillis();
             System.out.println("PentahoGlavniMjesecniETL thread " + t.getName() + " finished for: " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            e.printStackTrace();                
        }             
    }
    //TEST
    public static void main(String[] args) {
        PentahoGlavniMjesecniETL.getInstance();
    }
}