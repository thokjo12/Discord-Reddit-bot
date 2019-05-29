package networking;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import networking.Client.RedditClient;
import objects.Mapper;
import objects.reddit.RedditResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Created by Thomas on 30.01.2018.
 */
public class NetworkApi {
    private static RedditClient redditClient = RetrofitFactory.buildClient(RedditClient.class);

    public static <F, T> CompletableFuture<T> _fetch(Call<F> call, Mapper<F,T> mapper){
        return fetch(call,(future,body) -> future.complete(mapper.apply(body)));
    }

    static <F, T> CompletableFuture<T> fetch(Call<F> call, BiConsumer<CompletableFuture<T>, F> filler) {
        CompletableFuture<T> future = new CompletableFuture<>();
        call.enqueue(new Callback<F>() {
            @Override
            public void onResponse(Call<F> call, Response<F> response) {
                if (response.isSuccessful()) {
                    filler.accept(future, response.body());
                } else {
                    try {
                        if (response.errorBody() != null) {
                            future.completeExceptionally(new Exception(response.errorBody().string()));

                        } else {
                            future.completeExceptionally(new Exception("Response was null"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<F> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public static CompletableFuture<RedditResponse> getTopPost(String sub){
        return fetch(redditClient.getTopPosts(sub),CompletableFuture::complete);
    }

    public static CompletableFuture<RedditResponse> getNewPost(String sub){
        return fetch(redditClient.getNewPosts(sub),CompletableFuture::complete);
    }
}
