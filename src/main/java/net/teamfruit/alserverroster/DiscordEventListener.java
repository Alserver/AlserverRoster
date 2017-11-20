package net.teamfruit.alserverroster;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;

public class DiscordEventListener {
	public static final DiscordEventListener INSTANCE = new DiscordEventListener();

	private DiscordEventListener() {
	}

	@EventSubscriber
	public void onUserJoin(final UserJoinEvent event) {

	}

	@EventSubscriber
	public void onUserLeave(final UserLeaveEvent event) {

	}

	@EventSubscriber
	public void onUserRoleUpdate(final UserRoleUpdateEvent event) {

	}

	@EventSubscriber
	public void onUserUpdate(final UserUpdateEvent event) {

	}
}
