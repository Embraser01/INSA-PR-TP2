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


public class Window {
    private JList<String> messageList;
    private DefaultListModel<String> defaultListModel;
    private JTextField messageField;
    private JButton sendButton;
    private JPanel root;
    private JScrollPane scrollPane;
    private IClient client;


    private static String name;
    private static Window instance = null;


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

    public static void main(String[] args) throws IOException {

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

    public static Window getInstance() {
        return instance;
    }

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

    public void addMessage(String message) {
        defaultListModel.addElement(message);

        // Stick to bottom
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    private void createUIComponents() {
        defaultListModel = new DefaultListModel<>();

        messageList = new JList<>(defaultListModel);
        messageList.setCellRenderer(new MyListCellRenderer());
    }

    private static class Notifier implements INotifier {

        @Override
        public void notifyMessage(String message) {
            instance.addMessage(message);
        }
    }
}