package io.hugang.execute.ext;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

import java.io.InputStream;

public class SshCommand extends Command {
    public SshCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "ssh";
    }

    @Override
    public boolean _execute() {
        String host = getDictStr("host");
        String port = getDictStr("port", "22");
        String user = getDictStr("user");
        String password = getDictStr("password");
        String command = getDictStr("command");

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, Integer.parseInt(port));
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();

            // show the command output
            InputStream in = channel.getInputStream();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }

            channel.disconnect();
            session.disconnect();

            return true;
        } catch (Exception e) {
            log.error("ssh command execute error", e);
            return false;
        }
    }
}