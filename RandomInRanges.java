package bullscows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInRanges {

    private final List<Integer> range = new ArrayList<>();

    final void addRange(int min, int max)
    {
        for(int i = min; i <= max; i++) {
                this.range.add(i);
        }
    }

    int getRandom()
    {
        System.out.println(range.size());
        return this.range.get(new Random().nextInt(this.range.size()));
    }

}
