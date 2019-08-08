package bg.sofia.uni.fmi.mjt.cache;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException(String name){
        super(name);
    }
}
