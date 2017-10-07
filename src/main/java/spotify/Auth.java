package spotify;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import utils.APIKeys;

class Auth {

    static Api getApi() {
        Api api = Api.builder()
                .clientId(APIKeys.keys.get("spotify_clientID"))
                .clientSecret(APIKeys.keys.get("spotify_clientSecret"))
                .build();

        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {
                System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
                System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

                api.setAccessToken(clientCredentials.getAccessToken());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        return api;
    }
}
