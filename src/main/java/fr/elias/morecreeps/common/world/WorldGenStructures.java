package fr.elias.morecreeps.common.world;

import java.util.Random;

import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class WorldGenStructures implements IWorldGenerator {

	@SuppressWarnings("rawtypes")
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider)
	{
		generateSurface(world, world.rand, chunkX, chunkZ);
	}
    public void generateSurface(World world, Random random, int i, int j)
    {
        if (CREEPSConfig.pyramids >= (1100 - CREEPSConfig.pyramidRarity * 100) + 100 && CREEPSConfig.pyramidGen)
        {
            if (random.nextInt(30) == 0)
            {
                int k = i + random.nextInt(16) + 16;
                byte byte0 = 65;
                int j1 = j + random.nextInt(16) + 16;

                if ((new CREEPSWorldGenPyramid()).generate(world, random, k, byte0, j1))
                {
                	CREEPSConfig.pyramids = 0;
                	world.playSound(null, null, SoundsHandler.PYRAMID_DISCOVERED, SoundCategory.VOICE, 0.95F, 1.0F);
                }
            }
        }
        else
        {
        	CREEPSConfig.pyramids++;
        }

        if (CREEPSConfig.castlecount >= (1100 - CREEPSConfig.castleRarity * 100) + 1300 && CREEPSConfig.castleGen)
        {
            if (random.nextInt(30) == 0)
            {
                int l = i + random.nextInt(16) + 16;
                int i1 = random.nextInt(40) + 80;
                int k1 = j + random.nextInt(16) + 16;

                if ((new CREEPSWorldGenCastle()).generate(world, random, l, i1, k1))
                {
                	CREEPSConfig.castlecount = 0;
                	world.playSound(null, null, SoundsHandler.BATTLE_CASTLE, SoundCategory.VOICE, 0.95F, 1.0F);
                }
            }
        }
        else
        {
        	CREEPSConfig.castlecount++;
        }
    }
}
