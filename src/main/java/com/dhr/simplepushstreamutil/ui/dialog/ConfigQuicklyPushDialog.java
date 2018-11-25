package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.ConfigQuicklyPushBean;
import com.dhr.simplepushstreamutil.bean.LocalDataBean;
import com.dhr.simplepushstreamutil.bean.ResolutionBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfigQuicklyPushDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton btnLiveArea;
    private JComboBox cbResolution;
    private JLabel tfAreaAndRoomName;
    private MainForm mainForm;
    private String roomName;
    private String liveArea;
    private ConfigQuicklyPushBean configQuicklyPushBean;

    public ConfigQuicklyPushDialog(MainForm mainForm) {
        this.mainForm = mainForm;
        setContentPane(contentPane);
        setSize(350, 230);
        setPreferredSize(new Dimension(350, 230));
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

        initView();
    }

    private void initView() {
        cbResolution.removeAllItems();
        cbResolution.addItem("640x360");
        cbResolution.addItem("854x480");
        cbResolution.addItem("1280x720");
        cbResolution.addItem("1920x1080");

        btnLiveArea.addActionListener(e -> mainForm.getAreaList());
        configQuicklyPushBean = mainForm.getLocalDataBean().getConfigQuicklyPushBean();
        if (null == configQuicklyPushBean) {
            configQuicklyPushBean = new ConfigQuicklyPushBean();
        } else {
            liveArea = configQuicklyPushBean.getAreaId();
            roomName = configQuicklyPushBean.getRoomName();
            for (int i = 0; i < cbResolution.getItemCount() - 1; i++) {
                if (configQuicklyPushBean.getResolution().equals(cbResolution.getItemAt(i).toString())) {
                    cbResolution.setSelectedIndex(i);
                }
            }
            tfAreaAndRoomName.setText("分区id：" + liveArea + "  房间标题：" + roomName);
        }

    }

    public void updateData(String liveArea, String roomName) {
        this.liveArea = liveArea;
        this.roomName = roomName;
        tfAreaAndRoomName.setText("分区id：" + liveArea + "  房间标题：" + roomName);
    }

    private void onOK() {
        if (null == roomName || roomName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入房间标题");
            return;
        }
        if (null == liveArea || liveArea.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择分区");
            return;
        }
        configQuicklyPushBean.setAreaId(liveArea);
        configQuicklyPushBean.setRoomName(roomName);
        configQuicklyPushBean.setResolution(cbResolution.getSelectedItem().toString());
        mainForm.getLocalDataBean().setConfigQuicklyPushBean(configQuicklyPushBean);
        mainForm.getJsonUtil().saveDataToFile(LocalDataBean.class.getSimpleName(), mainForm.getGson().toJson(mainForm.getLocalDataBean()));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
