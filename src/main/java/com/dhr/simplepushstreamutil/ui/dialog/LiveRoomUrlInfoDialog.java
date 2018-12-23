package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.LiveRoomUrlInfoBean;
import com.dhr.simplepushstreamutil.bean.LocalDataBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.dhr.simplepushstreamutil.util.JsonUtil;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LiveRoomUrlInfoDialog extends JDialog {
    private MainForm mainForm;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton btnRemove;
    private JList listLiveRoom;
    private List<LiveRoomUrlInfoBean> liveRoomUrlInfoBeans;
    private CallBack callBack;
    private LocalDataBean localDataBean;

    public LiveRoomUrlInfoDialog(MainForm mainForm, CallBack callBack) {
        this.mainForm = mainForm;
        this.callBack = callBack;
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
                int result = JOptionPane.showConfirmDialog(LiveRoomUrlInfoDialog.this, "是否删除该条数据？", "温馨提示", JOptionPane.YES_NO_OPTION);
                switch (result) {
                    case 0:
                        //是
                        remove();
                        break;
                    case 1:
                        //否
                        break;
                }
            }
        });
    }

    private void loadDataFromJson() {
        localDataBean = mainForm.getLocalDataBean();
        liveRoomUrlInfoBeans = localDataBean.getLiveRoomUrlInfoBeans();
        if (null == liveRoomUrlInfoBeans) {
            liveRoomUrlInfoBeans = new ArrayList<>();
        }
        listLiveRoom.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        for (LiveRoomUrlInfoBean bean : liveRoomUrlInfoBeans) {
            listModel.addElement(bean.getSaveName());
        }
        listLiveRoom.setModel(listModel);
        if (null != liveRoomUrlInfoBeans && !liveRoomUrlInfoBeans.isEmpty()) {
            listLiveRoom.setSelectedIndex(0);
        }
    }

    @Override
    public void setVisible(boolean b) {
        loadDataFromJson();
        super.setVisible(b);
    }

    public interface CallBack {
        void confirm(String url);
    }

    private void remove() {
        int selectedIndex = listLiveRoom.getSelectedIndex();
        if (selectedIndex >= 0) {
            if (!liveRoomUrlInfoBeans.isEmpty()) {
                liveRoomUrlInfoBeans.remove(selectedIndex);
                localDataBean.setLiveRoomUrlInfoBeans(liveRoomUrlInfoBeans);
                mainForm.getJsonUtil().saveDataToFile(LocalDataBean.class.getSimpleName(), mainForm.getGson().toJson(localDataBean));
                loadDataFromJson();
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要删除的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onOK() {
        int selectedIndex = listLiveRoom.getSelectedIndex();
        if (selectedIndex >= 0) {
            LiveRoomUrlInfoBean liveRoomUrlInfoBean = liveRoomUrlInfoBeans.get(selectedIndex);
            callBack.confirm(liveRoomUrlInfoBean.getUrl());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要提取的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
