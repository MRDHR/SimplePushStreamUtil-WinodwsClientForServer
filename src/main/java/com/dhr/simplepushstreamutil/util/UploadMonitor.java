package com.dhr.simplepushstreamutil.util;

import com.jcraft.jsch.SftpProgressMonitor;

import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author kevin.chen
 */
public class UploadMonitor implements SftpProgressMonitor, Runnable {

    /**
     * 文件的总大小
     */
    private long maxCount = 0;
    private long uploaded = 0;
    private long startTime = 0L;

    private boolean isScheduled = false;

    private ScheduledExecutorService executorService;
    private UploadCallBack uploadCallBack;

    public UploadMonitor(long maxCount, UploadCallBack uploadCallBack) {
        this.maxCount = maxCount;
        this.uploadCallBack = uploadCallBack;
    }

    /**
     * 当文件开始传输时，调用init方法
     *
     * @param op
     * @param src
     * @param dest
     * @param max
     */
    @Override
    public void init(int op, String src, String dest, long max) {
        uploadCallBack.uploadLog("开始上传文件：" + src + "至远程：" + dest + "文件总大小:" + maxCount / 1024 + "KB" + "\n");
        startTime = System.currentTimeMillis();
    }

    /**
     * 当每次传输了一个数据块后，调用count方法，count方法的参数为这一次传输的数据块大小
     *
     * @param count
     * @return
     */
    @Override
    public boolean count(long count) {
        if (!isScheduled) {
            createTread();
        }
        uploaded += count;
        uploadCallBack.uploadLog("本次上传大小：" + count / 1024 + "KB," + "\n");
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 当传输结束时，调用end方法
     */
    @Override
    public void end() {

    }

    /**
     * 创建一个线程每隔一定时间，输出一下上传进度
     */
    public void createTread() {
        executorService = Executors.newSingleThreadScheduledExecutor();

        //1秒钟后开始执行，每1秒钟执行一次
        executorService.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
        isScheduled = true;
    }

    @Override
    public void run() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        String value = format.format((uploaded / (double) maxCount));
        uploadCallBack.uploadLog("已传输：" + uploaded / 1024 + "KB,传输进度：" + value + "\n");
        if (uploaded == maxCount) {
            stop();
            long endTime = System.currentTimeMillis();
            uploadCallBack.uploadLog("传输完成！用时：" + (endTime - startTime) / 1000 + "s" + "\n");
        }
    }


    public void stop() {
        boolean isShutdown = executorService.isShutdown();
        if (!isShutdown) {
            executorService.shutdown();
        }
    }

    public interface UploadCallBack {
        public void uploadLog(String log);
    }
}