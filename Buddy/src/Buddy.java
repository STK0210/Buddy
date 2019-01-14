import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.pow;

/**
 * @author keyuli
 **/
public class Buddy {
    private int memory_size = 0;//内存大小
    private int pageframe_size = 0;//页框的大小
    private int blockgroup_sum = 0;//块组的个数

    private int max_block_size = 0;//最大的块大小
    private int freememory_size = 0;//自由空间的内存大小
    private char[] freemomory_address;//自由空间的实际物理位置

    public Map<Integer, List> FreeMemory = new HashMap<>();

    Buddy(int memory, int pageframe, int blockgroup) {
        this.memory_size = memory;
        this.pageframe_size = pageframe;
        this.blockgroup_sum = blockgroup;
        this.max_block_size = pageframe_size * (int) pow(2, blockgroup_sum - 1);
        this.freememory_size = this.max_block_size;
        freemomory_address = new char[freememory_size];
    }

    /**
     * 初始化所有块组
     */
    public void initialize_Buddy() {
        if (memory_size <= 0 || pageframe_size <= 0 || blockgroup_sum <= 0) {
            addText_request("输入数据错误！\n");
            return;
        }
        if (max_block_size > memory_size) {
            addText_request("分块过多，系统内存不支持！\n");
            return;
        }
        if (max_block_size < memory_size) {
            addText_request("生成成功，但不是完美情况。(不影响操作，错误原因可能为：a.块组数目较少 b.页框大小设置较小 c.内存物理空间较大)\n");
        }
        if (max_block_size == memory_size) {
            addText_request("生成成功！\n");
        }

        for (int i = 0; i < blockgroup_sum; i++) {//初始化map，以及所有的区块list，给最大的块一个初始值
            List<int[]> list = new ArrayList<int[]>();
            FreeMemory.put(block_name((int) pow(2, i)), list);
        }
        for (int i = 0; i < freemomory_address.length; i++) {
            freemomory_address[i] = '_';
        }
        FreeMemory.get(new Integer((int) Math.pow(2, blockgroup_sum - 1) * pageframe_size)).add(new int[]{0, freememory_size - 1});
    }

    /**
     * @param pageframe_sum 块中页框的总数
     * @return 返回当前换算单位下一个块的总大小
     */
    public int block_name(int pageframe_sum) {
        return pageframe_sum * pageframe_size;
    }

    /**
     * 显示所有的信息
     */
    public void show_all() {
        for (int i = 0; i < blockgroup_sum; i++) {
            List<int[]> temp = FreeMemory.get((int) pow(2, i) * pageframe_size);
            System.out.println("大小为" + (int) pow(2, i) * pageframe_size + "的地址：");
            for (int j = 0; j < temp.size(); j++) {
                System.out.println("第" + (j + 1) + "块为" + "\tx： " + temp.get(j)[0] + "\ty: " + temp.get(j)[1]);
            }
            System.out.println();
        }
    }

    public String show_all2() {
        String string = "";
        for (int i = 0; i < blockgroup_sum; i++) {
            List<int[]> temp = FreeMemory.get((int) pow(2, i) * pageframe_size);
            string += "大小为" + (int) pow(2, i) * pageframe_size + "的块：\n";
            for (int j = 0; j < temp.size(); j++) {
                string += "第" + (j + 1) + "块地址为" + "\tx： " + temp.get(j)[0] + "\ty: " + temp.get(j)[1]+"\n";
            }
            System.out.println();
        }
        return string;
    }

    /**
     * @param process 传入一个进程
     * @return 返回调页请求是否成功
     */
    public boolean Request(Process process) {
        if (process.need_size > max_block_size) {
            addText_request("        进程" + process.process_ID + "请求的块太大！无法执行申请！\n");
            return false;
        }
        boolean flag;
        List<int[]> temp = FreeMemory.get(process.need_size);
        if (temp.size() == 0) {
            flag = BreakUp_Block(process.need_size * 2, process);
            if (!flag)
                return false;
        }
        Using_Block(process.need_size, process);
        return true;
    }

    /**
     * @param size 传入的是要分块的初始块大小
     * @return 返回是否成功
     */
    public boolean BreakUp_Block(int size, Process process) {
        if (size > max_block_size) {
            //System.out.println("进程" + process.process_ID +"请求的块过大！无法执行申请！\n");
            return false;
        }
        boolean flag = true;
        List<int[]> temp = FreeMemory.get(size);
        if (temp.size() == 0) {
            flag = false;
            flag = BreakUp_Block(size * 2, process);
        }
        if (flag) {
            int[] all_address = temp.get(0);
            temp.remove(0);
            FreeMemory.put(size, temp);

            int[] front_address = new int[2];
            int[] alter_address = new int[2];
            front_address[0] = all_address[0];
            front_address[1] = front_address[0] + (size / 2) - 1;
            alter_address[0] = front_address[1] + 1;
            alter_address[1] = all_address[1];

            List<int[]> list = FreeMemory.get(size / 2);
            list.add(front_address);
            list.add(alter_address);
            FreeMemory.put(size / 2, list);

            return true;
        } else {
            return false;
        }
    }

    /**
     * @param size 传入要使用的块大小，从内存中取出
     * @return 返回这个从内存中调离的空间
     * 把数据从内存中删除，移动到进程中
     */
    public void Using_Block(int size, Process process) {
        int[] using_address = new int[2];
        List<int[]> temp = FreeMemory.get(size);

        if (temp.size() > 0) {//从内存中移除这个区域给进程
            using_address = temp.get(0);//用using_address存储temp区域
            temp.remove(0);
            FreeMemory.put(size, temp);
        }

        addText_request("        进程" + process.process_ID + "得到内存空间分配从" + using_address[0] + "==>" + using_address[1]
                + " 的 " + (using_address[1] - using_address[0] + 1) + "KB内存\n");

        process.set_address(using_address);//把上面的temp区域的内容塞进进程中
        remove_processlabel(process);
        GUI.jLabels_memory[process.process_ID] = add_memorylabel(process);
    }

    /**
     * @param process 接受一个正在使用内存的进程
     * @return 返回是否成功释放这段内存
     */
    public boolean Release(Process process) {
        int[] release_address = process.release_address();
        int begin = release_address[0];
        int end = release_address[1];
        int size = end - begin + 1;

        remove_memorylabel(process);

        addText_release("        进程" + process.process_ID
                + "释放地址" + release_address[0] + "==>" + release_address[1]
                + "的内存" + "大小为：" + size + "KB\n");

        int[] final_block = Combine_Block(begin, end);
        size = final_block[1] - final_block[0] + 1;
        List<int[]> temp = FreeMemory.get(size);
        temp.add(final_block);
        FreeMemory.put(size, temp);
        return true;
    }

    /**
     * @param begin 想合并的块的起始
     * @param end   块的末尾
     * @return 返回最终的合并大总块
     */
    public int[] Combine_Block(int begin, int end) {
        int[] final_block = {begin, end};
        while (true) {
            int[] buddy_block = Get_buddyBlock(begin, end);
            if (buddy_block == null)
                return final_block;
            else {
                if (buddy_block[0] > final_block[1]) {//块的后面是兄弟块
                    final_block[1] = buddy_block[1];
                } else {
                    final_block[0] = buddy_block[0];
                }
                begin = final_block[0];//更新并继续循环
                end = final_block[1];//更新并继续循环
            }
        }
    }

    /**
     * @param begin 块的起始位置
     * @param end   块的结束位置
     * @return 返回返回这个从内存中删掉的兄弟块的位置
     */
    public int[] Get_buddyBlock(int begin, int end) {
        int size = end - begin + 1;
        List<int[]> temp_area = FreeMemory.get(end - begin + 1);
        for (int i = 0; i < temp_area.size(); i++) {
            int[] temp = temp_area.get(i);
            if (temp[1] == begin - 1 || temp[0] == end + 1) {
                temp_area.remove(i);//把该兄弟块删掉
                FreeMemory.put(size, temp_area);
                return temp;//返回这个从内存中删掉的兄弟块
            }
        }
        return null;
    }

    public void remove_processlabel(Process process) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GUI.jLabels_process[process.process_ID].setVisible(false);
        GUI.jLabels_process[process.process_ID].repaint();
    }

    public JLabel add_memorylabel(Process process) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JLabel lblNewLabel = new JLabel("" + process.process_ID, SwingConstants.CENTER);
        lblNewLabel.setBounds((process.process_address[0] / pageframe_size) * (1024 / (freememory_size / pageframe_size)), 0, (process.need_size / pageframe_size) * (1024 / (freememory_size / pageframe_size)), 117);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setOpaque(true);
        lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        lblNewLabel.setBackground(process.background);
        process.background = lblNewLabel.getBackground();

        GUI.panel_memory.add(lblNewLabel);
        lblNewLabel.repaint();
        return lblNewLabel;
    }

    public void remove_memorylabel(Process process) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GUI.jLabels_memory[process.process_ID].setVisible(false);
        GUI.jLabels_memory[process.process_ID].repaint();
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
