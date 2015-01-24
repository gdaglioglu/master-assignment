package suncertify.client.gui;

/** Specifies the types of connections that can be made to the server. */
public enum ConnectionType {
    /** an RMI based server. */
    RMI,
    /** direct connect - no network involved. */
    DIRECT
}
