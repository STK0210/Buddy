import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;

/**
 * @author keyuli
 */
public class Process {
    public int process_ID = 0;//进程ID
    public int real_size = 0;//进程真实的申请大小
    public int arrive_time = 0;//到达时间
    public int execute_time = 0;//执行需要时间
    public Color background;

    public int need_size = 0;//进程需要的空间
    public int[] process_address = {0, 0};//进程的物理地址

    public List<Process> process_list = new ArrayList<>();

    /**
     * 集合的统一初始化不调用细化方法
     */
    Process() {
    }

    /**
     * @param ID      进程ID
     * @param real    进程真实的申请大小
     * @param arrive  到达时间
     * @param execute 执行需要时间
     */
    Process(int ID, int real, int arrive, int execute, int pageframe_size) {
        if (real <= 0) {
            System.out.println("输入数据错误！进程生成失败！");
            return;
        }
        this.process_ID = ID;
        this.real_size = real;
        this.need_size = (int) pow(2, (int) Math.ceil(Math.log(real_size) / Math.log(2)));
        if (this.need_size < pageframe_size) {
            this.need_size = (int) pow(2, (int) Math.ceil(Math.log(pageframe_size)));
        }
        this.arrive_time = arrive;
        this.execute_time = execute;
    }

    /**
     * @param address 把地址设置给进程
     */
    public void set_address(int[] address) {
        this.process_address = address;
    }

    /**
     * @return 返回进程占用的地址, 只是获取
     */
    public int[] get_address() {
        return process_address;
    }

    /**
     * @return 进程释放地址并返回这段地址
     */
    public int[] release_address() {
        int[] temp_address = this.process_address;
        this.process_address = null;
        return temp_address;
    }

    /**
     * @param process_sum   传入要生成的进程总数
     * @param pageframesize 页框的大小
     * @param blockgroup    块组的数目
     * @return 返回一个进程集合
     */
    public List<Process> initialize_Process(int process_sum, int pageframesize, int blockgroup) {
        int max_size = (pageframesize * (int) pow(2, blockgroup - 1) + 1) / 3;
        int min_size = 1;
        int max_time = 10 + 1;
        int min_time = 1;
        for (int i = 0; i < process_sum; i++) {
            Process p = new Process(i, new Random().nextInt(max_size - min_size) + min_size, i, new Random().nextInt(max_time - min_size) + min_time, pageframesize);
            process_list.add(p);
        }
        return process_list;
    }
}