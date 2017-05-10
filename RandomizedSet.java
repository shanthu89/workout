/**
 * Created by shant on 4/17/2017.
 */
import java.util.*;
public class RandomizedSet {

    /** Initialize your data structure here. */
    HashMap<Integer,Integer> map; // Key --> val, Value --> Index in arrayList.
    ArrayList<Integer> list;
    int size;
    public RandomizedSet() {
        map = new HashMap<>();
        list = new ArrayList<>();
        size = 0;
    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if(map.containsKey(val))
            return false;
        list.add(size,val);
        map.put(val,size++);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if(!map.containsKey(val)){
            return false;
        }
        int eleIndex = map.get(val);
        if(eleIndex<size-1){
            int last = list.get(size-1);
            list.set(eleIndex,last);
            map.put(last,eleIndex);
        }
        list.remove(size-1);
        map.remove(val);
        size--;
        return true;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        if(size==0)
            return -1;
        if(size==1)
            return list.get(0);
        Random rand = new Random();
        int  randIndex = rand.nextInt(map.size());
        return list.get(randIndex);
    }
    public static void main(String[] args){
        RandomizedSet obj = new RandomizedSet();
        int val = 10;
        int val1 = 11;
        boolean param_1 = obj.insert(val);
        boolean param_11 = obj.insert(val1);
        boolean param_2 = obj.remove(val);
        int param_3 = obj.getRandom();

        System.out.println("Param1:" + param_1 + " param_11: " + param_11 + " Param2: " + param_2 + " Param3" + param_3);
    }
}
