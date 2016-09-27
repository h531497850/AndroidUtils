package android.hqs.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * <ul>
 * <strong>检测root权限</strong>
 * <li>{@link ShellUtils#checkRootPermission()}</li>
 * </ul>
 * <ul>
 * <strong>执行指令</strong>
 * <li>{@link ShellUtils#execCmd(String, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(List, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(List, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String[], boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String[], boolean, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://xxx" target="_blank">songge</a> 2015-5-16
 */
public class ShellUtils {

	public static final String CMD_SU = "su";
	public static final String CMD_SH = "sh";
	public static final String CMD_EXIT = "exit\n";
	public static final String CMD_LINE_END = "\n";

	/**
	 * 检查是否有root权限
	 * 
	 * @return
	 */
	public static boolean checkRootPermission() {
		return execCmd("echo root", true, false).result == 0;
	}

	/**
	 * 执行shell命令，默认的返回结果信息
	 * 
	 * @param cmd
	 *            command
	 * @param isRoot
	 *            运行是否需要root权限
	 * @return
	 * @see ShellUtils#execCmd(String[], boolean, boolean)
	 */
	public static CmdResult execCmd(String cmd, boolean isRoot) {
		return execCmd(new String[] { cmd }, isRoot, true);
	}

	/**
	 * 执行shell命令，默认的返回结果信息
	 * 
	 * @param cmds
	 *            command list
	 * @param isRoot
	 *            运行是否需要root权限
	 * @return
	 * @see ShellUtils#execCmd(String[], boolean, boolean)
	 */
	public static CmdResult execCmd(List<String> cmds, boolean isRoot) {
		return execCmd(cmds == null ? null : cmds.toArray(new String[] {}),
				isRoot, true);
	}

	/**
	 * 执行shell命令，默认的返回结果信息
	 * 
	 * @param cmds
	 *            command array
	 * @param isRoot
	 *            运行是否需要root权限
	 * @return
	 * @see ShellUtils#execCmd(String[], boolean, boolean)
	 */
	public static CmdResult execCmd(String[] cmds, boolean isRoot) {
		return execCmd(cmds, isRoot, true);
	}

	/**
	 * 执行shell命令
	 * 
	 * @param cmd
	 *            command
	 * @param isRoot
	 *            运行是否需要root权限
	 * @param isNeedResultMsg
	 *            是否需要结果信息
	 * @return
	 * @see ShellUtils#execCmd(String[], boolean, boolean)
	 */
	public static CmdResult execCmd(String cmd, boolean isRoot,
			boolean isNeedResultMsg) {
		return execCmd(new String[] { cmd }, isRoot, isNeedResultMsg);
	}

	/**
	 * 执行shell命令
	 * 
	 * @param cmds
	 *            command list
	 * @param isRoot
	 *            运行是否需要root权限
	 * @param isNeedResultMsg
	 *            是否需要结果信息
	 * @return
	 * @see ShellUtils#execCmd(String[], boolean, boolean)
	 */
	public static CmdResult execCmd(List<String> cmds, boolean isRoot,
			boolean isNeedResultMsg) {
		return execCmd(cmds == null ? null : cmds.toArray(new String[] {}),
				isRoot, isNeedResultMsg);
	}

	/**
	 * 执行shell命令
	 * 
	 * @param cmds
	 *            command array
	 * @param isRoot
	 *            运行是否需要root权限，true获取root权限，false获取shell权限
	 * @param isNeedResultMsg
	 *            是否需要结果信息
	 * @return <ul>
	 *         <li>如果 isNeedResultMsg 为 false, {@link CmdResult#successMsg} 为空且
	 *         {@link CmdResult#errorMsg} 为空。</li>
	 *         <li>如果 {@link CmdResult#result} 为-1, 这里可能有一些异常.</li>
	 *         </ul>
	 */
	public static CmdResult execCmd(String[] cmds, boolean isRoot,
			boolean isNeedResultMsg) {
		int result = -1;
		if (cmds == null || cmds.length == 0) {
			return new CmdResult(result, null, null);
		}
		
		Process proc = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;

		DataOutputStream dos = null;
		try {
			proc = Runtime.getRuntime().exec(isRoot ? CMD_SU : CMD_SH);
			dos = new DataOutputStream(proc.getOutputStream());
			for (String cmd : cmds) {
				if (cmd == null) {
					continue;
				}
				// 不要使用dos.writeBytes(cmd)，避免中文字符错误
				dos.write(cmd.getBytes());
				dos.writeBytes(CMD_LINE_END);
				dos.flush();
			}
			dos.writeBytes(CMD_EXIT);
			dos.flush();

			/*
			 * 该方法将导致当前的线程等待，如果必要的话，直到由该Process对象表示的进程已经终止。如果子进程已经终止此方法将立即返回。
			 * 如果子进程尚未终止，则调用线程将被阻塞，直到子进程退出。
			 * 0表示正常终止。
			 */
			result = proc.waitFor();
			// 获取 cmd 结果
			if (isNeedResultMsg) {
				successMsg = new StringBuilder();
				errorMsg = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(
						proc.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (proc != null) {
				proc.destroy();
			}
		}
		return new CmdResult(result, successMsg == null ? null
				: successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}

	/**
	 * cmd结果
	 * <ul>
	 * <li>{@link CmdResult#result} cmd 0正常，其他错误，有些在命令在Linux上执行shell。</li>
	 * <li>{@link CmdResult#successMsg} 成功信息</li>
	 * <li>{@link CmdResult#errorMsg} 错误信息</li>
	 * </ul>
	 * 
	 * @author <a href="http://xxx" target="_blank">songge</a> 2015-5-16
	 */
	public static class CmdResult {

		/** cmd结果 **/
		public int result;
		/** 成功信息 **/
		public String successMsg;
		/** 错误信息 **/
		public String errorMsg;

		public CmdResult(int result) {
			this.result = result;
		}

		public CmdResult(int result, String successMsg, String errorMsg) {
			this.result = result;
			this.successMsg = successMsg;
			this.errorMsg = errorMsg;
		}
	}
}
