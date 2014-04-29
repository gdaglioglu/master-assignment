package suncertify.controller;

import suncertify.db.DatabaseFileUtils;
import suncertify.gui.UrlyBirdClientGui;
import suncertify.model.HotelRoom;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class MainController {

    public static void main(String[] args) {
        new UrlyBirdClientGui(args).setVisible(true);
    }
}
