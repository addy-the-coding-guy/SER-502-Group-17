public class HelloWorld{

     public static void main(String []args){
        HashMap h = new HashMap();
        h.insert("Aditya",10);
        h.insert("Bhanu",20);
        h.insert("Lee",30);
        h.displayHashMap();
     }
}

class HashMap {
    private BucketEntry[] bucket = new BucketEntry[10];
    
    HashMap() {
        for(int i = 0; i < 10; i++)
            bucket[i] = new BucketEntry();
    }
    
    public void insert(String key, int value) {
        int index = this.hash(key);
        bucket[index].setKey(key);
        bucket[index].setValue(value);
    }
    
    public int hash(String key) {
        int len = key.length();
        return len%10;
    }
    
    public void displayHashMap() {
        for (int i = 0; i< 10; i++) {
            System.out.println("Key:"+this.bucket[i].getKey() +"value:" + this.bucket[i].getValue());
        }
    }
}

class BucketEntry {
    private String key;
    private int value;
    
    BucketEntry() {
        key = "";
        value = 0;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setValue(int Value) {
        value = Value;
    }
    
    public void setKey(String KeyValue) {
        key = KeyValue;
    }
}
