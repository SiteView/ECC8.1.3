package com.siteview.ecc.system.impl;

import java.io.File;
import java.util.ArrayList;

public final class OS {
	public static final OS WIN = new OS("WIN");

	public static final OS MAC = new OS("MAC");

	public static final OS POSIX = new OS("POSIX");

	private String ourOS = "";

	/**
	 * Private constructor to avert instantiation.
	 */
	private OS(String os) {
		ourOS = os;
	}

	/**
	 * Returns a String representing this OS object.
	 */
	public String toString() {
		return ourOS;
	}

	/**
	 * Returns the first readable and writable application data folder
	 * appropriate to this OS.
	 */
	public static File getAppDataFolder() {
		File[] folders = listAppDataFolders();

		for (int i = 0; i < folders.length; i++) {
			File folder = folders[i];
			if (folder.canRead() && folder.canWrite())
				return folder;
		}

		return null;
	}

	/**
	 * Returns the first readable and writable user data folder appropriate to
	 * this OS.
	 */
	public static File getUserDataFolder() {
		File[] folders = listUserDataFolders();

		for (int i = 0; i < folders.length; i++) {
			File folder = folders[i];
			if (folder.canRead() && folder.canWrite())
				return folder;
		}

		return null;
	}

	/**
	 * Returns a list of the preferred locations for storing application-
	 * specific data in descending order.
	 */
	public static File[] listAppDataFolders() {
		String home = System.getProperty("user.home");
		ArrayList folders = new ArrayList();

		if (isWinNT()) // NT/2000/XP
		{
			// C:\Documents and Settings\All Users\Application Data
			// Surmise that the "All Users" folder will be a child of the
			// parent of the current user's home folder:
			File folder = new File(home).getParentFile();
			folders.add(new File(folder, "All Users\\Application Data"));
		}

		else if (isWin9X()) // 95/98/ME
		{
			// C:\Windows
			folders.add(new File(home));
		}

		else if (isVista()) {
			// C:\ProgramData
			File folder = new File(home).getParentFile().getParentFile();
			folders.add(new File(folder, "ProgramData"));

			// C:\Users\Public\AppData
			folder = new File(home).getParentFile();
			folders.add(new File(folder, "Public\\AppData"));
		}

		else if (isMac()) {
			folders.add(new File("/Library/Application Support"));
		}

		else {
			folders.add(new File("/var/local"));
			folders.add(new File("/var"));
		}

		// folders.addAll(Arrays.asList(listUserDataFolders()));
		File[] files = new File[folders.size()];
		return (File[]) folders.toArray(files);
	}

	/**
	 * Returns a list of the preferred locations for storing user- specific data
	 * in descending order.
	 */
	public static File[] listUserDataFolders() {
		String home = System.getProperty("user.home");
		ArrayList folders = new ArrayList();

		if (isWinNT()) {
			folders.add(new File(home + "\\Application Data"));
			folders.add(new File(home + "\\Local Settings\\Application Data"));
		}

		else if (isVista()) {
			folders.add(new File(home + "\\AppData"));
			folders.add(new File(home + "\\AppData\\Local"));
		}

		else if (isMac()) {
			folders.add(new File(home + "/Library/Application Support"));
		}

		folders.add(new File(home));

		File[] files = new File[folders.size()];
		return (File[]) folders.toArray(files);
	}

	/**
	 * Returns the name of the current operating system platform as listed in
	 * the System properties.
	 */
	public static final String getOSName() {
		return System.getProperty("os.name");
	}

	/**
	 * Returns the OS representing the current operating system platform.
	 */
	public static final OS getOS() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("windows") >= 0)
			return WIN;
		else if (os.indexOf("mac os") >= 0)
			return MAC;
		else
			return POSIX;
	}

	/**
	 * Returns whether or not the current operating system is a Microsoft
	 * Windows platform.
	 */
	public static final boolean isWindows() {
		return getOS() == WIN;
	}

	/**
	 * Returns whether or not the current operating system is a Microsoft
	 * Windows 9X platform (Windows 95/98/ME).
	 */
	public static boolean isWin9X() {
		if (getOS() != WIN)
			return false;

		String os = getOSName();
		if (os.indexOf("95") >= 0 || os.indexOf("98") >= 0
				|| os.indexOf("me") >= 0)
			return true;
		return false;
	}

	/**
	 * Returns whether or not the current operating system is a Microsoft
	 * Windows NT platform (Windows NT/2000/2003/XP).
	 */
	public static boolean isWinNT() {
		if (getOS() != WIN)
			return false;

		String os = getOSName();
		if (os.indexOf("NT") >= 0 || os.indexOf("2000") >= 0
				|| os.indexOf("2003") >= 0 || os.indexOf("XP") >= 0)
			return true;
		return false;
	}

	/**
	 * Returns whether or not the current operating system is a Microsoft
	 * Windows Vista platform.
	 */
	public static boolean isVista() {
		if (getOS() != WIN)
			return false;

		String os = getOSName();
		return (os.indexOf("Vista") >= 0);
	}

	/**
	 * Returns whether or not the current operating system is the Apple
	 * Macintosh OS X platform.
	 */
	public static final boolean isMac() {
		return getOS() == MAC;
	}

	/**
	 * Returns whether or not the current operating system is a Posix-compatible
	 * platform (Unix, Linux, Solaris, etc).
	 */
	public static final boolean isPosix() {
		return getOS() == POSIX;
	}

}