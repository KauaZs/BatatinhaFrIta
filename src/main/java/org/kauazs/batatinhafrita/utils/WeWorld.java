package org.kauazs.batatinhafrita.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockTypes;

public class WeWorld {

    public static void deleteWall(org.bukkit.World world, String pos1, String pos2) throws MaxChangedBlocksException {
        CuboidRegion region = createRegion(world, pos1, pos2);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(region.getWorld())) {
            editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
        }
    }

    public static void restoreWall(org.bukkit.World world, String pos1, String pos2, String blockType) throws MaxChangedBlocksException {
        CuboidRegion region = createRegion(world, pos1, pos2);

        var block = BlockTypes.get(blockType.toLowerCase());
        if (block == null) {
            System.out.println("Bloco inválido: " + blockType);
            return;
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(region.getWorld())) {
            editSession.setBlocks(region, block.getDefaultState());
        }
    }

    public static CuboidRegion createRegion(org.bukkit.World world, String pos1, String pos2) {
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);

        int[] p1 = parsePos(pos1);
        int[] p2 = parsePos(pos2);

        BlockVector3 position1 = BlockVector3.at(p1[0], p1[1], p1[2]);
        BlockVector3 position2 = BlockVector3.at(p2[0], p2[1], p2[2]);

        return new CuboidRegion(weWorld, position1, position2);
    }

    public static int[] parsePos(String pos) {
        String[] split = pos.replace(" ", "").split(",");
        return new int[] {
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
        };
    }
}
