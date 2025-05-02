package acm;

public class ACMDemo {
    public static void main(String[] args) {
        // Create real object and proxy
        Account realAccount = new AccountImpl();
        Account account = ProxyFactory.createProxy(realAccount);

        // Two threads: one depositing, one printing history
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                account.deposit(100 * i);
                sleep(50);
            }
        }, "Depositor");

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                account.printHistory();
                sleep(70);
            }
        }, "Historian");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {}
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
