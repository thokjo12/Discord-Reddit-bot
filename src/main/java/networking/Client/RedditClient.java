package networking.Client;


import objects.reddit.RedditResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RedditClient {
    @GET("{subreddit}/top/.json")
    Call<RedditResponse> getTopPosts(@Path("subreddit") String subreddit);

    @GET("{subreddit}/new/.json")
    Call<RedditResponse> getNewPosts(@Path("subreddit") String subreddit);
}
