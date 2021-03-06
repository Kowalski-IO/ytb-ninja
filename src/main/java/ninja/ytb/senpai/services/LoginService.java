package ninja.ytb.senpai.services;

import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import io.kowalski.oaami.models.OaamiToken;
import ninja.ytb.senpai.models.AccessToken;
import ninja.ytb.senpai.models.User;
import ninja.ytb.senpai.security.OAuthUserToken;

public class LoginService {

	private final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

	private final AccessTokenService accessTokenService;

	@Inject
	public LoginService(final AccessTokenService accessTokenService) {
		this.accessTokenService = accessTokenService;
	}

	public final User login(final OaamiToken token) throws AuthenticationException {
		User user = null;
		Optional<AccessToken> optionalAccessToken = accessTokenService.retrieveAccessToken(token);
		if (optionalAccessToken.isPresent()) {
			OAuthUserToken oAuthToken = new OAuthUserToken(token);
			oAuthToken.setUser(optionalAccessToken.get().getUser());
			SecurityUtils.getSubject().login(oAuthToken);
		} else {
			LOGGER.error("Unable to create or retreive access token for login.");
			throw new AuthenticationException("Unable to create / retreive access token for login");
		}
		return user;
	}
}
