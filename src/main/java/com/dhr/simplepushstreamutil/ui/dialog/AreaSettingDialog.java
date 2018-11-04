package com.dhr.simplepushstreamutil.ui.dialog;

import com.dhr.simplepushstreamutil.entity.LiveAreaListEntity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AreaSettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfRoomName;
    private JTree treeArea;
    private CallBack callBack;
    private List<LiveAreaListEntity> data;

    public AreaSettingDialog(CallBack callBack) {
        this.callBack = callBack;
        setContentPane(contentPane);
        setSize(330, 320);
        setPreferredSize(new Dimension(330, 320));
        setLocationRelativeTo(null);
        setModal(true);
        setTitle("分区和标题");
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
    }

    public void setData(List<LiveAreaListEntity> data) {
        this.data = data;
        treeArea.removeAll();
        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("分区列表");
        for (LiveAreaListEntity dataBean : data) {
            // 创建二级节点
            DefaultMutableTreeNode secondNode = new DefaultMutableTreeNode(dataBean.getName());
            for (LiveAreaListEntity.ListBean listBean : dataBean.getList()) {
                // 创建三级节点
                DefaultMutableTreeNode thirdNode = new DefaultMutableTreeNode(listBean.getName());
                secondNode.add(thirdNode);
            }
            rootNode.add(secondNode);
        }
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        treeArea.setModel(model);
        // 设置树显示根节点句柄
        treeArea.setShowsRootHandles(false);
        // 设置树节点可编辑
        treeArea.setEditable(false);
    }

    private void onOK() {
        String roomName = tfRoomName.getText();
        if (roomName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "房间标题为空，请输入房间标题后重试",
                    "温馨提示：",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeArea
                    .getLastSelectedPathComponent();
            if (!treeNode.isLeaf()) {
                JOptionPane.showMessageDialog(
                        this,
                        "您选择的分区不是最底层分区，须选择最底层的分区",
                        "温馨提示：",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                String parentName = treeNode.getParent().toString();
                String currentName = treeNode.toString();
                for (LiveAreaListEntity dataBean : data) {
                    if (parentName.equals(dataBean.getName())) {
                        for (LiveAreaListEntity.ListBean listBean : dataBean.getList()) {
                            if (currentName.equals(listBean.getName())) {
                                callBack.callBack(roomName, listBean.getId());
                                dispose();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public interface CallBack {
        void callBack(String roomName, String areaId);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
