/**
 * Created by shant on 3/2/2017.
 */
import java.util.*;
public class LRUCache {
    public class Node{
        int val;
        int key;
        Node prev;
        Node next;
        Node(int key,int val){
            this.val = val;
            this.key = key;
            this.prev  = null;
            this.next  = null;
        }
    }
    public LRUCache(int capacity) {
        this.cacheSize = capacity;
    }
    HashMap<Integer,Node> map = new HashMap<>();
    Node head;
    Node tail;
    int cacheSize;
    public static void main(String[] args){
        LRUCache obj = new LRUCache(3);
        obj.put(1,1);
        obj.put(2,2);
        obj.put(3,3);
        obj.put(4,4);
        obj.get(4);
        obj.get(3);
        obj.get(2);
        obj.get(1);
        obj.put(5,5);
        obj.get(1);
        obj.get(2);
        obj.get(3);
        obj.get(4);
        obj.get(5);
        /*
        //obj.put(2,20);
        //obj.put(3,30);
        //obj.put(13,35);
        //obj.get(2);
        obj.put(1,5);
        obj.put(1,2);
        obj.get(1);
        obj.get(2);
        //obj.put(15,305);*/
        obj.viewMap();
    }
    public void viewMap(){
        for(Map.Entry<Integer,Node> m:map.entrySet()){
            System.out.print("Key: " + m.getKey());
            System.out.println(" Value: " + m.getValue().val);
        }
    }
    public void removeLRU(){//LRU is at the head. Just remove it from map and advance head pointer.
        map.remove(head.key);
        if(head.next==null)
            head = null;
        else{
            head = head.next;
            head.prev = null;
        }
    }

    public void moveToTail(Node n){//move the most recently used node to the end of list/tail.
        if(tail==n)
            return;
        if(head==n){
            head = head.next;

        }else{
            n.prev.next = n.next;
            n.next.prev = n.prev;

        }
        tail.next = n;
        n.prev = tail;
        tail = tail.next;
        tail.next = null;
    }
    public int get(int key) {
        Node n = map.get(key);
        if(n!=null){
            moveToTail(n);
            System.out.println("GOT: " + n.val);
            return n.val;
        }
        System.out.println("GOT: " + -1);
        return -1;
    }
    public void addToList(Node n){//add new node to list when new key added.
        //Node n = new Node(value);
        if(head==null){//means no node in list.
            head = n;
            tail = n;
        }else{
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
    }
    public void put(int key, int value) {
        if(map.containsKey(key)){//if key already present just update the value and move to end.
            Node temp = map.get(key);
            temp.val = value;
            map.put(key,temp);
            moveToTail(temp);
            System.out.println("PUT UPDATE "+ temp.key);
            return;
        }
        if(map.size()<cacheSize){//if capacity not reached just add to list.
            Node n = new Node(key,value);
            map.put(key,n);
            addToList(n);
        }
        else{
            removeLRU();
            Node n = new Node(key,value);
            map.put(key,n);
            addToList(n);
        }
        System.out.println("PUT OK");
    }
}
