package android.hqs.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.hqs.tool.LogcatTool;
import android.hqs.util.FormatUtil;
import android.os.Environment;
import android.util.Log;

public class FlashQuit implements UncaughtExceptionHandler {
	private static final String TAG = LogcatTool.makeTag(FlashQuit.class);

	private static final String SAVE_PATH;
	private static FlashQuit HANDLER;
	private final String mPackageName;

	private void wtf(Object msg) {
		Log.wtf(TAG, String.valueOf(msg));
	}

	static {
		SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + "/SystemErr.txt";
	}

	private FlashQuit(Context context) {
		mPackageName = context.getPackageName();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static void startWork(Context context) {
		if(HANDLER == null) {
			HANDLER = new FlashQuit(context);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		wtf("UncaughtException: saved at " + SAVE_PATH);
		ex.printStackTrace();
		saveStackTrace(ex);
		System.exit(0);
	}

	private void saveStackTrace(Throwable ex) {
		final Writer writer = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(writer);
		FileOutputStream stream = null;
		try {
			ex.printStackTrace(printWriter);
			final File file = new File(SAVE_PATH);
			stream = new FileOutputStream(file, true);
			stream.write((getHeadInfo() + writer.toString() + "\r\n\r\n").getBytes());
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		finally {
			if (printWriter != null) {
				printWriter.close();
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getHeadInfo() {
		return "\r\n\r\n"
				+ "["
				+ mPackageName
				+ ": "
				+ FormatUtil.time(System.currentTimeMillis(),
						"yyyy年MM月dd日 HH点   mm分 ss秒 SSS毫秒") + "]\r\n";
	}
}
