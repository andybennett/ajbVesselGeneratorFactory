package ajb.random;

import java.util.Random;

/**
 * Created by JulienBe on 05/08/16.
 */
public class RandomInt extends Random {
    public static int anyRandomIntRange(int min, int max) {
        return min + new RandomInt().nextInt(max);
    }
}
