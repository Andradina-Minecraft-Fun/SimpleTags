package me.refracdevelopment.simpletags.manager;

import me.refracdevelopment.simpletags.player.Profile;
import me.refracdevelopment.simpletags.utilities.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager() {
        // Refresh to remove profiles from a previous instance of plugin
        // This is basically /reload support (not recommended)
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            getProfiles().clear();
            handleProfileCreation(onlinePlayer.getUniqueId(), onlinePlayer.getName());
            Tasks.runAsync(() -> getProfile(onlinePlayer.getUniqueId()).getData().load());
        });
    }

    public void handleProfileCreation(UUID uuid, String name) {
        if (!this.profiles.containsKey(uuid)) {
            profiles.put(uuid, new Profile(uuid, name));
        }
    }

    public Profile getProfile(Object object) {
        if (object instanceof Player) {
            Player target = (Player) object;
            if (!this.profiles.containsKey(target.getUniqueId())) {
                return null;
            }
            return profiles.get(target.getUniqueId());
        }
        if (object instanceof UUID) {
            UUID uuid = (UUID) object;
            if (!this.profiles.containsKey(uuid)) {
                return null;
            }
            return profiles.get(uuid);
        }
        if (object instanceof String) {
            return this.profiles.values().stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(object.toString())).findFirst().orElse(null);
        }
        return null;
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(Map<UUID, Profile> profiles) {
        this.profiles = profiles;
    }
}