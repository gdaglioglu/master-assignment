package suncertify.controller;

import suncertify.gui.client.UrlyBirdClientGui;
import suncertify.gui.server.UrlyBirdServerGui;
import suncertify.utilities.URLyBirdApplicationMode;

/**
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class MainController {

    private static URLyBirdApplicationMode urlyBirdApplicationMode;

    public static void main(String[] args) {

        if (args.length == 0) {

            urlyBirdApplicationMode = URLyBirdApplicationMode.NETWORKED_CLIENT;
            new UrlyBirdClientGui(urlyBirdApplicationMode).setVisible(true);

        } else if (args[0].equalsIgnoreCase("standalone")) {

            urlyBirdApplicationMode = URLyBirdApplicationMode.STANDALONE_CLIENT;
            new UrlyBirdClientGui(urlyBirdApplicationMode).setVisible(true);

        } else if (args[0].equalsIgnoreCase("server")) {

            urlyBirdApplicationMode = URLyBirdApplicationMode.SERVER_ONLY;
            new UrlyBirdServerGui().setVisible(true);

        } else {

            System.err.println("    [error]    Incorrect Usage.\n"
                    + "Usage options are:\n\n"
                    + " 1. java -jar URLyBird.jar\n"
                    + " 2. java -jar URLyBird.jar standalone\n"
                    + " 3. java -jar URLyBird.jar server\n"
                    + "\n\nRefer to the userguide.txt document for more information.");
            System.exit(1);
        }
    }
}
