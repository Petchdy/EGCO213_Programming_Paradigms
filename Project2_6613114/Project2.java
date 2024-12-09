//Kulwisit Sakkittiphokhin 6613114
package Project2_6613114;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;

////////////////////////////////////////////////////////////////////////////////

// 3 types of threads: 1.main 2.SellerThread 3.DeliveryThread

class Fleet {
    
    private String ftype;
    private int MaxNumber;
    private int MaxLoad;
    private int Remain;
    
    public Fleet (String type, int n, int m) { ftype = type; MaxNumber = n; MaxLoad = m; Remain = MaxNumber; }
    
    public void Reset () { Remain = MaxNumber; } ;
    public synchronized int VehAllo(int n) {
        
        int leftParcel = n;
        // we can only send out if there is still fleet remain
        if (Remain > 0) {
            int out = Math.min(n/MaxLoad, Remain);
            leftParcel = n - (out * MaxLoad);
            Remain -= out;
            // in case there are leftover and there are still fleet left
            if (Remain > 0 && leftParcel > MaxLoad/2) {
                leftParcel = 0;
                out++;
                Remain--;
            }
            System.out.printf("%16s  >>  deliver %3d parcels by %2d %-6s     remaining parcels = %3d, remaining %-6s = %2d\n", 
                          Thread.currentThread().getName(), n - leftParcel, out, ftype, leftParcel, ftype, Remain);
        }
        else { // no fleet left
            System.out.printf("%16s  >>  deliver %3d parcels by %2d %-6s     remaining parcels = %3d, remaining %-6s = %2d\n", 
                          Thread.currentThread().getName(), 0, 0, ftype, n, ftype, Remain);
        }
        
        return leftParcel;
        
    }

}

class SellerThread extends Thread {
    
    private int days;
    private int SellerMaxDrop;
    private ArrayList<DeliveryShop> ds;
    private CyclicBarrier mainBarrier, sellerBarrier;
    
    public SellerThread (String name, int days, int md, ArrayList<DeliveryShop> ds, CyclicBarrier mb, CyclicBarrier sb) { 
        super(name); 
        this.days = days; 
        this.SellerMaxDrop = md; 
        this.ds = new ArrayList<>(ds);
        this.mainBarrier = mb; 
        this.sellerBarrier = sb; 
    }
    
    public int randomParcels() {
        int temp = (int)(1 + Math.random()*SellerMaxDrop);
        return temp;
    }
    
    public int randomShop() {
        int temp = (int)(Math.random()*(ds.size()));
        return temp;
    }
    
    public void printSeller(int parcelsAdd,int sendTo) {
        System.out.printf("%16s  >>  drop %3d parcels at %15s shop\n",Thread.currentThread().getName(), parcelsAdd, ds.get(sendTo).getShopName());
    }
    
    public void randomSendParcel() {
        // random number of parcel
        int parcelsAdd = randomParcels();
        // random shop to send to
        int sendTo = randomShop();
        // add parcel to deliveryshop
        ds.get(sendTo).addParcels(parcelsAdd);
        // print where seller send to + how many
        printSeller(parcelsAdd, sendTo);
    }
    
    @Override
    public void run() { 
        
        try {
            for (int i = 0; i < days; i++) {
                // wait for main to print day first
                mainBarrier.await();
                // random number of parcel and shop to send to
                // print where seller send to + how many
                randomSendParcel();
                // wait for all the seller thread to finish before go on
                sellerBarrier.await();
            }
        }
        catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("e");
        }
        
    }
}

class DeliveryShop implements Comparable<DeliveryShop> {
    
    private String shopName;
    private int parcelBalance;
    private int totalReceived = 0;
    private int totalDelivered = 0;
    
    public DeliveryShop (String st) { shopName = st; parcelBalance = 0; }
    
    public String getShopName () { return shopName; }
    public int getParcelBalance () { return parcelBalance; }
    
    public synchronized void addParcels (int n) { parcelBalance += n; totalReceived += n; }
    public synchronized void remainingParcels (int n) { parcelBalance = n; }
    public float successCal() { return (float)(totalReceived - parcelBalance)/totalReceived; }
    public void summary () {
        System.out.printf("%16s  >>  %-15s    received = %4d, delivered = %4d, success rate = %.2f\n", Thread.currentThread().getName(), getShopName(), totalReceived, totalReceived - parcelBalance, successCal() );
    }
    
    @Override
    public int compareTo(DeliveryShop other){
        if (this.successCal() > other.successCal()) {
            return -1;
        }
        else if (this.successCal() < other.successCal()) {
            return 1;
        }
        else {
            return (this.shopName).compareTo(other.shopName);
        }
    }
    
}

class DeliveryThread extends Thread {
    
    private int days;
    private Fleet ft;
    private DeliveryShop ds;

    private CyclicBarrier sellerBarrier, deliveryBarrier, betweenDeliveryBarrier;
    
    public DeliveryThread (String name, int simuday, Fleet ft, DeliveryShop ds, CyclicBarrier sb, CyclicBarrier db, CyclicBarrier bdb) { 
        super(name); 
        this.days = simuday;
        this.ft = ft;
        this.ds = ds; 
        this.sellerBarrier = sb; 
        this.deliveryBarrier = db; 
        this.betweenDeliveryBarrier = bdb;
    }
    
    public void printParcelToDeliver() {
        System.out.printf("%16s  >>      parcels to deliver = %3d\n", Thread.currentThread().getName(), ds.getParcelBalance());
    }
    
    @Override
    public void run() {
    
        try {
            for (int i = 0; i < days; i++) {
                sellerBarrier.await();
                // print number of parcel to deliver
                printParcelToDeliver();
                // wait, finish printParcelToDeliver first
                betweenDeliveryBarrier.await();
                // send number of current parcel to vehallo + set left parcel
                ds.remainingParcels(ft.VehAllo(ds.getParcelBalance()));
                // wait for all the delivery thread to finish before go on
                deliveryBarrier.await();
            }
        }
        catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("e");
        }
        
    }
}

public class Project2 {

    int Days;
    int BikeNum, BikeMaxLoad;
    int TruckNum, TruckMaxLoad;
    int SellerNum, MaxParcelDrop;
    int DeliveryByBike, DeliveryByTruck;
    
    public static void main(String[] args) {
        
        Project2 Main = new Project2();
        
        // Read file
        Main.ReadFile();
        
        // Create barrier
        CyclicBarrier dayBarrier = new CyclicBarrier(1);
        CyclicBarrier mainBarrier = new CyclicBarrier(Main.SellerNum + 1);
        CyclicBarrier sellerBarrier = new CyclicBarrier(Main.DeliveryByBike + Main.DeliveryByTruck + Main.SellerNum);
        CyclicBarrier deliveryBarrier = new CyclicBarrier(Main.DeliveryByBike + Main.DeliveryByTruck + 1);
        CyclicBarrier betweenDeliveryBarrier = new CyclicBarrier(Main.DeliveryByBike + Main.DeliveryByTruck);
        
        // Fleet (bike + truck)
        Fleet BikeFleet = new Fleet("bikes", Main.BikeNum, Main.BikeMaxLoad);
        Fleet TruckFleet = new Fleet("trucks", Main.TruckNum, Main.TruckMaxLoad);
        
        // DeliveryShop + DeliveryThread
        ArrayList<DeliveryShop> deliveryShops = new ArrayList<>();
        ArrayList<DeliveryThread> deliverythreads = new ArrayList<>();
        for (int i = 0; i < Main.DeliveryByBike; i++) {
            DeliveryShop deliveryshop = new DeliveryShop("BikeDelivery_" + i);
            deliveryShops.add( deliveryshop );
            deliverythreads.add( new DeliveryThread("BikeDelivery_" + i, Main.Days, BikeFleet, deliveryshop, sellerBarrier, deliveryBarrier, betweenDeliveryBarrier) );
        }
        for (int i = 0; i < Main.DeliveryByTruck; i++) {
            DeliveryShop deliveryshop = new DeliveryShop("TruckDelivery_" + i);
            deliveryShops.add( deliveryshop );
            deliverythreads.add( new DeliveryThread("TruckDelivery_" + i, Main.Days, TruckFleet, deliveryshop, sellerBarrier, deliveryBarrier, betweenDeliveryBarrier) );
        }
        
        // SellerThread
        ArrayList<SellerThread> sellerthreads = new ArrayList<>();
        for (int i = 0; i < Main.SellerNum; i++) {
            sellerthreads.add(new SellerThread("Seller_" + i, Main.Days, Main.MaxParcelDrop, deliveryShops, mainBarrier, sellerBarrier) );
        }
        
        // Paremeters
        Main.StartSimu();
        
        // start thread
        for (SellerThread st : sellerthreads) { st.start(); }
        for (DeliveryThread dt : deliverythreads) { dt.start(); }
        
        // start working
        for (int i = 0; i < Main.Days; i++) {
            
            try {
                // start day + reset
                Main.DayStart(i + 1);
                BikeFleet.Reset();
                TruckFleet.Reset();
                dayBarrier.await();
                // seller
                mainBarrier.await();
                // delivery
                deliveryBarrier.await();
            }
            catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("e");
            }
        }
        
        // join thread
        try {
            for (SellerThread st : sellerthreads) { st.join(); }
            for (DeliveryThread dt : deliverythreads) { dt.join(); }
        }
        catch (InterruptedException e) { System.out.println("error"); }
        
        // Summary
        Main.summary(deliveryShops);
        
    }
    
    public void ReadFile() {
        
        Scanner sc = new Scanner(System.in);
        String path = "src/main/java/project2_6613114/";
        // correct file name
        // String file = "config_1.txt";
        // wrong file name
        String file = "config.txt";
        String line;
        
        while (true) {   
            try (Scanner FileScan = new Scanner(new File(path + file)) ) {
                
                // Set total days
                line = FileScan.nextLine(); 
                String cols [] = line.split(",");
                Days = Integer.parseInt(cols[1].trim());
                
                // Setup Fleet
                // bike
                line = FileScan.nextLine(); 
                cols = line.split(",");
                BikeNum = Integer.parseInt(cols[1].trim());
                BikeMaxLoad = Integer.parseInt(cols[2].trim());
                
                // truck
                line = FileScan.nextLine(); 
                cols = line.split(",");
                TruckNum = Integer.parseInt(cols[1].trim());
                TruckMaxLoad = Integer.parseInt(cols[2].trim());
                
                // Setpu SellerThread
                line = FileScan.nextLine();
                cols = line.split(",");
                SellerNum = Integer.parseInt(cols[1].trim());
                MaxParcelDrop = Integer.parseInt(cols[2].trim());
                
                // Setup delivery thread
                line = FileScan.nextLine();
                cols = line.split(",");
                DeliveryByBike = Integer.parseInt(cols[1].trim());
                DeliveryByTruck = Integer.parseInt(cols[2].trim());
                
                break;
            }
            catch (FileNotFoundException e) {
                System.out.println(e + " " + path + file + "(The system cannot find the file specified)");
                System.out.println("New file name =");
                file = sc.next();
            }
            
        }
    }
    
    public void StartSimu() {
        System.out.printf("%16s  >>  %s Parameters %s\n", Thread.currentThread().getName(), "=".repeat(20), "=".repeat(20));
        System.out.printf("%16s  >>  days of simulation = %d\n", Thread.currentThread().getName(), Days);
        System.out.printf("%16s  >>  Bike  Fleet, total bikes  = %3d, max load = %3d parcels, min load = %3d parcels\n", Thread.currentThread().getName(), BikeNum, BikeMaxLoad, BikeMaxLoad/2);
        System.out.printf("%16s  >>  Truck Fleet, total trucks = %3d, max load = %3d parcels, min load = %3d parcels\n", Thread.currentThread().getName(), TruckNum, TruckMaxLoad, TruckMaxLoad/2);
        System.out.printf("%16s  >>  SellerThreads    = [", Thread.currentThread().getName());
        plist(SellerNum, "Seller_");
        System.out.printf("]\n");
        System.out.printf("%16s  >>  max parcel drop  = %d\n", Thread.currentThread().getName(), MaxParcelDrop);
        System.out.printf("%16s  >>  DeliveryThreads  = [", Thread.currentThread().getName());
        plist(DeliveryByBike, "BikeDelivery_");
        System.out.printf(", ");
        plist(DeliveryByTruck, "TruckDelivery_");
        System.out.printf("]\n");
    }
    
    public void DayStart(int n) {
        System.out.printf("%16s  >>  \n", Thread.currentThread().getName());
        System.out.printf("%16s  >>  %s\n", Thread.currentThread().getName(), "=".repeat(52));
        System.out.printf("%16s  >>  Day %d\n", Thread.currentThread().getName(), n);
    }
    
    public void plist(int n, String format) {
        for (int i = 0; i < n; i++) {
            System.out.printf("%s", format + i);
            if (i != n-1) {
                System.out.printf(", ");
            }
        }
    }
 
    public void summary(ArrayList<DeliveryShop> deliveryShops) {
        System.out.printf("%16s  >>  \n", Thread.currentThread().getName());
        System.out.printf("%16s  >>  %s\n", Thread.currentThread().getName(), "=".repeat(52));
        System.out.printf("%16s  >>  Summary\n", Thread.currentThread().getName());
        Collections.sort(deliveryShops);
        for (DeliveryShop ds : deliveryShops) {
            ds.summary();
        } 
    }
}