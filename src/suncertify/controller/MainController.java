package suncertify.controller;

import suncertify.gui.client.UrlyBirdClientGui;
import suncertify.gui.server.UrlyBirdServerGui;

/**
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class MainController {

    public static void main(String[] args) {
        new UrlyBirdClientGui(args).setVisible(true);
        new UrlyBirdServerGui().setVisible(true);
    }
}
