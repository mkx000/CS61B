package lab9;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MYHashMap<K, V> implements Map61B<K, V> {
    private int size;

    private ArrayMap<K, V>[] buckets;
    private static final int DEFAULT_SIZE = 16;

    private static final double MAX_LOAD = .75;

    public MYHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            buckets[i] = new ArrayMap<>();
        }
        size = 0;
    }

    public static void main(String[] args) {
        MYHashMap<String, Integer> b = new MYHashMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            //make sure put is working via containsKey and get
            if (b.get("hi" + i) == null) {
                System.out.println(i);
            }
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.containsKey("hi" + i));
            assertTrue(null != b.get("hi" + i)
                    && b.containsKey("hi" + i));
        }
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    @Override
    public void clear() {
        buckets = null;
        size = 0;
    }

    private int hash(K key) {
        if (key == null) {
            return -1;
        }
        int hashcode = key.hashCode();
        return Math.floorMod(hashcode, buckets.length);
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        return buckets[index].get(key);
    }

    private double load_factor(){
        return (double) size / buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (index == -1){
            return;
        }
        if (buckets[index].get(key) == null){
            size++;
        }
        buckets[index].put(key, value);
        if (load_factor() > MYHashMap.MAX_LOAD){
            resize();
        }
    }

    public void resize(){
        ArrayMap[] newBuckets = new ArrayMap[2 * buckets.length];
        for (int i = 0; i < newBuckets.length; i++){
            newBuckets[i] = new ArrayMap();
        }
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        buckets = newBuckets;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
