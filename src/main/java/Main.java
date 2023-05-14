public class Main {
    public static void main(String[] args) {
         class Counter {

            int count = 0;

            public synchronized void inc() {
                count++;
            }
        }
    }
}
