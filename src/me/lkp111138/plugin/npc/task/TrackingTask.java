package me.lkp111138.plugin.npc.task;

import me.lkp111138.plugin.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.List;

public class TrackingTask implements Runnable {
    private int counter = 0;

    @Override
    public void run() {
        ++counter;
        NPC.getActiveNPCList().forEach(npc -> {
            boolean found = false;
            if (!npc.isEyeContact()) {
                return;
            }
            if (npc.getTrackingEntity() != null) {
                Vector trackedLoc = npc.getTrackingEntity().getLocation().toVector();
                Vector myLoc = npc.getVillager().getLocation().toVector();
                if (myLoc.distanceSquared(trackedLoc) > npc.getTrackingRange() * npc.getTrackingRange()) {
                    npc.setTrackingEntity(null);
                } else {
                    found = true;
                    makeFirstFaceSecond(npc.getVillager(), npc.getTrackingEntity());
                }
            }
            for (int i = 0; i < npc.getStartTrackingRange(); i++) {
                List<Entity> entities = npc.getVillager().getNearbyEntities(i,4, i);
                for (Entity e : entities) {
                    if (e.getType().equals(EntityType.PLAYER)) {
                        makeFirstFaceSecond(npc.getVillager(), e);
                        npc.setTrackingEntity(e);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found && counter % 8 == 0) {
                Location loc = npc.getVillager().getLocation();
                loc.setPitch(0);
                npc.getVillager().teleport(loc);
            }
        });
    }

    private void makeFirstFaceSecond(Entity first, Entity second) {
        Location loc = first.getLocation();
        Vector villagerVec = loc.toVector();
        Vector playerVec = second.getLocation().toVector();
        Vector direction = playerVec.add(villagerVec.multiply(-1)).normalize();
        loc.setDirection(direction);
        first.teleport(loc);
    }
}
