package compresion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {
    /** Compress a string to a list of output symbols. */
    public static List<Integer> Compresion(String uncompressed) {
        // Build the dictionary.
        int TamañoDiccionario = 256;

        int j=0;
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        Map<String, Integer> dictionaryP = new HashMap<String, Integer>();
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i);

        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> result2 = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
                // dictionaryP.put(wc, dictSize++);
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, TamañoDiccionario++);
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));

        int x=1;
        String res ="";
        Object vec[] = result.toArray();
        for(int i=0; i<result.size();i++){

            res=vec[i].toString();
            dictionaryP.put(res,x++);
        }

        for(int i=0; i<dictionaryP.size();i++){
            result2.add(dictionaryP.get(i));
        }

        return result;
    }


    public static String Descompresion(List<Integer> compressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)compressed.remove(0);
        StringBuffer result = new StringBuffer(w);
        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }
        return result.toString();
    }
}
