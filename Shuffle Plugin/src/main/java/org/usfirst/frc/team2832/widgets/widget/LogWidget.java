package org.usfirst.frc.team2832.widgets.widget;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.javafx.tk.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.*;
import org.usfirst.frc.team2832.widgets.data.LogData;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Description(name = "Log", dataTypes = String[].class)
@ParametrizedController("LogWidget.fxml")
public class LogWidget extends SimpleAnnotatedWidget<LogData> {

    @FXML
    private Pane root;
    @FXML
    private Button button;
    @FXML
    ComboBox<String> comboBox;

    private final Property<Color> disconnectColor = new SimpleObjectProperty<>(this, "disconnectColor", Color.LAWNGREEN);
    private final Property<Color> connectColor = new SimpleObjectProperty<>(this, "connectColor", Color.DARKRED);
    private final Property<Color> downloadColor = new SimpleObjectProperty<>(this, "downloadColor", Color.AQUA);

    final static boolean USING_SFTP = false;

    private File directory;
    private boolean isDownloading;

    final private static String IP = "10.28.32.2";
    final private static String USERNAME = "anonymous";
    final private static String PASSWORD = "";

    @FXML
    private void initialize() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("USB", "RIO", "SIM");
        comboBox.setItems(list);

        directory = new File("C:\\");
        new Thread(() -> {
            while (true) {
                root.backgroundProperty().setValue(createSolidColorBackground(getColor()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        exportProperties(disconnectColor, connectColor, downloadColor);

        button.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setInitialDirectory(directory);
                directoryChooser.setTitle("Select Target Log Directory");
                File selectedDir = directoryChooser.showDialog(root.getScene().getWindow());
                if (selectedDir == null)
                    return;
                directory = selectedDir;
                new Thread(() -> {
                    isDownloading = true;
                    root.backgroundProperty().setValue(createSolidColorBackground(getColor()));
                    File logDir = new File(directory.getAbsolutePath() + "\\LOGS");
                    logDir.mkdir();
                    File csvDir = new File(directory.getAbsolutePath() + "\\CSV");
                    csvDir.mkdir();
                    if (!"SIM".equals(comboBox.getValue())) {
                        try {
                            String sourceDir;
                            if ("USB".equals(comboBox.getValue()))
                                sourceDir = "/U/";
                            else // Must be 'RIO'
                                sourceDir = "/home/lvuser/";
                            if (USING_SFTP) {
                                Session session = getSession(IP, USERNAME, PASSWORD);
                                session.connect();
                                downloadFiles(session, sourceDir + "LOGS", directory.getAbsolutePath() + "\\LOGS");
                                session.disconnect();
                                session = getSession(IP, USERNAME, PASSWORD);
                                session.connect();
                                downloadFiles(session, sourceDir + "CSV", directory.getAbsolutePath() + "\\CSV");
                                session.disconnect();
                            } else {
                                FTPClient ftpClient = new FTPClient();
                                ftpClient.connect(IP, 21);
                                ftpClient.login(USERNAME, PASSWORD);
                                ftpClient.enterLocalPassiveMode();
                                downloadDirectory(ftpClient, sourceDir + "LOGS", directory.getAbsolutePath() + "LOGS");
                                downloadDirectory(ftpClient, sourceDir + "CSV", directory.getAbsolutePath() + "CSV");
                                ftpClient.logout();
                                ftpClient.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            FileUtils.copyDirectory(new File(System.getProperty("user.home") + "\\LOGS"), new File(directory.getAbsolutePath() + "\\LOGS"));
                            FileUtils.copyDirectory(new File(System.getProperty("user.home") + "\\CSV"), new File(directory.getAbsolutePath() + "\\CSV"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (csvDir.list().length == 0)
                        csvDir.delete();
                    if (logDir.list().length == 0)
                        logDir.delete();
                    isDownloading = false;
                    root.backgroundProperty().setValue(createSolidColorBackground(getColor()));
                }).start();
            }
        });
    }

    private Background createSolidColorBackground(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    private Color getColor() {
        if (isDownloading)
            return downloadColor.getValue();
        Session session = null;
        try {
            session = getSession(IP, USERNAME, PASSWORD);
            session.connect();
            return disconnectColor.getValue();
        } catch (Exception e) {
            if (session != null)
                session.disconnect();
            return connectColor.getValue();
        }
    }

    @Override
    public Pane getView() {
        return root;
    }

    private static Session getSession(String hostIp, String username, String password) throws Exception {
        JSch jsch = new JSch();
        jsch.addIdentity(System.getProperty("user.home") + "//.ssh//id_rsa");
        Session session = jsch.getSession(username, hostIp);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        //session.setConfig("PreferredAuthentications", "password");
        //session.setConfig("cipher.s2c", "aes128-cbc,3des-cbc,blowfish-cbc");
        //session.setConfig("cipher.c2s", "aes128-cbc,3des-cbc,blowfish-cbc");
        //session.setConfig("CheckCiphers", "aes128-cbc");
        return session;
    }

    private static ChannelSftp getChannelSftp(Session session) throws Exception {
        Channel channel = session.openChannel("sftp");
        channel.setInputStream(System.in);
        channel.setOutputStream(System.out);
        channel.connect();

        ChannelSftp sftpChannel = (ChannelSftp) channel;
        return sftpChannel;
    }

    private static void downloadFiles(Session session, String srcDir, String destDir) throws Exception {
        ChannelSftp sftpChannel = getChannelSftp(session);
        List<ChannelSftp.LsEntry> list = getFiles(sftpChannel, srcDir, destDir);
        try {
            for (ChannelSftp.LsEntry file : list) {
                if (!file.getAttrs().isDir()) {
                    sftpChannel.get(file.getFilename(), file.getFilename());
                }
            }
        } finally {
            if (sftpChannel != null)
                sftpChannel.disconnect();
        }
    }

    private static List<ChannelSftp.LsEntry> getFiles(ChannelSftp sftpChannel, String srcDir, String destDir) throws Exception {
        sftpChannel.lcd(destDir);
        sftpChannel.cd(srcDir);
        // Get a listing of the remote directory
        @SuppressWarnings("unchecked")
        List<ChannelSftp.LsEntry> list = sftpChannel.ls(".");
        return list;
    }

    private static void downloadDirectory(FTPClient ftpClient, String remoteDir, String localDir) throws IOException {
        FTPFile[] subFiles = ftpClient.listFiles(remoteDir);
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                if (!aFile.isDirectory()) {
                    boolean success = downloadSingleFile(ftpClient, remoteDir + "/" + aFile.getName(), localDir + "\\" + aFile.getName());
                    if (success) {
                        System.out.println("DOWNLOADED " + aFile.getName());
                    } else {
                        System.out.println("COULD NOT download " + aFile.getName());
                    }
                }
            }
        }
    }

    private static boolean downloadSingleFile(FTPClient ftpClient, String remoteFilePath, String savePath) throws IOException {
        File downloadFile = new File(savePath);
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void main(String[] args) {
        // Test values
        boolean isDownloading;
        File directory = new File("U:\\");
        new JFXPanel();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("RIO");

        // Code in thread starting:
        isDownloading = true;
        //root.backgroundProperty().setValue(createSolidColorBackground(getColor()));
        File logDir = new File(directory.getAbsolutePath() + "\\LOGS");
        logDir.mkdir();
        File csvDir = new File(directory.getAbsolutePath() + "\\CSV");
        csvDir.mkdir();
        if (!"SIM".equals(comboBox.getValue())) {
            try {
                String sourceDir;
                if ("USB".equals(comboBox.getValue()))
                    sourceDir = "/U/";
                else // Must be 'RIO'
                    sourceDir = "/home/lvuser/";//"/home/lvuser/";
                FTPClient ftpClient;
                try {
                    ftpClient = new FTPClient();
                    // connect and login to the server
                    //ftpClient.connect(IP, 21);
                    //ftpClient.login(USERNAME, PASSWORD);
                    ftpClient.connect(IP, 21);
                    ftpClient.login(USERNAME, PASSWORD);
                    // use local passive mode to pass firewall
                    ftpClient.enterLocalPassiveMode();
                    downloadDirectory(ftpClient, sourceDir + "LOGS", directory.getAbsolutePath() + "LOGS");
                    downloadDirectory(ftpClient, sourceDir + "CSV", directory.getAbsolutePath() + "CSV");
                    // log out and disconnect from the server
                    ftpClient.logout();
                    ftpClient.disconnect();

                    System.out.println("Disconnected");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //root.backgroundProperty().setValue(createSolidColorBackground(getColor()));
            } catch (Exception e) {
                //if (session != null)
                //  session.disconnect();
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.copyDirectory(new File(System.getProperty("user.home") + "\\LOGS"), new File(directory.getAbsolutePath() + "\\LOGS"));
                FileUtils.copyDirectory(new File(System.getProperty("user.home") + "\\CSV"), new File(directory.getAbsolutePath() + "\\CSV"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (csvDir.list().length == 0)
            csvDir.delete();
        if (logDir.list().length == 0)
            logDir.delete();
        isDownloading = false;

    }
}
