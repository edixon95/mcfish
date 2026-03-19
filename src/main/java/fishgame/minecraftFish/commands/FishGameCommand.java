package fishgame.minecraftFish.commands;

import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.game.GameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FishGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public FishGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /fishgame <start|stop>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            gameManager.startGame();
            sender.sendMessage(Component.text("Fishing game started!"));
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            gameManager.stopGame();
            sender.sendMessage(Component.text("Fishing game stopped!"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            switch (args[1].toLowerCase()) {
                case "fish":
                    gameManager.getFishManager().reloadFish();
                    break;

                default:
                    sender.sendMessage("Unknown reload type.");
                    break;
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {

            switch (args[1].toLowerCase()) {
                case "fish":

                    FishType[] fishTypes = gameManager.getFishManager().getFishPool();

                    for (FishType fish : fishTypes) {
                        sender.sendMessage(
                                fish.getName() + " | Gold Value: " + fish.getBaseGoldValue() + " | Weight: " + fish.getRarityWeight()
                        );
                    }

                    break;

                default:
                    sender.sendMessage("Unknown reload type.");
                    break;
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("player")) {
            String name = args[2];
            switch(args[1].toLowerCase()) {
                case "raritymod":
                    double rarityModifier;
                    try {
                        rarityModifier = Double.parseDouble(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Power must be a number.");
                        return true;
                    }
                    gameManager.getPlayerManager().handleRarityModifier(name, rarityModifier);

                    return true;

                case "grademod":
                    double gradeModifier;
                    try {
                        gradeModifier = Double.parseDouble(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Power must be a number.");
                        return true;
                    }
                    gameManager.getPlayerManager().handleGradeModifier(name, gradeModifier);

                    return true;

                default:
                    sender.sendMessage("Unknown player command.");
                    break;
            }


        }

        sender.sendMessage(Component.text("Unknown command. Use start or stop."));
        return true;
    }
}