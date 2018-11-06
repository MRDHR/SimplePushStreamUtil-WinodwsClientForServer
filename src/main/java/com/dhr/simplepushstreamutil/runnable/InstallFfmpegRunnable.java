package com.dhr.simplepushstreamutil.runnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InstallFfmpegRunnable implements Runnable {
    private final InputStream istrm_;
    private InstallFfmpegCallBack installFfmpegCallBack;

    public InstallFfmpegRunnable(InputStream istrm, InstallFfmpegCallBack installFfmpegCallBack) {
        istrm_ = istrm;
        this.installFfmpegCallBack = installFfmpegCallBack;
    }

    @Override
    public void run() {
        try {
            String line = null;
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(istrm_, StandardCharsets.UTF_8));
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                installFfmpegCallBack.log(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("处理命令出现错误：" + e.getMessage());
        }
    }

    public interface InstallFfmpegCallBack {
        void log(String log);
    }
}
