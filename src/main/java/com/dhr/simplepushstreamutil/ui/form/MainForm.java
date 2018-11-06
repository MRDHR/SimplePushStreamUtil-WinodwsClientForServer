package com.dhr.simplepushstreamutil.ui.form;

import com.dhr.simplepushstreamutil.bean.*;
import com.dhr.simplepushstreamutil.entity.LiveAreaListEntity;
import com.dhr.simplepushstreamutil.mina.MinaClient;
import com.dhr.simplepushstreamutil.runnable.InstallFfmpegRunnable;
import com.dhr.simplepushstreamutil.runnable.InstallStreamlinkRunnable;
import com.dhr.simplepushstreamutil.runnable.InstallYoutubedlRunnable;
import com.dhr.simplepushstreamutil.ui.dialog.*;
import com.dhr.simplepushstreamutil.util.*;
import com.google.gson.Gson;
import com.jcraft.jsch.ChannelSftp;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainForm extends JFrame {
    private BiliBiliAccountDialog bilibiliAccountDialog;
    private ConfigSchemeDialog configSchemeDialog;
    private AreaSettingDialog areaSettingDialog;

    private SourceUrlInfoDialog resourceUrlInfoDialog;
    private ServerInfoDialog serverInfoDialog;
    private LiveRoomUrlInfoDialog liveRoomUrlInfoDialog;

    private String resourceUrl;
    private String m3u8Url;
    private String liveRoomUrl;

    private JsonUtil jsonUtil = new JsonUtil();
    private Gson gson = new Gson();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private LocalDataBean localDataBean;

    private String serverIp;
    private int serverPort;
    private String userName;
    private String userPassword;

    private int controlPanelHeight;

    private boolean isLocalFile = false;

    private List<ResolutionBean> listResolutions = new ArrayList<>();

    private MinaClient minaClient;

    private JschUtil jschUtil = new JschUtil();
    private SftpUtil sftpUtil;
    private String userDirPath = new File(System.getProperty("user.dir")).getPath();
    private File file1 = new File(userDirPath + "\\SimplePushStreamUtil-Server.service");
    private File file2 = new File(userDirPath + "\\SimplePushStreamUtil-Server.jar");

    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
        }
        JFrame frame = new JFrame("简易推流工具");
        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        MainForm mainForm = new MainForm();

        JMenuBar mb = new JMenuBar();                 //实例菜单栏
        JMenu config = new JMenu("配置");                //实例一个菜单项
        JMenu environment = new JMenu("环境");                //实例一个菜单项
        JMenuItem mi10 = new JMenuItem("安装推流服务端");
        JMenuItem mi7 = new JMenuItem("安装ffmpeg");
        JMenuItem mi8 = new JMenuItem("安装youtube-dl");
        JMenuItem mi9 = new JMenuItem("安装streamlink");
        JMenu more = new JMenu("更多");
        JMenuItem mi2 = new JMenuItem("配置B站账号");
        JMenuItem mi3 = new JMenuItem("配置解析方案");


        JMenuItem mi5 = new JMenuItem("帮助");
        JMenuItem mi6 = new JMenuItem("关于");

        frame.setJMenuBar(mb);                        //设置菜单栏
        mb.add(config);                               //添加菜单项
        mb.add(environment);                               //添加菜单项
        mb.add(more);                               //添加菜单项
        config.add(mi2);                             //加入子菜单
        config.add(mi3);                             //加入子菜单

        more.add(mi5);                              //添加子目录
        more.add(mi6);

        environment.add(mi10);
        environment.add(mi7);
        environment.add(mi8);
        environment.add(mi9);

        mi2.addActionListener(e -> mainForm.showBilibiliAccountDialog());

        mi3.addActionListener(e -> mainForm.showConfigSchemeDialog());
        mi6.addActionListener(e -> mainForm.showTipsDialog("随手写的程序，没啥好说的"));

        mi7.addActionListener(e -> mainForm.installFfmpeg());
        mi8.addActionListener(e -> mainForm.installYoutubeDl());
        mi9.addActionListener(e -> mainForm.installStreamLink());
        mi10.addActionListener(e -> mainForm.installServer());

        frame.setContentPane(mainForm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private MainForm() {
        initView();
        initData();
    }

    private void installFfmpeg() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
        } else {
            taLog.setText("开始安装ffmpeg，请耐心等待（全过程预计5-6分钟。由于同步的问题，日志输出较慢）\n");
            try {
                serverPort = Integer.parseInt(cacheServerPort);
                executorService.execute(() -> {
                    try {
                        sftpUtil = new SftpUtil(userName, userPassword, serverIp, serverPort);
                        ChannelSftp login = sftpUtil.login();
                        if (null != login) {
                            addTextToLog("登录服务器成功，开始上传ffmpeg安装文件\n");
                            File file = new File(userDirPath + "\\installffmpeg.sh");
                            FileInputStream fis = new FileInputStream(file);
                            UploadMonitor monitor = new UploadMonitor(file.length(), uploadCallBack);
                            sftpUtil.upload("/usr/local/src/", "SimplePushStreamUtil/", "installffmpeg.sh", fis, monitor);
                            addTextToLog("开始执行安装文件\n");
                            jschUtil.versouSshUtil(serverIp, userName, userPassword, serverPort);
                            jschUtil.runCmd("cd /usr/local/src/SimplePushStreamUtil/ && chmod a+x installffmpeg.sh && ./installffmpeg.sh", installFfmpegCallBack);
                        } else {
                            addTextToLog("登录服务器失败");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        addTextToLog("ffmpeg安装失败\n");
                    }
                });
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
            }
        }
    }

    private InstallFfmpegRunnable.InstallFfmpegCallBack installFfmpegCallBack = log -> {
        addTextToLog(log + "\n");
        if (log.contains("ffmpeg安装结束")) {
            if (null != jschUtil) {
                jschUtil.close();
            }
        }
    };

    private void installYoutubeDl() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
        } else {
            taLog.setText("开始安装youtube-dl，请耐心等待（全过程预计1-2分钟。由于同步的问题，日志输出较慢）\n");
            try {
                serverPort = Integer.parseInt(cacheServerPort);
                executorService.execute(() -> {
                    try {
                        sftpUtil = new SftpUtil(userName, userPassword, serverIp, serverPort);
                        ChannelSftp login = sftpUtil.login();
                        if (null != login) {
                            addTextToLog("登录服务器成功，开始上传youtube-dl安装文件\n");
                            File file = new File(userDirPath + "\\installyoutubedl.sh");
                            FileInputStream fis = new FileInputStream(file);
                            UploadMonitor monitor = new UploadMonitor(file.length(), uploadCallBack);
                            sftpUtil.upload("/usr/local/src/", "SimplePushStreamUtil/", "installyoutubedl.sh", fis, monitor);
                            addTextToLog("开始执行安装文件\n");
                            jschUtil.versouSshUtil(serverIp, userName, userPassword, serverPort);
                            jschUtil.runCmd("cd /usr/local/src/SimplePushStreamUtil/ && chmod a+x installyoutubedl.sh && ./installyoutubedl.sh", installYoutubedlCallBack);
                        } else {
                            addTextToLog("登录服务器失败");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        addTextToLog("youtube-dl安装失败\n");
                    }
                });
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
            }
        }
    }

    private InstallYoutubedlRunnable.InstallYoutubedlCallBack installYoutubedlCallBack = log -> {
        addTextToLog(log + "\n");
        if (log.contains("youtube-dl安装结束")) {
            if (null != jschUtil) {
                jschUtil.close();
            }
        }
    };

    private void installStreamLink() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
        } else {
            taLog.setText("开始安装streamlink，请耐心等待（全过程预计1-2分钟。由于同步的问题，日志输出较慢）\n");
            try {
                serverPort = Integer.parseInt(cacheServerPort);
                executorService.execute(() -> {
                    try {
                        sftpUtil = new SftpUtil(userName, userPassword, serverIp, serverPort);
                        ChannelSftp login = sftpUtil.login();
                        if (null != login) {
                            addTextToLog("登录服务器成功，开始上传streamlink安装文件\n");
                            File file = new File(userDirPath + "\\installstreamlink.sh");
                            FileInputStream fis = new FileInputStream(file);
                            UploadMonitor monitor = new UploadMonitor(file.length(), uploadCallBack);
                            sftpUtil.upload("/usr/local/src/", "SimplePushStreamUtil/", "installstreamlink.sh", fis, monitor);
                            addTextToLog("开始执行安装文件\n");
                            jschUtil.versouSshUtil(serverIp, userName, userPassword, serverPort);
                            jschUtil.runCmd("cd /usr/local/src/SimplePushStreamUtil/ && chmod a+x installstreamlink.sh && ./installstreamlink.sh", installStreanlinkCallBack);
                        } else {
                            addTextToLog("登录服务器失败");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        addTextToLog("streamlink安装失败\n");
                    }
                });
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
            }
        }
    }

    private InstallStreamlinkRunnable.InstallStreanlinkCallBack installStreanlinkCallBack = log -> {
        addTextToLog(log + "\n");
        if (log.contains("streamlink安装结束")) {
            if (null != jschUtil) {
                jschUtil.close();
            }
        }
    };

    private void installServer() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
        } else {
            taLog.setText("开始上传服务端文件，请耐心等待（全过程预计1-2分钟。由于同步的问题，日志输出较慢）\n");
            try {
                serverPort = Integer.parseInt(cacheServerPort);
                executorService.execute(() -> {
                    try {
                        sftpUtil = new SftpUtil(userName, userPassword, serverIp, serverPort);
                        ChannelSftp login = sftpUtil.login();
                        if (null != login) {
                            addTextToLog("登录服务器成功，开始上传linux服务文件\n");
                            FileInputStream fis = new FileInputStream(file1);
                            UploadMonitor monitor = new UploadMonitor(file1.length(), uploadCallBack);
                            sftpUtil.upload("/usr/local/src/", "SimplePushStreamUtil/", "SimplePushStreamUtil-Server.service", fis, monitor);
                            addTextToLog("第一个文件上传成功，开始上传jar包（时间比较久）\n");
                            fis = new FileInputStream(file2);
                            monitor = new UploadMonitor(file2.length(), uploadCallBack);
                            sftpUtil.upload("/usr/local/src/", "SimplePushStreamUtil/", "SimplePushStreamUtil-Server.jar", fis, monitor);
                            addTextToLog("上传完成，开始配置环境\n");
                            jschUtil.versouSshUtil(serverIp, userName, userPassword, serverPort);
                            jschUtil.runCmd("cd /usr/local/src/SimplePushStreamUtil/ && mv SimplePushStreamUtil-Server.service /etc/systemd/system/", "UTF-8");
                            jschUtil.runCmd("systemctl disable SimplePushStreamUtil-Server.service", "UTF-8");
                            jschUtil.runCmd("systemctl enable SimplePushStreamUtil-Server.service", "UTF-8");
                            jschUtil.runCmd("systemctl start SimplePushStreamUtil-Server.service", "UTF-8");
                            addTextToLog("开启服务成功");
                        } else {
                            addTextToLog("登录服务器失败");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
            }
        }
    }

    private UploadMonitor.UploadCallBack uploadCallBack = this::addTextToLog;

    /**
     * 初始化界面
     */
    private void initView() {
        rbBoth.setSelected(true);
        btnCloseLiveRoom.setEnabled(false);
        btnToMyLiveRoom.setEnabled(false);

        rbInputLiveRoomUrl.setSelected(true);
        rbInputLiveRoomUrl.addItemListener(itemListener);
        rbGetLiveRoomUrl.addItemListener(itemListener);
        contentScrollPanel.getVerticalScrollBar().setUnitIncrement(20);

        btnConnect.addActionListener(e -> serverConnect());

        btnDisconnect.addActionListener(e -> {
            if (null == minaClient) {
                showTipsDialog("请先连接服务器后再进行操作");
            } else {
                minaClient.close();
                addTextToLog("断开连接");
            }
        });

        btnSaveServerInfo.addActionListener(e -> showSaveServerInfoDialog());

        btnSaveSourceUrlInfo.addActionListener(e -> showSaveSourceUrlInfoDialog());

        btnSaveLiveRoomUrlInfo.addActionListener(e -> showSaveLiveRoomUrlInfo());

        btnGetResourceInfo.addActionListener(e -> showSourceUrlInfoDialog());

        btnLoadServerInfo.addActionListener(e -> showServerInfoDialog());

        btnGetLiveRoomInfo.addActionListener(e -> showLiveRoomUrlInfoDialog());

        btnGetFormatList.addActionListener(e -> getFormatList());

        btnPushStream.addActionListener(e -> pushStreamPerformed());

        btnStopStream.addActionListener(e -> stopPushStream());

        btnOpenLiveRoom.addActionListener(e -> openLiveRoom());

        btnCloseLiveRoom.addActionListener(e -> closeLiveRoom());

        btnToMyLiveRoom.addActionListener(e -> toMyLIveRoom());

    }

    /**
     * 初始化数据
     */
    private void initData() {
        String localData = jsonUtil.getDatafromFile(LocalDataBean.class.getSimpleName());
        if (null == localData || localData.isEmpty()) {
            localDataBean = new LocalDataBean();
            jsonUtil.saveDataToFile(LocalDataBean.class.getSimpleName(), gson.toJson(localDataBean));
        } else {
            localDataBean = gson.fromJson(localData, LocalDataBean.class);
        }
        if (null == localDataBean.getConfigSchemeBean()) {
            ConfigSchemeBean configSchemeBean = new ConfigSchemeBean();
            configSchemeBean.setSchemeType(0);
            localDataBean.setConfigSchemeBean(configSchemeBean);
            jsonUtil.saveDataToFile(LocalDataBean.class.getSimpleName(), gson.toJson(localDataBean));
        }
//        showOrHideLiveRoomControlPanel();
    }

    /**
     * 测试服务器连接
     */
    private void serverConnect() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
        } else {
            taLog.setText("开始测试连接服务器");
            try {
                if (null == minaClient) {
                    minaClient = new MinaClient(this, serverIp);
                }
                serverPort = Integer.parseInt(cacheServerPort);
                executorService.execute(() -> {
                    try {
                        minaClient.open();
                        addTextToLog("\n连接服务器成功");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        addTextToLog("\n连接服务器失败，请检查输入的信息");
                    }
                });
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
            }
        }
    }

    /**
     * 获取分辨率列表
     */
    private void getFormatList() {
        serverIp = tfServerIp.getText();
        String cacheServerPort = tfServerPort.getText();
        userName = tfUserName.getText();
        userPassword = tfUserPassword.getText();
        if (null == serverIp || serverIp.isEmpty()) {
            showTipsDialog("服务器ip不能为空");
            return;
        } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
            showTipsDialog("端口号不能为空");
            return;
        } else if (null == userName || userName.isEmpty()) {
            showTipsDialog("用户名不能为空");
            return;
        } else if (null == userPassword || userPassword.isEmpty()) {
            showTipsDialog("用户密码不能为空");
            return;
        } else {
            try {
                serverPort = Integer.parseInt(cacheServerPort);
            } catch (Exception ex) {
                showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
                return;
            }
        }
        resourceUrl = new String(taResourceUrl.getText().getBytes(), StandardCharsets.UTF_8);
        if (resourceUrl.isEmpty()) {
            showTipsDialog("请输入直播源地址后重试");
        } else {
            String message = "";
            switch (localDataBean.getConfigSchemeBean().getSchemeType()) {
                case 0:
                    message = "该地址是否需要使用youtube-dl进行解析？\n（如填入的为m3u8地址或本地视频文件地址，请选否）";
                    break;
                case 1:
                    message = "该地址是否需要使用streamlink进行解析？\n（如填入的为m3u8地址或本地视频文件地址，请选否）";
                    break;
            }

            int result = JOptionPane.showConfirmDialog(
                    MainForm.this, message, "温馨提示：",
                    JOptionPane.YES_NO_OPTION
            );
            switch (result) {
                case 0:
                    //是
                    cbFormatList.removeAllItems();
                    isLocalFile = false;
                    getFormatListInLinux();
                    break;
                case 1:
                    //否
                    cbFormatList.removeAllItems();
                    isLocalFile = true;
                    m3u8Url = resourceUrl;
                    break;
            }
        }
    }

    /**
     * 推流按钮点击
     */
    private void pushStreamPerformed() {
        if (rbGetLiveRoomUrl.isSelected()) {
            if (null == minaClient) {
                showTipsDialog("请先连接服务器后再进行操作");
            } else {
                FromClientBean fromClientBean = new FromClientBean();
                fromClientBean.setType(ParseMessageUtil.TYPE_LIVEROOMISOPEN);
                minaClient.send(fromClientBean);
            }
        } else {
            //手动填写直播间地址
            liveRoomUrl = taLiveRoomUrl.getText();
            if (null == liveRoomUrl || liveRoomUrl.isEmpty()) {
                showTipsDialog("直播间地址为空，请输入后重试。");
            } else {
                pushStream();
            }
        }
    }

    public void liveRoomIsOpenSuccess(String url) {
        liveRoomUrl = url;
        pushStream();
    }

    public void liveRoomIsOpenFail(String result) {
        showTipsDialog(result);
    }

    /**
     * 通过B站账号信息获取推流地址
     */
    private void openLiveRoom() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            FromClientBean fromClientBean = new FromClientBean();
            fromClientBean.setType(ParseMessageUtil.TYPE_OPENLIVEROOM);
            minaClient.send(fromClientBean);
        }
    }

    public void openLiveRoomFail(String result) {
        showTipsDialog(result);
    }

    public void getAreaListSuccess(List<LiveAreaListEntity> resolutionBeans) {
        if (null == areaSettingDialog) {
            areaSettingDialog = new AreaSettingDialog(this::updateTitleAndOpenLiveRoom);
        }
        areaSettingDialog.setData(resolutionBeans);
        areaSettingDialog.setVisible(true);
    }

    public void getAreaListFail(String log) {
        addTextToLog(log);
    }

    /**
     * 更新房间标题并打开直播间
     *
     * @param roomName
     * @param areaId
     */
    private void updateTitleAndOpenLiveRoom(String roomName, String areaId) {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                FromClientBean fromClientBean = new FromClientBean();
                fromClientBean.setType(ParseMessageUtil.TYPE_UPDATETITLEANDOPENLIVEROOM);
                fromClientBean.setAreaId(areaId);
                fromClientBean.setRoomName(roomName);
                minaClient.send(fromClientBean);
            });
        }
    }

    public void updateTitleAndOpenLiveRoomSuccess(String result) {
        addTextToLog(result);
        btnCloseLiveRoom.setEnabled(true);
        btnToMyLiveRoom.setEnabled(true);
    }

    public void updateTitleAndOpenLiveRoomFail(String result) {
        addTextToLog(result);
    }

    /**
     * 关闭直播间
     */
    private void closeLiveRoom() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                FromClientBean fromClientBean = new FromClientBean();
                fromClientBean.setType(ParseMessageUtil.TYPE_CLOSELIVEROOM);
                minaClient.send(fromClientBean);
            });
        }
    }

    public void closeLiveRoomSuccess(String result) {
        addTextToLog(result);
        btnCloseLiveRoom.setEnabled(false);
        btnToMyLiveRoom.setEnabled(false);
    }

    public void closeLiveRoomFail(String result) {
        addTextToLog(result);
    }

    /**
     * 打开我的直播间
     */
    private void toMyLIveRoom() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            FromClientBean fromClientBean = new FromClientBean();
            fromClientBean.setType(ParseMessageUtil.TYPE_TOMYLIVEROOM);
            minaClient.send(fromClientBean);
        }
    }

    public void toMyLIveRoomSuccess(String site) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()
                    && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(site);
                desktop.browse(uri);
            }
        } catch (IOException | URISyntaxException ex) {
            System.out.println(ex);
        }
    }

    public void toMyLIveRoomFail(String result) {
        addTextToLog(result);
    }

    /**
     * 单选按钮选中状态变化监听
     */
    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (0 == controlPanelHeight) {
                controlPanelHeight = controlPanel.getPreferredSize().height;
            }
            int liveRoomPanelHeight = liveRoomPanel.getPreferredSize().height;
            if (e.getSource() == rbInputLiveRoomUrl && rbInputLiveRoomUrl.isSelected()) {
                if (!liveRoomPanel.isVisible()) {
                    liveRoomPanel.setVisible(true);
                    controlPanel.setPreferredSize(new Dimension(0, controlPanelHeight + liveRoomPanelHeight));
                    controlPanel.setSize(0, controlPanelHeight + liveRoomPanelHeight);
                }
            } else if (e.getSource() == rbGetLiveRoomUrl && rbGetLiveRoomUrl.isSelected()) {
                if (liveRoomPanel.isVisible()) {
                    liveRoomPanel.setVisible(false);
                    controlPanel.setPreferredSize(new Dimension(0, controlPanelHeight - liveRoomPanelHeight));
                    controlPanel.setSize(0, controlPanelHeight - liveRoomPanelHeight);
                }
            }
            controlPanelHeight = controlPanel.getPreferredSize().height;
        }
    };

    /**
     * 显示提示对话框
     *
     * @param content
     */
    private void showTipsDialog(String content) {
        JOptionPane.showMessageDialog(this, content, "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 显示bilibili账号对话框
     */
    private void showBilibiliAccountDialog() {
        if (null == bilibiliAccountDialog) {
            bilibiliAccountDialog = new BiliBiliAccountDialog(this);
            bilibiliAccountDialog.pack();
        }
        bilibiliAccountDialog.setVisible(true);
    }

    /**
     * 显示配置解析方案对话框
     */
    private void showConfigSchemeDialog() {
        if (null == configSchemeDialog) {
            configSchemeDialog = new ConfigSchemeDialog(this);
            configSchemeDialog.pack();
        }
        configSchemeDialog.setVisible(true);
    }

    /**
     * 显示保存服务器信息的输入框
     */
    private void showSaveServerInfoDialog() {
        String saveName = JOptionPane.showInputDialog("请输入保存的名称");
        if (null != saveName) {
            if (saveName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "保存的名称不能为空，请输入后重试", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
            } else {
                serverIp = tfServerIp.getText();
                String cacheServerPort = tfServerPort.getText();
                userName = tfUserName.getText();
                userPassword = tfUserPassword.getText();
                if (null == serverIp || serverIp.isEmpty()) {
                    showTipsDialog("服务器ip不能为空");
                } else if (null == cacheServerPort || cacheServerPort.isEmpty()) {
                    showTipsDialog("端口号不能为空");
                } else if (null == userName || userName.isEmpty()) {
                    showTipsDialog("用户名不能为空");
                } else if (null == userPassword || userPassword.isEmpty()) {
                    showTipsDialog("用户密码不能为空");
                } else {
                    try {
                        serverPort = Integer.parseInt(cacheServerPort);
                        List<ServerInfoBean> serverInfoBeans = localDataBean.getServerInfoBeans();
                        if (null == serverInfoBeans) {
                            serverInfoBeans = new ArrayList<>();
                        }
                        ServerInfoBean serverInfoBean = new ServerInfoBean();
                        serverInfoBean.setSaveName(saveName);
                        serverInfoBean.setIp(serverIp);
                        serverInfoBean.setPort(serverPort);
                        serverInfoBean.setUserName(userName);
                        serverInfoBean.setUserPassword(userPassword);
                        serverInfoBeans.add(serverInfoBean);
                        localDataBean.setServerInfoBeans(serverInfoBeans);
                        jsonUtil.saveDataToFile(LocalDataBean.class.getSimpleName(), gson.toJson(localDataBean));
                        showTipsDialog("服务器信息保存记录成功");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showTipsDialog("您输入的端口号有误，请检查后重试！（端口号均为整数数字）");
                    }

                }
            }
        }
    }

    /**
     * 显示保存直播源信息的输入框
     */
    private void showSaveSourceUrlInfoDialog() {
        String saveName = JOptionPane.showInputDialog("请输入保存的名称");
        if (null != saveName) {
            if (saveName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "保存的名称不能为空，请输入后重试", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
            } else {
                resourceUrl = taResourceUrl.getText();
                if (resourceUrl.isEmpty()) {
                    showTipsDialog("直播源地址不能为空");
                } else {
                    List<SourceUrlInfoBean> sourceUrlInfoBeans = localDataBean.getSourceUrlInfoBeans();
                    if (null == sourceUrlInfoBeans) {
                        sourceUrlInfoBeans = new ArrayList<>();
                    }
                    SourceUrlInfoBean sourceUrlInfoBean = new SourceUrlInfoBean();
                    sourceUrlInfoBean.setSaveName(saveName);
                    sourceUrlInfoBean.setUrl(resourceUrl);
                    sourceUrlInfoBeans.add(sourceUrlInfoBean);
                    localDataBean.setSourceUrlInfoBeans(sourceUrlInfoBeans);
                    jsonUtil.saveDataToFile(LocalDataBean.class.getSimpleName(), gson.toJson(localDataBean));
                    showTipsDialog("直播源信息保存记录成功");
                }
            }
        }
    }

    /**
     * 显示保存直播间地址的输入框
     */
    private void showSaveLiveRoomUrlInfo() {
        String saveName = JOptionPane.showInputDialog("请输入保存的名称");
        if (null != saveName) {
            if (saveName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "保存的名称不能为空，请输入后重试", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
            } else {
                liveRoomUrl = taLiveRoomUrl.getText();
                if (liveRoomUrl.isEmpty()) {
                    showTipsDialog("直播间地址不能为空");
                } else {
                    List<LiveRoomUrlInfoBean> liveRoomUrlInfoBeans = localDataBean.getLiveRoomUrlInfoBeans();
                    if (null == liveRoomUrlInfoBeans) {
                        liveRoomUrlInfoBeans = new ArrayList<>();
                    }
                    LiveRoomUrlInfoBean liveRoomUrlInfoBean = new LiveRoomUrlInfoBean();
                    liveRoomUrlInfoBean.setSaveName(saveName);
                    liveRoomUrlInfoBean.setUrl(liveRoomUrl);
                    liveRoomUrlInfoBeans.add(liveRoomUrlInfoBean);
                    localDataBean.setLiveRoomUrlInfoBeans(liveRoomUrlInfoBeans);
                    jsonUtil.saveDataToFile(LocalDataBean.class.getSimpleName(), gson.toJson(localDataBean));
                    showTipsDialog("直播间信息保存记录成功");
                }
            }
        }
    }

    /**
     * 显示本地保存的服务器信息对话框
     */
    private void showServerInfoDialog() {
        if (null == serverInfoDialog) {
            serverInfoDialog = new ServerInfoDialog(this, (ip, port, userName, userPassword) -> {
                tfServerIp.setText(ip);
                tfServerPort.setText(String.valueOf(port));
                tfUserName.setText(userName);
                tfUserPassword.setText(userPassword);
            });
            serverInfoDialog.pack();
        }
        serverInfoDialog.setVisible(true);
    }

    /**
     * 显示本地保存的直播源地址信息对话框
     */
    private void showSourceUrlInfoDialog() {
        if (null == resourceUrlInfoDialog) {
            resourceUrlInfoDialog = new SourceUrlInfoDialog(this, url -> taResourceUrl.setText(url));
            resourceUrlInfoDialog.pack();
        }
        resourceUrlInfoDialog.setVisible(true);
    }

    /**
     * 显示本地保存的直播间地址信息对话框
     */
    private void showLiveRoomUrlInfoDialog() {
        if (null == liveRoomUrlInfoDialog) {
            liveRoomUrlInfoDialog = new LiveRoomUrlInfoDialog(this, url -> taLiveRoomUrl.setText(url));
            liveRoomUrlInfoDialog.pack();
        }
        liveRoomUrlInfoDialog.setVisible(true);
    }

    /**
     * linux服务器环境获取分辨率列表
     */
    private void getFormatListInLinux() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                taLog.setText("开始获取分辨率列表");
                try {
                    String cmd = "";
                    switch (localDataBean.getConfigSchemeBean().getSchemeType()) {
                        case 0:
                            cmd = "youtube-dl --list-formats " + resourceUrl;
                            break;
                        case 1:
                            cmd = "streamlink " + resourceUrl;
                            break;
                    }
                    FromClientBean fromClientBean = new FromClientBean();
                    fromClientBean.setSchemeType(localDataBean.getConfigSchemeBean().getSchemeType());
                    fromClientBean.setType(ParseMessageUtil.TYPE_GETFORMATLIST);
                    fromClientBean.setCmd(cmd);
                    minaClient.send(fromClientBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void getFormatListSuccess(List<ResolutionBean> listResolutions) {
        MainForm.this.listResolutions = listResolutions;
        cbFormatList.removeAllItems();
        for (ResolutionBean resolution : listResolutions) {
            String result = "";
            if (null != resolution.getResolutionPx() && !resolution.getResolutionPx().isEmpty()) {
                result += resolution.getResolutionPx();
            } else {
                result += "无分辨率参数";
            }
            if (null != resolution.getFps() && !resolution.getFps().isEmpty()) {
                result += " " + resolution.getFps();
            }
            cbFormatList.addItem(result);
        }
        addTextToLog("\n\n" + "获取分辨率列表成功，请选择推送分辨率，检查直播间地址是否有误，检查无误后点击开始推流按钮");
    }

    public void getFormatListFail(String errLog) {
        cbFormatList.removeAllItems();
        addTextToLog(errLog);
    }

    /**
     * 推流
     */
    private void pushStream() {
        if (isLocalFile) {
            //直推m3u8或本地视频文件
            pushStreamToLiveRoom();
        } else {
            if (0 == localDataBean.getConfigSchemeBean().getSchemeType()) {
                startGetM3u8Url();
            } else if (1 == localDataBean.getConfigSchemeBean().getSchemeType()) {
                pushStreamToLiveRoom();
            }
        }
    }

    /**
     * 推流到直播间
     */
    private void pushStreamToLiveRoom() {
        pushStreamToLiveRoomInLinux();
    }

    /**
     * linux平台推流
     */
    private void pushStreamToLiveRoomInLinux() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                try {
                    addTextToLog("\n\n开始组装推流参数即将开始推流，请稍候...");
                    String videoParams = null;
                    String cache;
                    if (rbBoth.isSelected()) {
                        videoParams = " -c:v copy -c:a aac -strict -2 -f flv ";
                    } else if (rbOnlyAudio.isSelected()) {
                        videoParams = " -vn -c:a aac -strict -2  -f flv ";
                    } else if (rbOnlyImage.isSelected()) {
                        videoParams = " -c:v copy -an -strict -2  -f flv ";
                    }
                    if (0 == localDataBean.getConfigSchemeBean().getSchemeType()) {
                        cache = "ffmpeg -thread_queue_size 1024 -i " + m3u8Url + videoParams + "\"" + liveRoomUrl + "\"";
                    } else {
                        String resolutionPx = listResolutions.get(cbFormatList.getSelectedIndex()).getResolutionPx();
                        if (resolutionPx.contains("(")) {
                            resolutionPx = resolutionPx.substring(0, resolutionPx.lastIndexOf("("));
                        }
                        cache = "streamlink -O " + resourceUrl + " " + resolutionPx + " | ffmpeg -thread_queue_size 1024 -i pipe:0 " + videoParams + "\"" + liveRoomUrl + "\"";
                    }
                    System.out.println(cache);
                    FromClientBean fromClientBean = new FromClientBean();
                    fromClientBean.setType(ParseMessageUtil.TYPE_PUSHSTREAMTOLIVEROOM);
                    fromClientBean.setCmd(cache);
                    minaClient.send(fromClientBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void pushStreamToLiveRoomSuccess(String log) {
        addTextToLog(log);
    }

    public void pushStreamToLiveRoomFail(String log) {
        addTextToLog(log);
    }

    /**
     * 开始获取m3u8地址
     */
    private void startGetM3u8Url() {
        getM3u8UrlInLinux();
    }


    /**
     * linux平台的获取m3u8地址
     */
    private void getM3u8UrlInLinux() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                addTextToLog("\n\n开始获取直播源，请稍候...");
                try {
                    String cmd;
                    if (0 == localDataBean.getConfigSchemeBean().getSchemeType()) {
                        String resolutionNo = listResolutions.get(cbFormatList.getSelectedIndex()).getResolutionNo();
                        //通过youtube-dl获取m3u8地址
                        cmd = "youtube-dl -f " + resolutionNo + " -g " + resourceUrl;
                    } else {
                        String resolutionPx = listResolutions.get(cbFormatList.getSelectedIndex()).getResolutionPx();
                        if (resolutionPx.contains("(")) {
                            resolutionPx = resolutionPx.substring(0, resolutionPx.lastIndexOf("("));
                        }
                        cmd = "streamlink --stream-url " + resourceUrl + " " + resolutionPx;
                    }
                    FromClientBean fromClientBean = new FromClientBean();
                    fromClientBean.setType(ParseMessageUtil.TYPE_GETM3U8);
                    fromClientBean.setCmd(cmd);
                    minaClient.send(fromClientBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void getM3u8UrlSuccess(String m3u8Url) {
        MainForm.this.m3u8Url = m3u8Url;
        addTextToLog("\n\n获取直播源成功");
        pushStreamToLiveRoom();
    }

    public void getM3u8UrlFail(String errLog) {
        addTextToLog(errLog);
    }

    /**
     * linux平台的停止推流
     */
    private void stopPushStream() {
        if (null == minaClient) {
            showTipsDialog("请先连接服务器后再进行操作");
        } else {
            executorService.execute(() -> {
                FromClientBean fromClientBean = new FromClientBean();
                fromClientBean.setType(ParseMessageUtil.TYPE_STOPPUSHSTREAM);
                minaClient.send(fromClientBean);
            });
        }
    }

    public void stopPushStreamSuccess(String result) {
        addTextToLog(result);
    }

    /**
     * 设置日志
     *
     * @param log
     */
    private void addTextToLog(String log) {
        if (taLog.getText().length() > 8000) {
            taLog.setText(log);
        } else {
            taLog.append(log);
        }
        taLog.selectAll();
    }

    public LocalDataBean getLocalDataBean() {
        return localDataBean;
    }

    public JsonUtil getJsonUtil() {
        return jsonUtil;
    }

    public Gson getGson() {
        return gson;
    }

    public MinaClient getMinaClient() {
        return minaClient;
    }

    public BiliBiliAccountDialog getBilibiliAccountDialog() {
        return bilibiliAccountDialog;
    }

    private JPanel panel1;
    private JTextArea taLog;
    private JTextField tfServerIp;
    private JTextField tfServerPort;
    private JTextField tfUserName;
    private JTextField tfUserPassword;
    private JButton btnConnect;
    private JButton btnSaveServerInfo;
    private JButton btnLoadServerInfo;
    private JButton btnSaveSourceUrlInfo;
    private JButton btnGetResourceInfo;
    private JTextArea taResourceUrl;
    private JComboBox cbFormatList;
    private JRadioButton rbInputLiveRoomUrl;
    private JRadioButton rbGetLiveRoomUrl;
    private JButton btnSaveLiveRoomUrlInfo;
    private JButton btnGetLiveRoomInfo;
    private JRadioButton rbOnlyAudio;
    private JRadioButton rbOnlyImage;
    private JButton btnOpenLiveRoom;
    private JButton btnCloseLiveRoom;
    private JButton btnToMyLiveRoom;
    private JButton btnGetFormatList;
    private JButton btnPushStream;
    private JButton btnStopStream;
    private JPanel controlPanel;
    private JPanel liveRoomPanel;
    private JScrollPane contentScrollPanel;
    private JTextArea taLiveRoomUrl;
    private JRadioButton rbBoth;
    private JButton btnDisconnect;
    private JPanel serverInfoPanel;
    private JPanel liveRoomControlPanel;
}
