package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.bean.Command;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

/**
 * recorder command
 *
 * <p>
 * only support start recorder
 */
public class RecorderCommand extends Command {
    public static FrameGrabber grabber;
    public static FrameRecorder recorder;
    private static final Log log = Log.get();

    public RecorderCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String outputFile = getTarget();
        // new recorder and start
        try {
            grabber = new FFmpegFrameGrabber("desktop");
            grabber.setFormat("gdigrab");
            grabber.setFrameRate(30);
            grabber.start();

            // Create a FrameRecorder
            FrameRecorder recorder = FFmpegFrameRecorder.createDefault(outputFile, 640, 480);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // MP4 encoding
            recorder.setFormat("mp4");
            recorder.setFrameRate(30);
            recorder.start();

            // create a async thread to record
            new Thread(() -> {
                try {
                    // Capture and record frames
                    Frame frame;
                    while ((frame = grabber.grab()) != null) {
                        recorder.record(frame);
                    }
                } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
                    log.error("record error", e);
                }
            }).start();
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            log.error("start recorder error", e);
            return false;
        }
        return true;
    }

    public static void stop() {
        // stop recorder if recorder is not null
        if (RecorderCommand.recorder != null) {
            try {
                RecorderCommand.recorder.stop();
                RecorderCommand.recorder = null;
            } catch (FrameRecorder.Exception e) {
                log.error("stop recorder error", e);
            }
        }
        if (RecorderCommand.grabber != null) {
            try {
                RecorderCommand.grabber.stop();
                RecorderCommand.grabber = null;
            } catch (FrameGrabber.Exception e) {
                log.error("stop recorder error", e);
            }
        }
    }
}
