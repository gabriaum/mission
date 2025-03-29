package com.gabriaum.mission.command;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.user.User;
import com.gabriaum.mission.user.assets.Party;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length < 1) {

            sender.sendMessage(" ");
            sender.sendMessage("§6Uso do §a/" + label + "§6:");
            sender.sendMessage("§e/" + label + " convidar <target>");
            sender.sendMessage("§e/" + label + " aceitar <target>");
            sender.sendMessage("§e/" + label + " sair");
            sender.sendMessage("§e/" + label + " apagar");
            sender.sendMessage(" ");

            return true;
        }

        ArgumentType argumentType = ArgumentType.getByAlias(args[0]);

        if (argumentType == null) {
            player.sendMessage("§cArgumento inválido.");
            return false;
        }

        argumentType.argument.execute(args, player);

        return false;
    }

    @RequiredArgsConstructor
    protected enum ArgumentType {

        INVITE("convidar", (args, player) -> {

            if (args.length < 2) {

                player.sendMessage(" ");
                player.sendMessage("§6Uso do §a/party convidar§6:");
                player.sendMessage("§e/party convidar <target>");
                player.sendMessage(" ");

                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage("§cJogador não encontrado.");
                return;
            }

            if (player.equals(target)) {
                player.sendMessage("§cVocê não pode convidar a si mesmo.");
                return;
            }

            User user = MissionMain.getInstance().getUserController().get(player.getUniqueId());
            User targetUser = MissionMain.getInstance().getUserController().get(target.getUniqueId());

            if (user == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar sua conta.");
                return;
            }

            if (targetUser == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar a conta do jogador.");
                return;
            }

            if (targetUser.getParty() != null) {
                player.sendMessage("§cO jogador já está em uma party.");
                return;
            }

            Party party = user.getParty();

            if (party == null) {

                party = new Party(player.getUniqueId());

                party.addMember(player.getUniqueId());

                user.setParty(party);
            }

            if (targetUser.getInvites().contains(player.getUniqueId())) {
                player.sendMessage("§cVocê já convidou este jogador.");
                return;
            }

            TextComponent acceptComponent = new TextComponent("§b§lCLIQUE AQUI§e para aceitar.");

            acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party aceitar " + player.getName()));

            targetUser.getInvites().add(player.getUniqueId());

            player.sendMessage("§aPedido de party enviado com sucesso.");

            target.sendMessage("§aVocê recebeu um convite de party de §b" + player.getName() + "§a.");
            target.spigot().sendMessage(acceptComponent);
        }),

        ACCEPT("aceitar", (args, player) -> {

            if (args.length < 2) {

                player.sendMessage(" ");
                player.sendMessage("§6Uso do §a/party aceitar§6:");
                player.sendMessage("§e/party aceitar <player>");
                player.sendMessage(" ");

                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage("§cJogador não encontrado.");
                return;
            }

            User user = MissionMain.getInstance().getUserController().get(player.getUniqueId());
            User targetUser = MissionMain.getInstance().getUserController().get(target.getUniqueId());

            if (user == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar sua conta.");
                return;
            }

            if (targetUser == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar a conta do jogador.");
                return;
            }

            if (user.getParty() != null) {
                player.sendMessage("§cVocê já está em uma party.");
                return;
            }

            Party party = targetUser.getParty();

            if (party == null) {
                player.sendMessage("§c" + target.getName() + " não está mais em uma party.");
                return;
            }

            party.getMembers().add(player.getUniqueId());

//            user.setParty(party);

            party.sendMessage("§b" + player.getName() + "§a entrou na party.");

            for (UUID member : party.getMembers()) {

                Player memberPlayer = Bukkit.getPlayer(member);

                if (memberPlayer == null) {
                    continue;
                }

                User memberUser = MissionMain.getInstance().getUserController().get(member);

                if (memberUser == null) {
                    continue;
                }

                memberUser.setParty(party);
            }
        }),

        LEAVE("sair", (args, player) -> {

            User user = MissionMain.getInstance().getUserController().get(player.getUniqueId());

            if (user == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar sua conta.");
                return;
            }

            Party party = user.getParty();

            if (party == null) {
                player.sendMessage("§cVocê não está em uma party.");
                return;
            }

            if (party.getOwnerId().equals(player.getUniqueId())) {
                player.sendMessage("§cVocê é o dono da party, use /party apagar.");
                return;
            }

            party.getMembers().remove(player.getUniqueId());
            party.sendMessage("§b" + player.getName() + "§a saiu da party.");

            for (UUID member : party.getMembers()) {

                Player memberPlayer = Bukkit.getPlayer(member);

                if (memberPlayer == null) {
                    continue;
                }

                User memberUser = MissionMain.getInstance().getUserController().get(member);

                if (memberUser == null) {
                    continue;
                }

                memberUser.setParty(party);
            }
        }),

        DELETE("apagar", (args, player) -> {

            User user = MissionMain.getInstance().getUserController().get(player.getUniqueId());

            if (user == null) {
                player.sendMessage("§cOcorreu um problema ao tentar consultar sua conta.");
                return;
            }

            Party party = user.getParty();

            if (party == null) {
                player.sendMessage("§cVocê não está em uma party.");
                return;
            }

            if (!party.getOwnerId().equals(player.getUniqueId())) {
                player.sendMessage("§cVocê não é o dono da party.");
                return;
            }

            party.sendMessage("§cA party foi apagada.");

            party.getMembers().forEach(member -> {

                User memberUser = MissionMain.getInstance().getUserController().get(member);

                if (memberUser == null) {
                    return;
                }

                memberUser.setParty(null);
            });
        })
        ;

        private final String alias;

        private final Argument argument;

        public static ArgumentType getByAlias(String alias) {
            return Arrays.stream(values()).filter(argumentType -> argumentType.alias.equalsIgnoreCase(alias)).findFirst().orElse(null);
        }

        protected interface Argument {

            void execute(String[] args, Player player);
        }
    }
}
