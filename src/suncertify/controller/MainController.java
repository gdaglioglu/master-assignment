package suncertify.controller;

import suncertify.gui.client.UrlyBirdClientGui;
import suncertify.gui.server.UrlyBirdServerGui;
import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationMode;

/**
 * This is the class that handles the starting of the URLyBird Application.
 *
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class MainController {

    /**
     * The entry point of the URLyBird Application.
     *
     * @param args The switch to set the {@code UrlyBirdApplicationMode}.
     */
    public static void main(String[] args) {

        if (args.length == 0) {

            new UrlyBirdClientGui(UrlyBirdApplicationMode.NETWORKED_CLIENT).setVisible(true);

        } else if (args[0].equalsIgnoreCase(UrlyBirdApplicationConstants.CLI_ARG_STANDALONE_MODE)) {

            new UrlyBirdClientGui(UrlyBirdApplicationMode.STANDALONE_CLIENT).setVisible(true);

        } else if (args[0].equalsIgnoreCase(UrlyBirdApplicationConstants.CLI_ARG_SERVER_MODE)) {

            new UrlyBirdServerGui().setVisible(true);

        } else {

            System.err.println("    [error]    Incorrect Usage.\n"
                    + "Usage options are:\n\n"
                    + " 1. java -jar URLyBird.jar\n"
                    + " 2. java -jar URLyBird.jar " + UrlyBirdApplicationConstants.CLI_ARG_STANDALONE_MODE + "\n"
                    + " 3. java -jar URLyBird.jar " + UrlyBirdApplicationConstants.CLI_ARG_SERVER_MODE + "\n"
                    + "\n\nRefer to the userguide.txt document for more information.");
            System.exit(1);
        }
    }
}
