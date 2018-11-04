package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.bean.FromClientBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.dhr.simplepushstreamutil.util.ParseMessageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BiliBiliAccountDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnRemove;
    private JButton buttonCancel;
    private JButton btnSave;
    private JButton btnTestLogin;
    private JTextField tfUserName;
    private JTextField tfPassword;

    private MainForm mainForm;
    private String userName;
    private String password;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public BiliBiliAccountDialog(MainForm mainForm) {
        this.mainForm = mainForm;
        setContentPane(contentPane);
        setSize(330, 200);
        setPreferredSize(new Dimension(330, 200));
        setLocationRelativeTo(null);
        setModal(true);
        setTitle("配置B站账号");
        getRootPane().setDefaultButton(btnRemove);
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnTestLogin.addActionListener(e -> {
            userName = tfUserName.getText();
            password = tfPassword.getText();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        BiliBiliAccountDialog.this,
                        "账号不能为空，请输入后重试",
                        "温馨提示：",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        BiliBiliAccountDialog.this,
                        "密码不能为空，请输入后重试",
                        "温馨提示：",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                testLogin();
            }
        });
        btnSave.addActionListener(e -> saveLoginInfo());
        btnRemove.addActionListener(e -> removeLoginInfo());
    }

    private void testLogin() {
        executorService.execute(() -> {
            FromClientBean fromClientBean = new FromClientBean();
            fromClientBean.setType(ParseMessageUtil.TYPE_LOGIN);
            fromClientBean.setUserName(userName);
            fromClientBean.setPassword(password);
            mainForm.getMinaClient().send(fromClientBean);
        });
    }

    public void loginSuccess(String result) {
        JOptionPane.showMessageDialog(
                BiliBiliAccountDialog.this,
                result, "温馨提示：",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void loginFail(String result) {
        JOptionPane.showMessageDialog(
                BiliBiliAccountDialog.this,
                result, "温馨提示：",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveLoginInfo() {
        FromClientBean fromClientBean = new FromClientBean();
        fromClientBean.setType(ParseMessageUtil.TYPE_SAVELOGININFO);
        mainForm.getMinaClient().send(fromClientBean);
    }

    public void saveLoginInfoSuccess(String result) {
        JOptionPane.showMessageDialog(
                BiliBiliAccountDialog.this,
                result, "温馨提示：",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void saveLoginInfoFail(String result) {
        JOptionPane.showMessageDialog(
                BiliBiliAccountDialog.this,
                result, "温馨提示：",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void removeLoginInfo() {
        FromClientBean fromClientBean = new FromClientBean();
        fromClientBean.setType(ParseMessageUtil.TYPE_REMOVELOGININFO);
        mainForm.getMinaClient().send(fromClientBean);
    }

    public void removeLoginInfoSuccess(String result) {
        JOptionPane.showMessageDialog(
                this,
                result, "温馨提示：",
                JOptionPane.INFORMATION_MESSAGE
        );
//        mainForm.showOrHideLiveRoomControlPanel();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
