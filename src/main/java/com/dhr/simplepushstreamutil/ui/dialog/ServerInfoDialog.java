package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.LocalDataBean;
import com.dhr.simplepushstreamutil.bean.ServerInfoBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.dhr.simplepushstreamutil.util.JsonUtil;
import com.google.gson.Gson;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ServerInfoDialog extends JDialog {
    private MainForm mainForm;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton btnRemove;
    private JList listServerInfo;
    private List<ServerInfoBean> serverInfoBeans;
    private CallBack callBack;
    private LocalDataBean localDataBean;

    public ServerInfoDialog(MainForm mainForm, CallBack callback) {
        this.mainForm = mainForm;
        this.callBack = callback;
        setContentPane(contentPane);
        setSize(330, 320);
        setPreferredSize(new Dimension(330, 320));
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });
    }

    private void loadDataFromJson() {
        localDataBean = mainForm.getLocalDataBean();
        serverInfoBeans = this.localDataBean.getServerInfoBeans();
        if (null == serverInfoBeans) {
            serverInfoBeans = new ArrayList<>();
        }
        listServerInfo.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        for (ServerInfoBean bean : serverInfoBeans) {
            listModel.addElement(bean.getSaveName());
        }
        listServerInfo.setModel(listModel);
        if (null != serverInfoBeans && !serverInfoBeans.isEmpty()) {
            listServerInfo.setSelectedIndex(0);
        }
    }

    @Override
    public void setVisible(boolean b) {
        loadDataFromJson();
        super.setVisible(b);
    }

    private void remove() {
        int selectedIndex = listServerInfo.getSelectedIndex();
        if (selectedIndex >= 0) {
            if (!serverInfoBeans.isEmpty()) {
                serverInfoBeans.remove(selectedIndex);
                localDataBean.setServerInfoBeans(serverInfoBeans);
                mainForm.getJsonUtil().saveDataToFile(LocalDataBean.class.getSimpleName(), mainForm.getGson().toJson(localDataBean));
                loadDataFromJson();
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要删除的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public interface CallBack {
        void confirm(String ip, int port, String userName, String userPassword);
    }

    private void onOK() {
        int selectedIndex = listServerInfo.getSelectedIndex();
        if (selectedIndex >= 0) {
            ServerInfoBean serverInfoBean = serverInfoBeans.get(selectedIndex);
            callBack.confirm(serverInfoBean.getIp(), serverInfoBean.getPort(), serverInfoBean.getUserName(), serverInfoBean.getUserPassword());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要提取的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}
