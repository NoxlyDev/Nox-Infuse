package com.catadmirer.infuseSMP.managers;

import com.catadmirer.infuseSMP.Infuse;
import com.catadmirer.infuseSMP.effects.InfuseEffect;
import com.catadmirer.infuseSMP.util.BedrockUtil;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarUpdater extends BukkitRunnable {
    private final Key EFFECTS_FONT = Key.key("infuse", "effects");
    private final Infuse plugin;

    public ActionBarUpdater(Infuse plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();

            // Composing the action bar
            InfuseEffect effect;

            // Bedrock has no custom fonts: U+FFFF is a noncharacter, so use a private-use glyph (glyph_E0 cell FF) instead.
            char emptyIcon = BedrockUtil.isBedrock(uuid) ? '\uE0FF' : '\uffff';
            char placeholder = plugin.getMainConfig().emptyEffectIcon() ? emptyIcon : ' ';

            char leftIcon = placeholder;
            char rightIcon = placeholder;

            // Loading info for the first effect
            effect = plugin.getDataManager().getEffect(uuid, "1");
            if (effect != null) {
                leftIcon = effect.getIcon(player, "1");
            }

            // Loading info for the second effect
            effect = plugin.getDataManager().getEffect(uuid, "2");
            if (effect != null) {
                rightIcon = effect.getIcon(player, "2");
            }

            // Sending the action bar
            player.sendActionBar(Component.text(leftIcon + " " + rightIcon).font(EFFECTS_FONT));
        });
    }
}
