import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Network {

    private static ArrayList<Device> TC_lines;
    private static Router router;
    private static Scanner inputScanner = new Scanner(System.in);
    public static String stringBuffer = "";

    private static Device createDevice() {
        String input = inputScanner.nextLine();
        String[] in = input.split(" ");
        Device d = new Device(in[0], in[1], router);
        return d;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //N max number of connections a router can hold
        int N, TC;
        System.out.println("What is the number of WI-FI connections ?");
        N = inputScanner.nextInt();
        router = new Router(N);

        // TC total number of devices that wish to connect
        System.out.println("What is the number of devices clients wants to connect ?");
        TC = inputScanner.nextInt();
        inputScanner.nextLine();
        TC_lines = new ArrayList<>(TC);

        for (int i = 0; i < TC; i++)
            TC_lines.add(createDevice());
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);

        for (int i = 0; i < TC; i++)
            TC_lines.get(i).start();

    }

}

class Semaphore {
    protected int value = 0;

    protected Semaphore() {
        value = 0;
    }

    protected Semaphore(int initial) {
        value = initial;
    }

    public synchronized void waitf(Device device) {
        value--;
        if (value < 0){
            System.out.println("(" + device.name + ") (" + device.type + ") arrived and waiting");
            try {
                wait();

            } catch (InterruptedException e) {

            }}else {
            System.out.println("(" + device.name + ") (" + device.type + ") arrived");
        }

    }

    public synchronized void signal() {
        value++;
        if (value <= 0)
            notify();
    }
}

class Router {
    private ArrayList<Device> connectedDevices;
    private Semaphore deviecsSemaphore;

    public ArrayList<Device> getConnectedDevices() {
        return connectedDevices;
    }

    public void setConnectedDevices(ArrayList<Device> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }


    public Semaphore getDeviecsSemaphore() {
        return deviecsSemaphore;
    }

    public void setDeviecsSemaphore(Semaphore deviecsSemaphore) {
        this.deviecsSemaphore = deviecsSemaphore;
    }


    Router(int size) {
        connectedDevices = new ArrayList<>(size);
        deviecsSemaphore = new Semaphore(size);
    }

    public void occupyConnection(Device device) {
        deviecsSemaphore.waitf(device);
        connectedDevices.add(device);
        System.out.println("Connection " + connectedDevices.size() + ": " + device.name + " Occupied");
    }

    public void releaseConnection(Device device) {
        int id=connectedDevices.indexOf(device);
        String r = device.logout();
        connectedDevices.remove(device);
        System.out.println("Connection " + (id + 1) + ": " + r);
        deviecsSemaphore.signal();
    }

}

class Device extends Thread {

    private Router router;

    @Override
    public void run() {
        super.run();
        //connect();
        router.occupyConnection(this);
        login(router);
        perform_online_activity();
        router.releaseConnection(this);
    }

    String name;
    String type;

    Device(String name, String type, Router router) {
        this.name = name;
        this.type = type;
        this.router = router;
    }

    /*public void connect() {
        System.out.println("(" + name + ") (" + type + ") arrived");
    }*/

    public void login(Router router) {
        System.out.println("Connection " + (router.getConnectedDevices().indexOf(this)+1) + ": " + name + " login");


    }

    public void perform_online_activity() {
        System.out.println("Connection " + (router.getConnectedDevices().indexOf(this)+1) + ": " + name + " performs online activity ");
    }

    public String logout() {
        return name + " Logged out";
    }

}
