package eu.darkbot.util.function;


/**
 * Represents a function that accepts one argument, produces a result, and may throw an exception.
 * It works similar to {@link java.util.function.Function}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of exception that may be thrown by the function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws E if the function body throws an exception
     */
    R apply(T t) throws E;
}
