package me.PauMAVA.UhcPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*Further development needed:
 * - Create permissions.yml and create a permission to execute this command.
 * - Players must not be able to add themselves to this permission so always check that the permission add request is
 * created by the console.
 * - Use clickable text to confirm the abort action.
 * - Commit Bukkit.getScheduler().cancelTasks();
 * The permissions module must be developed before impkementing this command!
 * */



public class AbortCmd implements CommandExecutor {
	
	UhcPluginCore directPlugin;
	public AbortCmd(UhcPluginCore passedPlugin) {
		this.directPlugin = passedPlugin;
	}
	
	public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
		
		/*Check if the sender has permission to commit the command*/
		if(theSender instanceof Player) {
			theSender.sendMessage(ChatColor.RED + "Are you sure you want to abort UHC? This will stop the game, even if it has started!");
		}
		
		return true;
	}
}
