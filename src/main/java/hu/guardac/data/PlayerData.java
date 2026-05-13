package hu.guardac.data;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {

    private final UUID uuid;
    private final Map<String, Integer> violations = new HashMap<>();

    private Location lastLocation;
    private long lastMoveTime = System.currentTimeMillis();
    private double lastDeltaX, lastDeltaY, lastDeltaZ;
    private boolean lastOnGround;
    private int airTicks;

    private float lastYaw, lastPitch;
    private float deltaYaw, deltaPitch;

    private long lastAttackTime;
    private int cpsCount;
    private long cpsResetTime;
    private Entity lastTarget;

    private int packetCount;
    private long packetResetTime = System.currentTimeMillis();

    private boolean expectingVelocity;
    private long velocitySentTime;

    private int scaffoldTicks;

    private long lastInventoryOpen;
    private boolean inventoryOpen;

    private boolean bypassing;

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.lastLocation = player.getLocation().clone();
        this.lastYaw   = player.getLocation().getYaw();
        this.lastPitch = player.getLocation().getPitch();
    }

    public void addViolation(String check) { violations.merge(check, 1, Integer::sum); }
    public int getViolations(String check) { return violations.getOrDefault(check, 0); }
    public void resetViolations(String check) { violations.remove(check); }
    public void resetAllViolations() { violations.clear(); }
    public Map<String, Integer> getAllViolations() { return Collections.unmodifiableMap(violations); }

    public UUID getUuid() { return uuid; }
    public Location getLastLocation() { return lastLocation; }
    public void setLastLocation(Location l) { this.lastLocation = l; }
    public long getLastMoveTime() { return lastMoveTime; }
    public void setLastMoveTime(long t) { this.lastMoveTime = t; }
    public double getLastDeltaX() { return lastDeltaX; }
    public void setLastDeltaX(double v) { this.lastDeltaX = v; }
    public double getLastDeltaY() { return lastDeltaY; }
    public void setLastDeltaY(double v) { this.lastDeltaY = v; }
    public double getLastDeltaZ() { return lastDeltaZ; }
    public void setLastDeltaZ(double v) { this.lastDeltaZ = v; }
    public boolean isLastOnGround() { return lastOnGround; }
    public void setLastOnGround(boolean b) { this.lastOnGround = b; }
    public int getAirTicks() { return airTicks; }
    public void setAirTicks(int t) { this.airTicks = t; }
    public float getLastYaw() { return lastYaw; }
    public void setLastYaw(float y) { this.lastYaw = y; }
    public float getLastPitch() { return lastPitch; }
    public void setLastPitch(float p) { this.lastPitch = p; }
    public float getDeltaYaw() { return deltaYaw; }
    public void setDeltaYaw(float v) { this.deltaYaw = v; }
    public float getDeltaPitch() { return deltaPitch; }
    public void setDeltaPitch(float v) { this.deltaPitch = v; }
    public long getLastAttackTime() { return lastAttackTime; }
    public void setLastAttackTime(long t) { this.lastAttackTime = t; }
    public int getCpsCount() { return cpsCount; }
    public void setCpsCount(int c) { this.cpsCount = c; }
    public long getCpsResetTime() { return cpsResetTime; }
    public void setCpsResetTime(long t) { this.cpsResetTime = t; }
    public Entity getLastTarget() { return lastTarget; }
    public void setLastTarget(Entity e) { this.lastTarget = e; }
    public int getPacketCount() { return packetCount; }
    public void setPacketCount(int c) { this.packetCount = c; }
    public long getPacketResetTime() { return packetResetTime; }
    public void setPacketResetTime(long t) { this.packetResetTime = t; }
    public boolean isExpectingVelocity() { return expectingVelocity; }
    public void setExpectingVelocity(boolean b) { this.expectingVelocity = b; }
    public long getVelocitySentTime() { return velocitySentTime; }
    public void setVelocitySentTime(long t) { this.velocitySentTime = t; }
    public int getScaffoldTicks() { return scaffoldTicks; }
    public void setScaffoldTicks(int t) { this.scaffoldTicks = t; }
    public long getLastInventoryOpen() { return lastInventoryOpen; }
    public void setLastInventoryOpen(long t) { this.lastInventoryOpen = t; }
    public boolean isInventoryOpen() { return inventoryOpen; }
    public void setInventoryOpen(boolean b) { this.inventoryOpen = b; }
    public boolean isBypassing() { return bypassing; }
    public void setBypassing(boolean b) { this.bypassing = b; }
}
