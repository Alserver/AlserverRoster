package net.teamfruit.alserverroster;

import com.google.api.services.sheets.v4.Sheets;

import sx.blah.discord.handle.obj.IGuild;

public class RosterClient {

	private final Sheets sheets;
	private final String id;
	private final String name;
	private final IGuild guild;

	public RosterClient(final Sheets sheets, final String spreadsheetId, final String sheetName, final IGuild guild) {
		this.sheets = sheets;
		this.id = spreadsheetId;
		this.name = sheetName;
		this.guild = guild;
	}
}
