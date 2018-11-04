package com.dhr.simplepushstreamutil.runnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class KillFfmpegRunnable implements Runnable {
    private final OutputStream ostrm_;
    private final InputStream istrm_;
    private KillFfmpegCallBack killFfmpegCallBack;
    private boolean isCallBacked;

    public KillFfmpegRunnable(InputStream istrm, OutputStream ostrm, KillFfmpegCallBack killFfmpegCallBack) {
        istrm_ = istrm;
        ostrm_ = ostrm;
        this.killFfmpegCallBack = killFfmpegCallBack;
        isCallBacked = false;
    }

    @Override
    public void run() {
        try {
            String line = null;
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(istrm_, "GBK"));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
                System.out.println(line);
                if (line.contains("方法执行成功")) {
                    killFfmpegCallBack.killProcessSuccess();
                }
            }
        } catch (Exception e) {
            killFfmpegCallBack.killProcessFail(e.getMessage());
            System.out.println(e.getMessage());
            throw new RuntimeException("处理命令出现错误：" + e.getMessage());
        }
    }

    public interface KillFfmpegCallBack {
        void killProcessSuccess();

        void killProcessFail(String reason);
    }
}
