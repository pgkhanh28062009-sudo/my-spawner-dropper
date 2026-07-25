package com.example.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class SpawnerDropper extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> breakSpawner = sgGeneral.add(new BoolSetting.Builder()
        .name("break-spawner")
        .description("Tự động đập Spawner khi ở gần.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("Khoảng cách quét Spawner.")
        .defaultValue(4)
        .min(1)
        .sliderMax(6)
        .build()
    );

    private final Setting<Boolean> autoDrop = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-drop")
        .description("Tự động vứt rác khỏi túi đồ.")
        .defaultValue(true)
        .build()
    );

    public SpawnerDropper(Category category) {
        super(category, "spawner-dropper", "Tự động đập spawner và vứt rác.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        // 1. Tự động đập Spawner
        if (breakSpawner.get()) {
            BlockPos playerPos = mc.player.getBlockPos();
            int r = range.get();

            for (int x = -r; x <= r; x++) {
                for (int y = -r; y <= r; y++) {
                    for (int z = -r; z <= r; z++) {
                        BlockPos pos = playerPos.add(x, y, z);
                        if (mc.world.getBlockState(pos).getBlock() == Blocks.SPAWNER) {
                            mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                            mc.player.swingHand(Hand.MAIN_HAND);
                            return;
                        }
                    }
                }
            }
        }

        // 2. Tự động vứt rác (Trash Dropper)
        if (autoDrop.get()) {
            for (int i = 9; i < 45; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (isTrash(stack)) {
                    mc.interactionManager.dropStack(i);
                }
            }
        }
    }

    private boolean isTrash(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() == Items.ROTTEN_FLESH 
            || stack.getItem() == Items.BONE 
            || stack.getItem() == Items.STRING 
            || stack.getItem() == Items.SPIDER_EYE;
    }
}
