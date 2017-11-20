package net.teamfruit.alserverroster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Setting {

	private File file = new File("setting.properties").getAbsoluteFile();
	private Properties setting = new Properties();

	public File getSettingFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public Properties getSetting() {
		return this.setting;
	}

	public void setSetting(final Properties setting) {
		this.setting = setting;
	}

	public Setting load() throws IOException {
		try (FileInputStream fis = new FileInputStream(getSettingFile())) {
			getSetting().load(fis);
		}
		return this;
	}

	public Setting save() throws IOException {
		try (FileOutputStream fos = new FileOutputStream(getSettingFile())) {
			getSetting().store(fos, null);
		}
		return this;
	}

	public Setting reset() {
		getSetting().setProperty("discordtoken", "");
		return this;
	}

	public boolean exists() {
		return getSettingFile().exists();
	}
}
