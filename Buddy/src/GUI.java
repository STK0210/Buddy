import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @auther keyuli
 */
public class GUI extends JFrame {

    public static int process_sum = 20;
    public static int blockgroup_sum = 10;

    private JPanel contentPane;
    private JTextField textField_pageSize;
    private JTextField textField_memorySize;

    private JButton button_auto;
    private JTabbedPane tabbedPane;
    private boolean flag1 = false;
    private boolean flag2 = false;

    public static JPanel panel_memory;
    public static JPanel panel_process;
    public static JTextArea textArea_request;
    public static JTextArea textArea_release;

    static JLabel[] jLabels_process = new JLabel[process_sum];
    static JLabel[] jLabels_memory = new JLabel[process_sum];

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public GUI() {
        setResizable(false);
        setTitle("伙伴堆算法展示");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 150, 1313, 722);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label_memory = new JLabel("内存使用情况");
        label_memory.setFont(new Font("宋体", Font.PLAIN, 21));
        label_memory.setBounds(60, 78, 126, 42);
        contentPane.add(label_memory);

        panel_memory = new JPanel();
        panel_memory.setLayout(null);
        panel_memory.setBounds(200, 42, 1024, 117);
        panel_memory.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.add(panel_memory);


        JLabel label_process = new JLabel("进程列表");
        label_process.setFont(new Font("宋体", Font.PLAIN, 17));
        label_process.setBounds(89, 190, 82, 42);
        contentPane.add(label_process);

        panel_process = new JPanel();
        panel_process.setLayout(null);
        panel_process.setBounds(200, 191, 800, 42);
        panel_process.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.add(panel_process);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(BorderFactory.createLineBorder(Color.black));
        tabbedPane.setBounds(24, 266, 285, 408);
        contentPane.add(tabbedPane);

        JLabel label_request = new JLabel("进程申请情况");
        label_request.setFont(new Font("宋体", Font.PLAIN, 15));
        label_request.setBounds(475, 271, 106, 23);
        contentPane.add(label_request);

        JScrollPane scrollPane_request = new JScrollPane();
        scrollPane_request.setBounds(349, 295, 342, 270);
        contentPane.add(scrollPane_request);

        textArea_request = new JTextArea();
        textArea_request.setEditable(false);
        textArea_request.setText("");
        scrollPane_request.setViewportView(textArea_request);

        JLabel label_release = new JLabel("进程释放情况");
        label_release.setFont(new Font("宋体", Font.PLAIN, 15));
        label_release.setBounds(846, 271, 106, 23);
        contentPane.add(label_release);

        JScrollPane scrollPane_release = new JScrollPane();
        scrollPane_release.setBounds(719, 295, 342, 270);
        contentPane.add(scrollPane_release);

        textArea_release = new JTextArea();
        textArea_release.setText("");
        scrollPane_release.setViewportView(textArea_release);
        textArea_release.setEditable(false);

        ButtonGroup buttongroup = new ButtonGroup();

        JTextPane textPane_tips = new JTextPane();
        textPane_tips.setFont(new Font("宋体", Font.PLAIN, 15));
        textPane_tips.setEditable(false);
        textPane_tips.setText(
                "                                          说明：\r\n1.内存使用情况显示的是内存空间中的实际情况，块显示的位置，即进程占用的内存位置，以颜色区分。\r\n2.进程列表显示的是进程产生与释放的过程，如果阻塞，则不会在内存空间中显示，会存在于进程列表中。\r\n3.进程和数据均为随机生成。");
        textPane_tips.setBounds(349, 596, 712, 78);
        contentPane.add(textPane_tips);

        JRadioButton radioButton_radio = new JRadioButton("选项输入");
        radioButton_radio.setFont(new Font("宋体", Font.PLAIN, 15));
        radioButton_radio.setBounds(1147, 250, 113, 27);
        buttongroup.add(radioButton_radio);
        contentPane.add(radioButton_radio);

        JRadioButton radioButton_user = new JRadioButton("手动输入");
        radioButton_user.setFont(new Font("宋体", Font.PLAIN, 15));
        radioButton_user.setBounds(1147, 282, 113, 27);
        buttongroup.add(radioButton_user);
        contentPane.add(radioButton_user);

        JLabel label_memorySize = new JLabel("内存大小：");
        label_memorySize.setFont(new Font("宋体", Font.PLAIN, 15));
        label_memorySize.setBounds(1110, 395, 82, 18);
        contentPane.add(label_memorySize);

        JComboBox comboBox_memorySize = new JComboBox();
        comboBox_memorySize.setBounds(1184, 375, 61, 24);
        comboBox_memorySize.addItem("256");
        comboBox_memorySize.addItem("512");

        contentPane.add(comboBox_memorySize);

        JLabel unit_1 = new JLabel("M");
        unit_1.setFont(new Font("宋体", Font.PLAIN, 17));
        unit_1.setBounds(1247, 378, 13, 18);
        contentPane.add(unit_1);

        textField_memorySize = new JTextField();
        textField_memorySize.setColumns(10);
        textField_memorySize.setBounds(1184, 412, 61, 24);
        contentPane.add(textField_memorySize);

        JLabel unit_2 = new JLabel("M");
        unit_2.setFont(new Font("宋体", Font.PLAIN, 17));
        unit_2.setBounds(1247, 415, 13, 18);
        contentPane.add(unit_2);

        JLabel label_pageSize = new JLabel("页框大小：");
        label_pageSize.setFont(new Font("宋体", Font.PLAIN, 15));
        label_pageSize.setBounds(1110, 480, 82, 18);
        contentPane.add(label_pageSize);

        JComboBox comboBox_pageSize = new JComboBox<>();
        comboBox_pageSize.setBounds(1184, 460, 61, 24);
        comboBox_pageSize.addItem("1");
        comboBox_pageSize.addItem("2");
        comboBox_pageSize.addItem("4");
        contentPane.add(comboBox_pageSize);

        JLabel unit_3 = new JLabel("K");
        unit_3.setFont(new Font("宋体", Font.PLAIN, 17));
        unit_3.setBounds(1247, 462, 13, 18);
        contentPane.add(unit_3);

        textField_pageSize = new JTextField();
        textField_pageSize.setColumns(10);
        textField_pageSize.setBounds(1184, 497, 61, 24);
        contentPane.add(textField_pageSize);

        JLabel unit_4 = new JLabel("K");
        unit_4.setFont(new Font("宋体", Font.PLAIN, 17));
        unit_4.setBounds(1247, 499, 13, 18);
        contentPane.add(unit_4);

        JButton button_pause = new JButton("暂停");
        button_pause.setBounds(1099, 554, 83, 27);
        contentPane.add(button_pause);

        button_pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag1 = true;
            }
        });

        JButton button_goon = new JButton("继续");
        button_goon.setBounds(1099, 596, 83, 27);
        contentPane.add(button_goon);

        button_goon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag1 = false;
            }
        });

        JButton button_next = new JButton("下一步");
        button_next.setBounds(1196, 576, 83, 27);
        contentPane.add(button_next);

        button_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag1 = false;
                flag2 = true;
            }
        });

        button_auto = new JButton("自动运行");
        button_auto.setBounds(1132, 647, 113, 27);
        contentPane.add(button_auto);
        button_auto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button_auto) {
                    int memory_size = 0;
                    int pageframe_size = 0;
                    if (!radioButton_radio.isSelected()) {
                        if (!radioButton_user.isSelected()) {
                            return;
                        }
                    }
                    if (radioButton_radio.isSelected()) {
                        memory_size = Integer.parseInt(comboBox_memorySize.getSelectedItem().toString()) * 1024;
                        pageframe_size = Integer.parseInt(comboBox_pageSize.getSelectedItem().toString());
                        Test(memory_size, pageframe_size, 10);
                    }
                    if (radioButton_user.isSelected()) {
                        memory_size = Integer.parseInt(textField_memorySize.getText()) * 1024;
                        pageframe_size = Integer.parseInt(textField_pageSize.getText());
                        Test(memory_size, pageframe_size, 10);
                    }
                }
            }
        });

        setVisible(true);
    }

    public void Test(int memory_size, int pageframe_size, int blockgroup_size) {
        new Thread(new Runnable() {
            public void run() {
                tabbedPane.removeAll();
                button_auto.setEnabled(false);

                int nowtime = 0;

                Buddy buddy = new Buddy(memory_size, pageframe_size, blockgroup_sum);
                buddy.initialize_Buddy();
                List<Process> process_list = new Process().initialize_Process(process_sum, pageframe_size, blockgroup_size);//所有进程
                List<Process> running_list = new ArrayList<>();//进入内存执行中的进程

                textArea_request.setText("");
                textArea_release.setText("");
                addText_request("TIME:" + nowtime + "\n");
                addText_release("TIME:" + nowtime + "\n");

                JScrollPane scrollPane_init = new JScrollPane();
                tabbedPane.addTab("初始", null, scrollPane_init, null);
                JTextArea textArea_init = new JTextArea(buddy.show_all2());
                scrollPane_init.setViewportView(textArea_init);
                repaint();


                while (true) {
                    if (flag1 == true) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    for (int i = 0; i < process_list.size(); i++) {
                        Process process = process_list.get(i);
                        if (jLabels_process[process.process_ID] == null) {
                            jLabels_process[process.process_ID] = add_processlabel(process);
                        }
                        if (buddy.Request(process)) {
                            process.arrive_time = nowtime;//记录该进程开始得到内存的时间
                            process_list.remove(i);

                            running_list.add(process);
                            addText_request("        进程" + process.process_ID + "申请成功，开始执行" + "\t需要运行时间：" + process.execute_time + "\n");
                            //buddy.show_all();
                            break;
                        } else {
                            addText_request("        进程" + process.process_ID + "申请失败\n");
                        }
                    }
                    for (int i = 0; i < running_list.size(); i++) {
                        Process process = running_list.get(i);
                        if (process.execute_time <= nowtime - process.arrive_time) { // 如果有一个进程所需的内存的时间结束
                            buddy.Release(process); // 释放内存
                            addText_release("        进程" + process.process_ID + "释放成功，标签消失\n");
                            //buddy.show_all(); // 打印出当前的内存状况
                            running_list.remove(i); // 从运行队列中删掉该进程
                            i = 0; //重新扫描
                        }
                    }
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    addText_state(nowtime, buddy);
                    nowtime++;
                    addText_request("TIME:" + nowtime + "\n");
                    addText_release("TIME:" + nowtime + "\n");

                    if (running_list.size() == 0 && process_list.size() == 0) {
                        addText_request("全部进程执行完毕！");
                        addText_release("全部进程释放完毕！");
                        button_auto.setEnabled(true);
                        for (int i = 0; i < process_sum; i++) {
                            jLabels_process[i] = null;
                            jLabels_memory[i] = null;
                        }
                        break;
                    }
                    if (flag2 == true) {
                        flag1 = true;
                        flag2 = false;
                    }
                }
            }
        }).start();
    }

    public JLabel add_processlabel(Process process) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JLabel lblNewLabel = new JLabel("ID:" + process.process_ID);
        lblNewLabel.setBounds(process.process_ID * (800 / process_sum), 0, 800 / process_sum, 42);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setOpaque(true);
        lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        lblNewLabel.setBackground(new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
        process.background = lblNewLabel.getBackground();
        panel_process.add(lblNewLabel);
        repaint();
        return lblNewLabel;
    }

    public void addText_state(int nowtime, Buddy buddy) {
        JScrollPane scrollPane = new JScrollPane();
        tabbedPane.addTab("" + nowtime, null, scrollPane, null);
        JTextArea textArea = new JTextArea(buddy.show_all2());
        scrollPane.setViewportView(textArea);
        repaint();
    }

    public void addText_release(String string) {
        GUI.textArea_release.setCaretPosition(GUI.textArea_release.getDocument().getLength());
        GUI.textArea_release.append(string);
    }

    public void addText_request(String string) {
        GUI.textArea_request.setCaretPosition(GUI.textArea_request.getDocument().getLength());
        GUI.textArea_request.append(string);
    }
}