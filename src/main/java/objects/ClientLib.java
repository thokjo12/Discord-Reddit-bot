package objects;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Created by Thomas on 28.01.2018.
 */
public class ClientLib {

    public static IDiscordClient createClient(String token, boolean login){
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try{
            if(login){
                return clientBuilder.login();
            }else{
                return clientBuilder.build();
            }
        }catch (DiscordException e){
            System.out.println("Error building client: " + e.getErrorMessage());
            return null;
        }
    }
}
