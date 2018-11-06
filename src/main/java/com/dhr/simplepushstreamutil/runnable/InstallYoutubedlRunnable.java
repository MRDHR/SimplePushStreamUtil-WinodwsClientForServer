package com.dhr.simplepushstreamutil.runnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InstallYoutubedlRunnable implements Runnable {
    private final InputStream istrm_;
    private InstallYoutubedlCallBack installYoutubedlCallBack;

    public InstallYoutubedlRunnable(InputStream istrm, InstallYoutubedlCallBack installYoutubedlCallBack) {
        istrm_ = istrm;
        this.installYoutubedlCallBack = installYoutubedlCallBack;
    }

    @Override
    public void run() {
        try {
            String line = null;
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(istrm_, StandardCharsets.UTF_8));
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                installYoutubedlCallBack.log(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("处理命令出现错误：" + e.getMessage());
        }
    }

    public interface InstallYoutubedlCallBack {
        void log(String log);
    }
}
