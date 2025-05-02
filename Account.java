package acm;

public interface Account {
    @Shard({"balance"})
    void deposit(int amount);

    @Shard({"balance"})
    void withdraw(int amount);

    @Shard({"history"})
    void printHistory();
}
