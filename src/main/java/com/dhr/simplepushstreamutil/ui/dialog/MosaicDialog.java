package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.ui.form.MainForm;

import javax.swing.*;
import java.awt.event.*;

public class MosaicDialog extends JDialog {
    private MainForm mainForm;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfLeftRight;
    private JTextField tfBlurWidth;
    private JTextField tfBlurHeight;
    private JTextField tfTopBottom;
    private JRadioButton rbLeftTop;
    private JRadioButton rbRightTop;
    private JRadioButton rbLeftBottom;
    private JRadioButton rbRightBottom;

    public MosaicDialog(MainForm mainForm) {
        this.mainForm = mainForm;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);
        setLocationRelativeTo(null);
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

        rbLeftTop.setSelected(true);
    }

    private void onOK() {
        // add your code here
        int blurWidth;
        int blurHeight;
        int x = 0;
        int y = 0;
        try {
            int leftRight = Integer.parseInt(tfLeftRight.getText());
            int topBottom = Integer.parseInt(tfTopBottom.getText());
            blurWidth = Integer.parseInt(tfBlurWidth.getText());
            blurHeight = Integer.parseInt(tfBlurHeight.getText());
            if (rbLeftTop.isSelected()) {
                x = leftRight;
                y = topBottom;
            } else if (rbRightTop.isSelected()) {
                x = 854 - blurWidth - leftRight;
                y = topBottom;
            } else if (rbLeftBottom.isSelected()) {
                x = leftRight;
                y = 480 - blurHeight - topBottom;
            } else if (rbRightBottom.isSelected()) {
                x = 854 - blurWidth - leftRight;
                y = 480 - blurHeight - topBottom;
            }
            mainForm.startPushStream(blurWidth, blurHeight, x, y);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "填写的数字格式不正确，请重新填写", "温馨提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
