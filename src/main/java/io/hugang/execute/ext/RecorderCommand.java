package io.hugang.execute.ext;

import cn.hutool.log.Log;
import io.hugang.execute.Command;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import java.util.concurrent.Executors;

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
    public String getCommand() {
        return "recorder";
    }

    @Override
    public boolean _execute() {
        String outputFile = getTarget();
        // new recorder and start
        int width = 1920;
        int height = 1080;
        int frameRate = 30;
        try {
            grabber = new FFmpegFrameGrabber("desktop");
            grabber.setFormat("gdigrab");
            grabber.setFrameRate(frameRate);
            grabber.setImageHeight(height);
            grabber.setImageWidth(width);
            grabber.start();

            // Create a FrameRecorder
            recorder = FFmpegFrameRecorder.createDefault(outputFile, width, height);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // MP4 encoding
            recorder.setFormat("mp4");
            recorder.setFrameRate(frameRate);
            recorder.start();

            // create an async thread to record
            Thread recorder = new Thread(() -> {
                try {
                    // Capture and record frames
                    Frame frame;
                    while ((frame = grabber.grab()) != null) {
                        RecorderCommand.recorder.record(frame);
                    }
                } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
                    log.error("record error", e);
                }
            });
            Executors.newSingleThreadExecutor().submit(recorder);
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
