package client.ui;

import protocol.IClient;
import protocol.INotifier;
import protocol.IServer;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main client class, sets up the GUI, the RMI registry and the bindings
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class Window {
    /**
     * List used to display the messages
     */
    private JList<String> messageList;

    /**
     * List model storing messages, used by the JList to display the messages
     */
    private DefaultListModel<String> defaultListModel;

    /**
     * Field used to type and send messages
     */
    private JTextField messageField;

    /**
     * Button used to send messages
     */
    private JButton sendButton;

    /**
     * Root panel of the interface
     */
    private JPanel root;

    /**
     * Scroll pane allowing the message list to be scrolled
     */
    private JScrollPane scrollPane;

    /**
     * Client object returned by the server, used to execute remote commands
     */
    private IClient client;

    /**
     * Name of the user
     */
    private static String name;

    /**
     * Instance of the window (singleton)
     */
    private static Window instance = null;

    /**
     * Sets the current client object to communicate with the server
     *
     * @param client Client to be used for the communication
     */
    private void setClient(IClient client) {
        this.client = client;

        sendButton.addActionListener(e -> sendMessage());

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    /**
     * Main entry point of the program, sets up the RMI registry, the interface, and gets a new Client handler from
     * the server
     *
     * @param args Arguments given to the program, we use args[0] for the hostname and args[1] for the port
     */
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        Integer port = (args.length < 2) ? 1099 : Integer.parseInt(args[1]);

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            IServer stub = (IServer) registry.lookup("Server");

            name = JOptionPane.showInputDialog(null, "Nickname?");

            instance = new Window();

            IClient client = stub.connect(
                    (INotifier) UnicastRemoteObject.exportObject(new Notifier(), 0),
                    name);

            instance.setClient(client);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


        JFrame frame = new JFrame(name);
        frame.setContentPane(instance.root);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Returns the instance of the window (singleton)
     *
     * @return the singleton
     */
    public static Window getInstance() {
        return instance;
    }

    /**
     * Handles the input of the message field and calls the corresponding methods on the client object
     */
    public void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) return;
        String[] words = message.split(" ");
        String command = words[0];

        try {
            switch (command) {
                case "/join":
                    defaultListModel.removeAllElements();
                    if (!client.join(words[1])) {
                        addMessage("Invalid room");
                    }
                    break;
                case "/leave":
                    client.leave();
                    defaultListModel.removeAllElements();
                    break;
                case "/nick":
                    client.updateNickname(words[1]);
                    name = words[1];
                    break;
                case "/help":
                    addMessage(client.help());
                    break;
                default:
                    if (message.charAt(0) == '/') {
                        addMessage("Unknown command!");
                    } else if (!client.broadcast(message)) {
                        addMessage("You have to join a room first!");
                    }
                    break;
            }
        } catch (RemoteException e) {
            addMessage("<html><span style=\"color=red\">" + e.getMessage() + "</span></html>");
        }

        messageField.setText("");
    }

    /**
     * Adds a new message to the message list
     *
     * @param message Message to be added
     */
    public void addMessage(String message) {
        defaultListModel.addElement(message);

        // Stick to bottom
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    /**
     * Initializes the list and its model
     */
    private void createUIComponents() {
        defaultListModel = new DefaultListModel<>();

        messageList = new JList<>(defaultListModel);
        messageList.setCellRenderer(new MyListCellRenderer());
    }

    /**
     * Object sent to the server used to notify the client of new messages
     */
    private static class Notifier implements INotifier {

        /**
         * Notifies the client of the arrival of a new message
         *
         * @param message New message
         */
        @Override
        public void notifyMessage(String message) {
            instance.addMessage(message);
        }
    }
}