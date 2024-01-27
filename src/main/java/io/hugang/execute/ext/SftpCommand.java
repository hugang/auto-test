package io.hugang.execute.ext;

import cn.hutool.extra.ssh.Sftp;
import io.hugang.execute.Command;

import java.io.File;

public class SftpCommand extends Command {
    public SftpCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        String target = getTarget();
        String value = getDictStr("value", getValue());
        String type = getDictStr("type");
        String host = getDictStr("host");
        // if type=download, use sftp to get the target file from the remote server
        String[] split = host.split("@");
        String[] usernamePassword = split[0].split(":");
        String username = usernamePassword[0];
        String password = usernamePassword[1];
        String[] hostPort = split[1].split(":");
        String hostName = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        try (Sftp ftp = new Sftp(hostName, port, username, password)) {

            if ("download".equals(type)) {
                if (ftp.isDir(target)) {
                    ftp.recursiveDownloadFolder(target, new File(value));
                } else {
                    ftp.download(target, new File(value));
                }
                return true;
            } else if ("upload".equals(type)) {
                ftp.syncUpload(new File(target), value);
                return true;
            }
        }
        return false;
    }
}
