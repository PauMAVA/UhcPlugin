package me.PauMAVA.UhcPlugin.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface UhcCommandHandler {

    boolean handleCommand(CommandSender theSender, List<String> args);

}
