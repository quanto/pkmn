package security

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import game.PlayerData
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import game.Player

public class AuthenticationSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        if (event instanceof InteractiveAuthenticationSuccessEvent) {

            def session = RequestContextHolder.currentRequestAttributes().session
            String username = SecurityContextHolder.context.authentication?.getPrincipal()?.username
            System.out.println(username)
            Player player = Player.findByUsername(username)
            session.playerData = new PlayerData(player.id)
        }
    }
}