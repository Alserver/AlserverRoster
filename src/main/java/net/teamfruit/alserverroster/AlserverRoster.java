package net.teamfruit.alserverroster;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import sx.blah.discord.Discord4J.Discord4JLogger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class AlserverRoster {
	public static AlserverRoster instance;
	public static final Logger LOG = new Discord4JLogger(AlserverRoster.class.getName());

	private Setting setting;
	private IDiscordClient discord;

	public void launch() {
		try {
			this.setting = new Setting();
			if (!this.setting.exists()) {
				this.setting.reset().save();
				LOG.info("設定ファイルを"+this.setting.getSettingFile()+"に生成しました");
				return;
			}
			this.setting.load();
			LOG.info("設定をロード");
			if (StringUtils.isEmpty(this.setting.getSetting().getProperty("discordtoken"))) {
				LOG.error("DiscordBotのトークンを設定して下さい！");
				return;
			}

			this.discord = new ClientBuilder()
					.withToken(this.setting.getSetting().getProperty("discordtoken"))
					.registerListener(DiscordEventListener.INSTANCE)
					.login();
		} catch (final Exception e) {
			LOG.error("起動中にエラーが発生しました");
			LOG.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public Setting getSetting() {
		return this.setting;
	}

	public IDiscordClient getDiscordClient() {
		return this.discord;
	}

	public static void main(final String[] args) {
		instance = new AlserverRoster();
		instance.launch();
	}
}
