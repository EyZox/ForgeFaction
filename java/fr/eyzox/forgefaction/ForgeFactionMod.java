package fr.eyzox.forgefaction;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.eyzox.forgefaction.block.ModBlocks;
import fr.eyzox.forgefaction.command.ClaimCommand;
import fr.eyzox.forgefaction.command.CreateFactionCommand;
import fr.eyzox.forgefaction.command.InvitePlayerCommand;
import fr.eyzox.forgefaction.command.KickPlayerCommand;
import fr.eyzox.forgefaction.command.LeaveFactionCommand;
import fr.eyzox.forgefaction.command.ViewTerritoryCommand;
import fr.eyzox.forgefaction.network.TerritoryChunkPacket;
import fr.eyzox.forgefaction.proxy.CommonProxy;

@Mod(modid = ForgeFactionMod.MODID, version = ForgeFactionMod.VERSION)
public class ForgeFactionMod
{
	public static final String MODID = "forgefaction";
	public static final String VERSION = "1.0";

	private static ForgeFactionMod INSTANCE;

	public ForgeFactionMod() {
		INSTANCE = this;
	}

	@SidedProxy(clientSide = "fr.eyzox.forgefaction.proxy.ClientProxy", serverSide = "fr.eyzox.forgefaction.proxy.CommonProxy")
	private static CommonProxy proxy;

	private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	private void registerChannel() {
		channel.registerMessage(TerritoryChunkPacket.TerritoryChunkPaquetHandler.class, TerritoryChunkPacket.class,0, Side.CLIENT);
	}

	private void registerCommand() {
		ServerCommandManager m = (ServerCommandManager) MinecraftServer.getServer().getCommandManager();
		m.registerCommand(new CreateFactionCommand());
		m.registerCommand(new InvitePlayerCommand());
		m.registerCommand(new LeaveFactionCommand());
		m.registerCommand(new KickPlayerCommand());
		m.registerCommand(new ClaimCommand());
		m.registerCommand(new ViewTerritoryCommand());
	}

	public static ForgeFactionMod getInstance() { return INSTANCE;}

	public CommonProxy getProxy() {
		return proxy;
	}

	public SimpleNetworkWrapper getChannel() {
		return channel;
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModBlocks.registerBlock();

		proxy.registerEvent();
		registerChannel();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		registerCommand();
	}
}
