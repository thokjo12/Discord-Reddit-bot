package networking;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thomas on 28.01.2018.
 */
public class RetrofitFactory {
    static final String baseURL = "https://www.reddit.com/r/";

    public static <E> E buildClient(Class<E> clientclass){
        GsonBuilder builder = new GsonBuilder();
        return new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build()
                .create(clientclass);
    }
}
