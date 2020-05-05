package me.lkp111138.plugin.quest;

public class QuestProgress {
    private String questId;
    private int stage;
    private long timestamp;

    public QuestProgress(String questId, int stage, long timestamp) {
        this.questId = questId;
        this.stage = stage;
        this.timestamp = timestamp;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}