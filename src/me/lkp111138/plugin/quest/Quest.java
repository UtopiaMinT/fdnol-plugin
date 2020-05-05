package me.lkp111138.plugin.quest;

import me.lkp111138.plugin.item.CustomItem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {
    public static String advanceText;
    public static String dialogPrefix;
    public static String finishedText;
    public static String startedText;
    private static Map<Integer, Quest> npcQuests = new HashMap<>();

    public final String id;
    public final String name;
    public final int reqLevel;
    public final Map<CustomItem, Integer> reqItems;
    public final List<String> reqQuests;
    public final List<String> reqLevelHint;
    public final List<String> reqItemsHint;
    public final List<String> reqQuestsHint;
    public final List<String> unavailableHint;
    public final int cooldown;
    public final List<QuestStage> stages;
    public final int rewardXP;
    public final Map<CustomItem, Integer> rewardItems;
    public final int startNPC;
    public final List<String> startDialog;

    public static void init(ConfigurationSection section) {
        advanceText = section.getString("advance_text");
        dialogPrefix = section.getString("dialog_prefix");
        finishedText = section.getString("finished_text");
        startedText = section.getString("started_text");
    }
    
    public static Quest forNPC(int npc) {
        return npcQuests.get(npc);
    }

    public Quest(String id, ConfigurationSection section) {
        this.id = id;
        this.name = section.getString("name");
        this.reqLevel = section.getInt("req.level", 0);
        this.reqLevelHint = section.getStringList("req_hint.level");
        Map<CustomItem, Integer> reqItems = new HashMap<>();
        ConfigurationSection reqItemSection = section.getConfigurationSection("req.items");
        if (reqItemSection != null) {
            for (String key : reqItemSection.getKeys(false)) {
                CustomItem item = CustomItem.getItem(key);
                if (item != null) {
                    reqItems.put(item, reqItemSection.getInt(key));
                }
            }
        }
        this.reqItems = reqItems;
        this.reqItemsHint = section.getStringList("req_hint.items");
        this.reqQuests = section.getStringList("req.quest");
        this.reqQuestsHint = section.getStringList("req_hint.quest");
        this.unavailableHint = section.getStringList("unavailable_hint");
        this.cooldown = section.getInt("cooldown");
        this.rewardXP = section.getInt("reward.xp");
        Map<CustomItem, Integer> rewardItems = new HashMap<>();
        ConfigurationSection rewardItemSection = section.getConfigurationSection("reward.items");
        if (rewardItemSection != null) {
            for (String key : rewardItemSection.getKeys(false)) {
                CustomItem item = CustomItem.getItem(key);
                if (item != null) {
                    rewardItems.put(item, rewardItemSection.getInt(key));
                }
            }
        }
        this.rewardItems = rewardItems;
        this.startNPC = section.getInt("start_npc");
        this.startDialog = section.getStringList("start_dialog");
        List<QuestStage> list = new ArrayList<>();
        List<Map<?, ?>> stages = section.getMapList("stages");
        for (int i = 0; i < stages.size(); i++) {
            Map section1 = stages.get(i);
            QuestStage questStage = new QuestStage(section1, i == stages.size() - 1);
            list.add(questStage);
        }
        this.stages = list;
    }

    public class QuestStage {
        public final int npc;
        public final List<String> dialog;
        public final Map<CustomItem, Integer> reqItems;
        public final Map<CustomItem, Integer> rewardItems;
        public final List<String> hint;
        public final boolean isFinal;

        @SuppressWarnings("unchecked")
        QuestStage(Map section, boolean isFinal) {
            this.npc = (int) section.get("npc");
            this.dialog = (List<String>) section.get("dialog");
            Map<CustomItem, Integer> reqItems = new HashMap<>();
            Map reqItemSection = (Map) section.get("items");
            if (reqItemSection != null) {
                for (Object key : reqItemSection.keySet()) {
                    CustomItem item = CustomItem.getItem((String) key);
                    if (item != null) {
                        reqItems.put(item, (Integer) reqItemSection.get(key));
                    }
                }
            }
            this.reqItems = reqItems;
            Map<CustomItem, Integer> rewardItems = new HashMap<>();
            Map rewardItemSection = (Map) section.get("reward");
            if (rewardItemSection != null) {
                for (Object key : rewardItemSection.keySet()) {
                    CustomItem item = CustomItem.getItem((String) key);
                    if (item != null) {
                        rewardItems.put(item, (Integer) rewardItemSection.get(key));
                    }
                }
            }
            this.rewardItems = rewardItems;
            this.hint = (List<String>) section.get("hint");
            Quest last = npcQuests.put(startNPC, Quest.this);
            if (last != null && last != Quest.this) {
                throw new RuntimeException(String.format("Two quests on NPC #%s! (%s, %s)", startNPC, last.id, Quest.this.id));
            }
            last = npcQuests.put(npc, Quest.this);
            if (last != null && last != Quest.this) {
                throw new RuntimeException(String.format("Two quests on NPC #%s! (%s, %s)", npc, last.id, Quest.this.id));
            }
            this.isFinal = isFinal;
        }
    }
}
