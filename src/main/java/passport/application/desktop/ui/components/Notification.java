package passport.application.desktop.ui.components;

import java.awt.*;

public class Notification {

    public Notification(String title, String message) {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray not supported!");
            return;
        }

        var imageUrl = getClass().getResource("/desktop/icons/ticket.png");

        var image = Toolkit.getDefaultToolkit().createImage(imageUrl);

        TrayIcon trayIcon = new TrayIcon(image, "PassPort's Notification");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("PassPort's icon");

        try {
            SystemTray.getSystemTray().add(trayIcon);
            trayIcon.displayMessage(title, message,
                    TrayIcon.MessageType.INFO);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
