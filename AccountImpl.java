package acm;

import java.util.List;
import java.util.ArrayList;

public class AccountImpl implements Account {
    private int balance = 0;
    private final List<String> history = new ArrayList<>();

    @Override
    public void deposit(int amount) {
        balance += amount;
        history.add("Deposited " + amount);
        System.out.printf("[%s] deposit %d, balance=%d%n",
            Thread.currentThread().getName(), amount, balance);
    }

    @Override
    public void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            history.add("Withdrew " + amount);
            System.out.printf("[%s] withdraw %d, balance=%d%n",
                Thread.currentThread().getName(), amount, balance);
        } else {
            System.out.printf("[%s] insufficient funds for %d%n",
                Thread.currentThread().getName(), amount);
        }
    }

    @Override
    public void printHistory() {
        System.out.printf("[%s] history: %s%n",
            Thread.currentThread().getName(), history);
    }
}
