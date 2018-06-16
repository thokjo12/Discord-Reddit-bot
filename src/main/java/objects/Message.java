package objects;

/**
 * Created by Thomas on 30.01.2018.
 */
public class Message<R, M> {
    public R result;
    public M message;

    public void result(R result) {
        this.result = result;
    }

    public void message(M message) {
        this.message = message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean hasResult() {
        return result != null;
    }
}

