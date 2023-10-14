package io.github.tanguygab.spygotsecurity.utils;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import org.bukkit.block.*;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest.Type;

public class MultiBlockUtils {

    public static Block getSide(Block block) {
        if (block.getBlockData() instanceof org.bukkit.block.data.type.Chest data
                && block.getState() instanceof Chest state
                && state.getInventory().getHolder() instanceof DoubleChest chest) {
            Chest other = (Chest) (data.getType() == org.bukkit.block.data.type.Chest.Type.RIGHT
                    ? chest.getRightSide() : chest.getLeftSide());

            if (other != null) return other.getBlock();
        }
        return null;
    }

    public static boolean isMultiBlock(LockedBlock locked, LockedBlock other) {
        return other != null && other.getOwner().equals(locked.getOwner());
    }

    public static void setSingle(Block... blocks) {
        for (Block block : blocks)
            if (block != null && block.getBlockData() instanceof org.bukkit.block.data.type.Chest data) {
                data.setType(Type.SINGLE);
                block.setBlockData(data);
            }
    }

}
