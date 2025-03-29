package com.gabriaum.mission.listener;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.event.list.MissionTimeEvent;
import com.gabriaum.mission.mission.Mission;
import com.gabriaum.mission.mission.stage.MissionStage;
import com.gabriaum.mission.mission.type.MissionType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Iterator;

public class MissionListener implements Listener {

    @EventHandler
    public void onMissionTime(MissionTimeEvent event) {

        Iterator<Mission> iterator = MissionMain.getInstance().getMissionController().iterator();

        while (iterator.hasNext()) {

            Mission mission = iterator.next();

            switch (mission.getStage()) {

                case STARTING -> {

                    if (mission.getTime() == 6) {

                        mission.sendMessage("§aA missão §b" + mission.getType().getName() + "§a irá iniciar em §b5§a segundos.");
                        mission.sendTitle("§aIniciando em", "§f5 segundos");
                    }

                    mission.setTime(mission.getTime() - 1);

                    if (mission.getTime() < 1) {

                        mission.sendMessage("§aA missão §b" + mission.getType().getName() + "§a iniciou.");

                        mission.setStage(MissionStage.IN_PROGRESS);
                    } else {

                        mission.sendMessage("§aA missão §b" + mission.getType().getName() + "§a irá iniciar em §b" + mission.getTime() + "§a segundos.");
                    }

                    break;
                }

                case IN_PROGRESS -> {

                    if (mission.getParticipants().isEmpty()) {

                        mission.sendMessage("§aA missão §b" + mission.getType().getName() + "§a foi cancelada por falta de participantes.");
                        mission.setStage(MissionStage.ENDING);

                        break;
                    }

                    break;
                }

                case ENDING -> {

                    MissionType type = mission.getType();

                    if (type != null) {

                        MissionType.EventAction action = type.getWinAction();

                        if (action != null) {

                            action.execute(mission.getParticipants());
                        }
                    }

                    iterator.remove();

                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {

        if (!event.isCancelled()) {

            Player player = event.getPlayer();

            Mission mission = MissionMain.getInstance().getMissionController().stream().filter(m -> m.getParticipants().contains(player.getUniqueId())).findFirst().orElse(null);

            if (mission == null || !mission.getType().equals(MissionType.BREAK_BLOCKS)) {
                return;
            }

            MissionType type = mission.getType();

            int limit = type.getLimit();

            if (mission.getAccumulated() < limit) {

                mission.setAccumulated(mission.getAccumulated() + 1);

                if (mission.getAccumulated() == limit) {

                    mission.sendTitle("§aParabéns", "§fMissão concluída");
                    mission.playSound(Sound.LEVEL_UP);

                    mission.setStage(MissionStage.ENDING);
                }
            }
        }
    }
}