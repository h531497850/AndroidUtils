package android.hqs.gj.util;

import java.util.regex.Pattern;

public class PTN {

	// aee_exp
	/**
	 * db.fatal.03.ANR/KE/HW_Reboot/HWT/NE/JE/SWT
	 */
	public static final Pattern TYPE = Pattern.compile("db.fatal.[0-9]{2}.[A-Za-z_]{2,9}");
	/**
	 * db.fatal.00.KE.dbg
	 */
	public static final Pattern DBG = Pattern.compile("db.fatal.[0-9]{2}.[A-Za-z_]{2,9}.dbg");
	/**
	 * db.fatal.00.KE.dbg.DEC
	 */
	public static final Pattern DEC = Pattern.compile("db.fatal.[0-9]{2}.[A-Za-z_]{2,9}.dbg.DEC");
	/**
	 * ... 11 more
	 */
	public static final Pattern MORE = Pattern.compile(".{3}\\s+[0-9]{1,}\\s+(more)");
	// last_kmsg_PD1613_A_1.13.4_2017_0607_233038.txt
	/**
	 * last_kmsg_PD1613_A_1.13.4_2017_0607_233038
	 */
	public static final Pattern KMSG = Pattern.compile("last_kmsg_[A-Z]{2,3}[0-9]{4}_(.+)_[0-9]{4}_[0-9]{4}_[0-9]{6}");
	/**
	 * Get module name for Qualcomm platform
	 * <p>
	 * Process RenderThread (pid: 23543, stack limit = 0xc2542238)
	 */
	public static final Pattern QCOM_MODEL = Pattern
			.compile("Process\\s+[A-Za-z0-9]{1,}\\s+\\(pid:\\s*[0-9]{1,}.*(stack\\s+limit.*0[x|X][A-Fa-f0-9]{1,})\\)");
	
	/**
	 * [<c01d4e2c>] (set_page_dirty) from [<c01f2fdc>] (do_shared_fault+0x12c/0x198)
	 */
	public static final Pattern QCOM_KE = Pattern
			.compile("\\[<[A-Fa-f0-9]{8}>\\]\\s+\\(.+\\)\\s+from\\s+\\[<[A-Fa-f0-9]{8}>\\]\\s+\\(.+\\)");


}
