package hr.adriacomsoftware.app.server.etl.job;

import hr.as2.inf.common.types.AS2Date;

public class PentahoGlavniVecernjiETL implements Runnable {
    private static PentahoGlavniVecernjiETL _instance = null;
    
    public static PentahoGlavniVecernjiETL getInstance() {
        if (_instance == null)
            _instance = new PentahoGlavniVecernjiETL();
        return _instance;
    }
    private PentahoGlavniVecernjiETL() {
        try {
            Thread etl_thread = new Thread(this);
            etl_thread.setPriority(Thread.MIN_PRIORITY);
            etl_thread.start();
        } catch (Exception e) {
            System.out.println("PentahoGlavniVecernjiETL default constructor exception - "+ e);
        }
    }
    public void run() {
        Thread t = Thread.currentThread();
        long startTime=0, endTime=0;
        try {
	         System.out.println("PentahoGlavniVecernjiETL START thread " + t.getName() + " started at: "+AS2Date.getCurrentDateAsString());
	         startTime = System.currentTimeMillis(); 
	         //String[] parmametri = {"-rep", "ETL_PROD", "-user", "admin", "-job=JOB Glavni ETL - VECERNJI"}; 
             //J2EEKitchen.startJob(parmametri);
             System.out.println("PentahoGlavniVecernjiETL STARTED - JOB Glavni ETL - VECERNJI");
             endTime = System.currentTimeMillis();
             System.out.println("PentahoGlavniVecernjiETL thread " + t.getName() + " finished for: " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            e.printStackTrace();                
        }             
    }
    //TEST
    public static void main(String[] args) {
        PentahoGlavniVecernjiETL.getInstance();
    }
}