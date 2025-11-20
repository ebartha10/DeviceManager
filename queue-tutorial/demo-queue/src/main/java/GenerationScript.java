import java.util.Random;

public class GenerationScript {

    private final Random random = new Random();

    // Generates a random value message
    public String generateMessage() {
        int value = random.nextInt(1000); // Random int 0–999
        long timestamp = System.currentTimeMillis();
        return String.format("RandomValue=%d, Timestamp=%d", value, timestamp);
    }
}
