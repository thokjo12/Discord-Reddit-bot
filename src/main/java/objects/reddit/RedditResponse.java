package objects.reddit;


import objects.reddit.subtypes.Data;
import objects.reddit.subtypes.SubData;

/**
 * Created by Thomas on 30.01.2018.
 */
public class RedditResponse {
    public Data data;

    public SubData getInitialSubData(){
        return data.children.get(0).data;
    }
}
