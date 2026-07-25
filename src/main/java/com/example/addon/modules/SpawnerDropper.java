package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SpawnerDropper extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> range = sgGeneral.add(new IntegerSetting.Builder()
        .name("range")
        .description("Khoảng cách tìm Spawner.")
        .defaultValue(4)
        .min(1)
        .sliderMax(6)
        .build()
    );

    private final Setting<List<Item>> trashItems = sgGeneral.add(new ItemListSetting.Builder()
        .name("trash-items")
        .description("Các vật phẩm rác cần tự động vứt.")
        .defaultValue(List.of(Items.ROTTEN_FLESH, Items.BONE, Items.STRING))
        .build()
    );

    public SpawnerDropper(Category category) {
        super(category, "spawner-dropper", "Tự động đào Spawner và vứt rác.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        // 1. Tìm và đào Spawner
        BlockPos playerPos = mc.player.getBlockPos();
        for (int x = -range.get(); x <= range.get(); x++) {
            for (int y = -range.get(); y <= range.get(); y++) {
                for (int z = -range.get(); z <= range.get(); z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (mc.world.getBlockState(pos).getBlock() == Blocks.SPAWNER) {
                        BlockUtils.breakBlock(pos, true);
                    }
                }
            }
        }

        // 2. Vứt rác
        for (Item item : trashItems.get()) {
            FindItemResult result = InvUtils.find(item);
            if (result.found()) {
                InvUtils.drop(result.slot());
            }
        }
    }
}

