public class Network {

    public static void main(String[] args) {
	// write your code here
    }
}

class Semaphore {
    protected int value = 0 ;
    protected Semaphore() { value = 0 ; }
    protected Semaphore(int initial) { value = initial ; }
    public synchronized void P() {

        value-- ;
        if (value < 0)
            try { wait() ; } catch( InterruptedException e ) { }
    }
    public synchronized void V() {
        value++ ; if (value <= 0) notify() ;
    }
}

class Router{

}
class Device extends Thread{
    @Override
    public void run() {
        super.run();
    }
}
