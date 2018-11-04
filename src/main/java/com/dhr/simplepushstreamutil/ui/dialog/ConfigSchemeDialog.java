package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.ConfigSchemeBean;
import com.dhr.simplepushstreamutil.bean.LocalDataBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfigSchemeDialog extends JDialog {
    private MainForm mainForm;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton rbYoutubeDl;
    private JRadioButton rbStreamLink;
    private ConfigSchemeBean configSchemeBean;

    public ConfigSchemeDialog(MainForm mainForm) {
        this.mainForm = mainForm;
        setContentPane(contentPane);
        setSize(242, 200);
        setPreferredSize(new Dimension(242, 200));
        setLocationRelativeTo(null);
        setModal(true);
        setTitle("配置解析方案");
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
        configSchemeBean = mainForm.getLocalDataBean().getConfigSchemeBean();
        int schemeType = configSchemeBean.getSchemeType();
        switch (schemeType) {
            case 0:
                rbYoutubeDl.setSelected(true);
                break;
            case 1:
                rbStreamLink.setSelected(true);
                break;
        }
    }

    private void onOK() {
        configSchemeBean.setSchemeType(rbYoutubeDl.isSelected() ? 0 : 1);
        mainForm.getLocalDataBean().setConfigSchemeBean(configSchemeBean);
        mainForm.getJsonUtil().saveDataToFile(LocalDataBean.class.getSimpleName(), mainForm.getGson().toJson(mainForm.getLocalDataBean()));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
