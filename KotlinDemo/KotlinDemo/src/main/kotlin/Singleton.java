public class Singleton {
    private static Singleton instance;
    private Singleton() {}
    public synchronized static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    public void singletonTest() {
        System.out.println("singletonTest is called.");
    }
}
