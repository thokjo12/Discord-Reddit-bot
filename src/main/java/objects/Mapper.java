package objects;

/**
 * Created by Thomas on 16.06.2018.
 */
@FunctionalInterface
public interface Mapper<From, To> {
    To apply(From t);
}
