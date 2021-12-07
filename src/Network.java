import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Network {

    private static ArrayList <Device> TC_lines;
    private static Router router;
    private static Scanner inputScanner = new Scanner(System.in);
    public static String stringBuffer;

    private static Device createDevice()
    {
        String input = inputScanner.nextLine();
        String[] in = input.split(" ");
        Device d = new Device(in[0],in[1], router);
        return d;
    }

    public static void main(String[] args) {
        Scanner input;

        //N max number of connections a router can hold
        int N,TC;
        System.out.println("What is the number of WI-FI connections ?");
        N = inputScanner.nextInt();
        router = new Router(N);

        // TC total number of devices that wish to connect
        System.out.println("What is the number of devices clients wants to connect ?");
        TC = inputScanner.nextInt();
        inputScanner.nextLine();
        TC_lines = new ArrayList<>(TC);

        for (int i =1; i<TC;i++ )
        {
            TC_lines.add(createDevice());
        }

        System.out.println(stringBuffer);

    }
}

class Semaphore {
    protected int value = 0 ;
    protected Semaphore() { value = 0 ; }
    protected Semaphore(int initial) { value = initial ; }
    public synchronized void waitf() {
        value-- ;
        if (value < 0)
            try {
                wait() ;
            }
        catch( InterruptedException e )
        {

        }
    }
    public synchronized void signal() {
        value++ ;
        if (value <= 0)
            notify() ;
    }
}

class Router {

    private ArrayList<Device> connectedDevices;

    private Semaphore deviecsSemaphore;

    Router (int size)
    {
        connectedDevices = new ArrayList<>(size);
        deviecsSemaphore = new Semaphore(size);
    }

        public void occupyConnection(Device device)
        {
            deviecsSemaphore.waitf();
            connectedDevices.add(device);
            Network.stringBuffer += "Connection " + connectedDevices.size() + ": " + device.name + "Occupied\n";
        }

        public void releaseConnection(Device device)
        {
            String r = device.logout();
            connectedDevices.remove(device);
            deviecsSemaphore.signal();
            Network.stringBuffer += "Connection " + connectedDevices.size() + ": " + r + "\n";
        }

}

class Device extends Thread {

    private Random rand = new Random();
    private Router router;

    @Override
    public synchronized void run() {
        super.run();
        try {
            wait(rand.nextInt(500));
            connect();
            wait(rand.nextInt(500));
            router.occupyConnection(this);
            wait(rand.nextInt(500));
            login();
            wait(rand.nextInt(500));
            perform_online_activity();
            wait(rand.nextInt(500));
            router.releaseConnection(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String name;
    String type;

    Device(String name, String type, Router router) {
        this.name = name;
        this.type = type;
        this.router = router;
        run();
    }

    public void connect()
    {
        Network.stringBuffer += "(" +  name + ") (" + type  + ") arrived\n";
    }

   public void login(){
        Network.stringBuffer += name + "login\n";

   }

   public void perform_online_activity(){
        Network.stringBuffer += name+ "performs online activity \n";
   }
   public String logout(){
        return name + "Logged out\n";
   }

}
