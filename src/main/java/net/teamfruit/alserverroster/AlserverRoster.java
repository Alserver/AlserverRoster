package net.teamfruit.alserverroster;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import sx.blah.discord.Discord4J.Discord4JLogger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

public class AlserverRoster {
	public static AlserverRoster instance;
	public static final Logger LOG = new Discord4JLogger(AlserverRoster.class.getName());

	private Setting setting;
	private RosterClient roster;
	private IDiscordClient discord;

	public void launch() {
		try {
			this.setting = new Setting();
			if (!this.setting.exists()) {
				this.setting.save();
				LOG.info("設定ファイルを"+this.setting.getSettingFile()+"に生成しました");
				return;
			}
			this.setting.load();
			this.setting.save();
			LOG.info("設定をロード");
			if (!this.setting.isValid()) {
				LOG.error("設定に空の項目があります");
				return;
			}

			final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			final FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new File(".").getAbsoluteFile());
			final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			final List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

			final InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(this.setting.getProperty("google_client_secret"))));
			final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, isr);
			final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
					.setDataStoreFactory(dataStoreFactory)
					.setAccessType("online")
					.build();
			final Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

			final Sheets sheets = new Sheets.Builder(httpTransport, jsonFactory, credential)
					.setApplicationName(this.setting.getProperty("application_name"))
					.build();

			this.discord = new ClientBuilder()
					.withToken(this.setting.getProperty("discord_token"))
					.registerListener(DiscordEventListener.INSTANCE)
					.login();

			final IGuild guild = this.discord.getGuildByID(Long.parseLong(this.setting.getProperty("discord_guild_id")));
			this.roster = new RosterClient(sheets, this.setting.getProperty("spreadsheet_id"), this.setting.getProperty("sheet_name"), guild);

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

	public RosterClient getRosterClient() {
		return this.roster;
	}

	public static void main(final String[] args) {
		instance = new AlserverRoster();
		instance.launch();
	}
}
