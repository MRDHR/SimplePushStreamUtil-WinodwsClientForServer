package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.LocalDataBean;
import com.dhr.simplepushstreamutil.bean.SourceUrlInfoBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.dhr.simplepushstreamutil.util.JsonUtil;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SourceUrlInfoDialog extends JDialog {
    private MainForm mainForm;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList listSourceUrlInfo;
    private JButton btnRemove;
    private List<SourceUrlInfoBean> sourceUrlInfoBeans;
    private CallBack callBack;
    private LocalDataBean localDataBean;

    public SourceUrlInfoDialog(MainForm mainForm, CallBack callBack) {
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
                remove();
            }
        });
    }

    private void loadDataFromJson() {
        localDataBean = mainForm.getLocalDataBean();
        sourceUrlInfoBeans = localDataBean.getSourceUrlInfoBeans();
        if (null == sourceUrlInfoBeans) {
            sourceUrlInfoBeans = new ArrayList<>();
        }
        listSourceUrlInfo.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        for (SourceUrlInfoBean bean : sourceUrlInfoBeans) {
            listModel.addElement(bean.getSaveName());
        }
        listSourceUrlInfo.setModel(listModel);
        if (null != sourceUrlInfoBeans && !sourceUrlInfoBeans.isEmpty()) {
            listSourceUrlInfo.setSelectedIndex(0);
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
        int selectedIndex = listSourceUrlInfo.getSelectedIndex();
        if (selectedIndex >= 0) {
            if (!sourceUrlInfoBeans.isEmpty()) {
                sourceUrlInfoBeans.remove(selectedIndex);
                localDataBean.setSourceUrlInfoBeans(sourceUrlInfoBeans);
                mainForm.getJsonUtil().saveDataToFile(LocalDataBean.class.getSimpleName(), mainForm.getGson().toJson(localDataBean));
                loadDataFromJson();
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要删除的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onOK() {
        int selectedIndex = listSourceUrlInfo.getSelectedIndex();
        if (selectedIndex >= 0) {
            SourceUrlInfoBean resourceUrlInfoBean = sourceUrlInfoBeans.get(selectedIndex);
            callBack.confirm(resourceUrlInfoBean.getUrl());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "请选择需要提取的记录", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}
