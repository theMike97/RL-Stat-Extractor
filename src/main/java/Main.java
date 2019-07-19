import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjAxNjkxNzg5NjI3MzU5MjMy.XTGCqA.n3-YEkoDopxU9MAPhdmQYayN1uw";
        builder.setToken(token);
        builder.addEventListener(new ListenerMain());
        builder.build();
    }

}
