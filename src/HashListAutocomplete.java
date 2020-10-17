import java.util.*;

/**
 *
 * @author alyssa zhao
 */

public class HashListAutocomplete implements Autocompletor {
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap = new HashMap<>();
    private int mySize;


    /**
     * Calls initialize to create myMap with keys of terms
     * and values of weights
     * @param terms words to create terms from
     * @param weights weights of each entry of terms[]
     */
    public HashListAutocomplete (String[] terms, double[] weights){
        if( terms == null || weights == null){
            throw new NullPointerException("One or more arguments null");
        }
        initialize(terms, weights);
    }

    /**
     *
     * @param prefix prefixes up to MAX_PREFIX or length of word
     * @param k number of matches desired
     * @return an ArrayList of sorted Terms that match the desired prefix in myMap
     */
    @Override
    public List<Term> topMatches(String prefix, int k) {
        if(k == 0) return new ArrayList<>();

        if(myMap.containsKey(prefix.substring(0,Math.min(prefix.length(), MAX_PREFIX)))) {
            List<Term> all = myMap.get(prefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
            return list;
        }

        return new ArrayList<>();
    }

    /**
     * Create internal state needed to store Term objects
     * from the parameters. Should be called in implementing
     * constructors
     *
     * @param terms   is array of Strings for words in each Term
     * @param weights is corresponding weight for word in terms
     */
    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap.clear();

        for(int i = 0; i<terms.length; i++){
            String s = terms[i];
            int j = 0;
            while(j<=s.length() && j<= MAX_PREFIX){
                String prefix = s.substring(0,j);


                myMap.putIfAbsent(prefix, new ArrayList<>());

                myMap.get(prefix).add(new Term(s, weights[i]));
                j++;


            }
        }

        for(String key : myMap.keySet()){
            Collections.sort(myMap.get(key), Comparator.comparing(Term::getWeight).reversed());
        }



    }

    /**
     * Return size in bytes of all Strings and doubles
     * stored in implementing class. To the extent that
     * other types are used for efficiency, there size should
     * be included too
     *
     * @return number of bytes used after initialization
     */
    @Override
    public int sizeInBytes() {
        if(mySize == 0){
            for(List<Term> terms : myMap.values()){
                for(Term t : terms){
                    mySize += BYTES_PER_DOUBLE+BYTES_PER_CHAR*t.getWord().length();
                }
            }
            for (String key : myMap.keySet()){
                mySize += BYTES_PER_CHAR * key.length();
            }
        }
        return mySize;
    }
}
