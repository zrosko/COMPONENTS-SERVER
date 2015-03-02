package hr.adriacomsoftware.app.server.etl.job;

import hr.as2.inf.common.types.AS2Date;

public class PentahoGlavniETL implements Runnable {
    private static PentahoGlavniETL _instance = null;
    
    public static PentahoGlavniETL getInstance() {
        if (_instance == null)
            _instance = new PentahoGlavniETL();
        return _instance;
    }
    private PentahoGlavniETL() {
        try {
            Thread etl_thread = new Thread(this);
            etl_thread.setPriority(Thread.MIN_PRIORITY);
            etl_thread.start();
        } catch (Exception e) {
            System.out.println("PentahoGlavniETL default constructor exception - "+ e);
        }
    }
    public void run() {
        Thread t = Thread.currentThread();
        long startTime=0, endTime=0;
        try {
	         System.out.println("PentahoGlavniETL START thread " + t.getName() + " started at: "+AS2Date.getCurrentDateAsString());
	         startTime = System.currentTimeMillis(); 
	         //String[] parmametri = {"-rep", "ETL_PROD", "-user", "admin", "-job=JOB Glavni ETL"}; 
             //J2EEKitchen.startJob(parmametri);
             System.out.println("PentahoGlavniETL STARTED - JOB Glavni ETL");
             endTime = System.currentTimeMillis();
             System.out.println("PentahoGlavniETL thread " + t.getName() + " finished for: " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            e.printStackTrace();                
        }             
    }
    //TEST
    public static void main(String[] args) {
        PentahoGlavniETL.getInstance();
    }
}