package FTC_TP2.src;

import java.util.HashMap;
import java.util.Map;

public class TratamentoER {
    public static Map<Character, Integer> precedenceMap = new HashMap<>();

    static {
        precedenceMap.put('|', 1);
        precedenceMap.put('.', 2);
        precedenceMap.put('*', 3);
    }

    public static String leitura(String expressaoRegular) {
        return expressaoRegular.replaceAll("\\s", "");
    }
}
