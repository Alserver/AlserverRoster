package net.teamfruit.alserverroster;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

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

	public void init() throws IOException {
		final List<List<Object>> values = this.sheets.spreadsheets().values()
				.get(this.id, this.name)
				.execute().getValues();
		if (values==null||values.isEmpty())
			createNewRoster();

	}

	private void createNewRoster() throws IOException {
		final List<Object> a = Lists.newArrayList("", "", "");
		final List<Object> b = Lists.newArrayList("Members", this.guild.getTotalMemberCount(), "");
		final List<List<Object>> row = Lists.newArrayList();
		row.add(a);
		row.add(b);

		final Set<IUser> users = Sets.newHashSet(this.guild.getUsers());
		for (final ListIterator<IRole> it = this.guild.getRoles().listIterator(this.guild.getRoles().size()); it.hasPrevious();) {
			final IRole role = it.previous();
			final List<IUser> roleusers = this.guild.getUsersByRole(role);
			if (role.isHoisted()&&!roleusers.isEmpty()) {
				a.add(role.getLongID());
				b.add(role.getName());
				a.add("");
				b.add("Name");
				for (final IUser user : roleusers)
					if (users.remove(user)) {
						a.add(user.getLongID());
						b.add(user.getDisplayName(this.guild));
					}
				a.add("");
				b.add("");
			}
		}
		final ValueRange range = new ValueRange();
		range.setMajorDimension("COLUMNS");
		range.setValues(row);
		System.out.println(this.sheets.spreadsheets().values()
				.update(this.id, this.name, range)
				.setValueInputOption("USER_ENTERED")
				.execute());
	}
}
