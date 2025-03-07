
import java.security.Principal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControllerTest {

    // Cette méthode vous permettra de vérifier le contenu de l'objet Principal (par exemple, après une authentification OAuth2)
    @GetMapping("/auth/me")
    public String getCurrentUser(@org.springframework.security.core.annotation.AuthenticationPrincipal OidcUser oidcUser, Principal principal) {
        if (oidcUser != null) {
            // Affichage pour démonstration (Attention : en production, ne jamais exposer le token)
            return "Welcome, " + oidcUser.getFullName() + " (" + oidcUser.getEmail() + ")<br>" +
                   "ID Token: " + oidcUser.getIdToken().getTokenValue();
        }
        return "User not authenticated";
    }
}
